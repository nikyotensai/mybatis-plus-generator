package com.github.nikyotensai;

import com.github.nikyotensai.config.ConstVal;
import com.github.nikyotensai.config.TemplateConfig;
import com.github.nikyotensai.config.builder.ConfigBuilder;
import com.github.nikyotensai.config.po.TableField;
import com.github.nikyotensai.config.po.TableInfo;
import com.github.nikyotensai.config.util.ClassLoaderUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成文件
 */
@Mojo(name = "code", threadSafe = true)
public class GenerateMojo extends AbstractGenerateMojo {

    /**
     * velocity引擎
     */
    private VelocityEngine engine;

    /**
     * 输出文件
     */
    private Map<String, String> outputFiles;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            ClassLoaderUtil.init(project.getSystemClasspathElements());
            log.info("==========================准备生成文件...==========================");
            // 初始化配置
            initConfig();
            // 初始化输出文件路径模板
            initOutputFiles();
            // 创建输出文件路径
            mkdirs(config.getPathInfo());
            // 获取上下文
            Map<String, VelocityContext> ctxData = analyzeData(config);
            // 循环生成文件
            for (Map.Entry<String, VelocityContext> ctx : ctxData.entrySet()) {
                batchOutput(ctx.getKey(), ctx.getValue());
            }
            log.info("==========================文件生成完成！！！==========================");
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * 分析数据
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    private Map<String, VelocityContext> analyzeData(ConfigBuilder config) {
        List<TableInfo> tableList = config.getTableInfoList();
        Map<String, String> packageInfo = config.getPackageInfo();
        Map<String, VelocityContext> ctxData = new HashMap<>();
        String superEntityClass = getSuperClassName(config.getSuperEntityClass());
        String superMapperClass = getSuperClassName(config.getSuperMapperClass());
        String superServiceImplClass = getSuperClassName(config.getSuperServiceImplClass());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        for (TableInfo tableInfo : tableList) {
            VelocityContext ctx = new VelocityContext();
            List<TableField> tableFields = tableInfo.getFields().stream()
                    .filter(tableField -> "BigDecimal".equalsIgnoreCase(tableField.getPropertyType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tableFields)) {
                ctx.put("havBigDecimal", true);
            } else {
                ctx.put("havBigDecimal", false);
            }

            ctx.put("package", packageInfo);
            ctx.put("table", tableInfo);
            ctx.put("entity", tableInfo.getEntityName());
            ctx.put("addTabeName", !tableInfo.getEntityName().equalsIgnoreCase(tableInfo.getName()));
            ctx.put("idGenType", config.getIdType());
            ctx.put("superEntityClassPackage", config.getSuperEntityClass());
            ctx.put("superEntityClass", superEntityClass);
            ctx.put("superMapperClassPackage", config.getSuperMapperClass());
            ctx.put("superMapperClass", superMapperClass);
            ctx.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
            ctx.put("superServiceImplClass", superServiceImplClass);
            ctx.put("enableCache", generatorConfig.isEnableCache());
            ctx.put("activeRecord", generatorConfig.isActiveRecord());
            ctx.put("author", generatorConfig.getStrategy().getAuthor());
            ctx.put("date", date);
            ctxData.put(tableInfo.getEntityName(), ctx);
        }
        return ctxData;
    }

    /**
     * 获取类名
     *
     * @param classPath
     * @return
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isBlank(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }

    /**
     * 处理输出目录
     *
     * @param pathInfo 路径信息
     */
    private void mkdirs(Map<String, String> pathInfo) {
        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
            File dir = new File(entry.getValue());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    log.info("创建目录： [" + entry.getValue() + "]");
                }
            }
        }
    }

    /**
     * 初始化输出目录
     */
    private void initOutputFiles() {
        outputFiles = new HashMap<String, String>();
        Map<String, String> pathInfo = config.getPathInfo();
        outputFiles.put(ConstVal.ENTITY, pathInfo.get(ConstVal.ENTITY_PATH) + ConstVal.ENTITY_NAME);
        outputFiles.put(ConstVal.MAPPER, pathInfo.get(ConstVal.MAPPER_PATH) + ConstVal.MAPPER_NAME);
        outputFiles.put(ConstVal.XML, pathInfo.get(ConstVal.XML_PATH) + ConstVal.XML_NAME);
        outputFiles.put(ConstVal.SERVICEIMPL, pathInfo.get(ConstVal.SERVICEIMPL_PATH) + ConstVal.SERVICEIMPL_NAME);
    }

    /**
     * 合成上下文与模板
     *
     * @param context vm上下文
     */
    private void batchOutput(String entityName, VelocityContext context) {
        try {
            String entityFile = String.format(outputFiles.get(ConstVal.ENTITY), entityName);
            String mapperFile = String.format(outputFiles.get(ConstVal.MAPPER), entityName);
            String xmlFile = String.format(outputFiles.get(ConstVal.XML), entityName);
            String implFile = String.format(outputFiles.get(ConstVal.SERVICEIMPL), entityName);

            TemplateConfig template = config.getTemplate();

            // 根据override标识来判断是否需要创建文件（entity不判断）
            vmToFile(context, template.getEntity(), entityFile);
            if (isCreate(mapperFile)) {
                vmToFile(context, template.getMapper(), mapperFile);
            }
            if (isCreate(xmlFile)) {
                vmToFile(context, template.getXml(), xmlFile);
            }
            if (isCreate(implFile)) {
                vmToFile(context, template.getServiceImpl(), implFile);
            }
        } catch (IOException e) {
            log.error("无法创建文件，请检查配置信息！");
            e.printStackTrace();
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param context      内容对象
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    private void vmToFile(VelocityContext context, String templatePath, String outputFile) throws IOException {
        VelocityEngine velocity = getVelocityEngine();
        Template template = velocity.getTemplate(templatePath, ConstVal.UTF8);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
        template.merge(context, writer);
        writer.close();
        log.info("模板:" + templatePath + ";  文件:" + outputFile);
    }

    /**
     * 设置模版引擎，主要指向获取模版路径
     */
    private VelocityEngine getVelocityEngine() {
        if (engine == null) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOADPATH_KEY, ConstVal.VM_LOADPATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", "true");
            engine = new VelocityEngine(p);
            engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
        }
        return engine;
    }

    /**
     * 检测文件是否存在
     *
     * @return 是否
     */
    private boolean isCreate(String filePath) {
        File file = new File(filePath);
        return !file.exists() || generatorConfig.isFileOverride();
    }

}
