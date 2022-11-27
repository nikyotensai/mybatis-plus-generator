package com.github.nikyotensai.config.builder;

import com.github.nikyotensai.config.*;
import com.github.nikyotensai.config.po.TableField;
import com.github.nikyotensai.config.po.TableInfo;
import com.github.nikyotensai.config.rules.NamingStrategy;
import com.github.nikyotensai.config.rules.QuerySQL;
import com.github.nikyotensai.config.util.LogUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 配置汇总 传递给文件生成工具
 */
public class ConfigBuilder {

    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private QuerySQL querySQL;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceImplClass;
    /**
     * ID的字符串类型
     */
    private String idType;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;

    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;

    /**
     * 模板路径配置信息
     */
    private TemplateConfig template;

    /**
     * 在构造器中处理配置
     *
     * @param outputDir        输出目录
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     */
    public ConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                         TemplateConfig template, String outputDir) throws Exception {
        handlerPackage(outputDir, packageConfig);
        handlerDataSource(dataSourceConfig);
        handlerStrategy(strategyConfig);
        this.template = template;
    }

    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }

    /**
     * 所有路径配置
     *
     * @return 路径配置
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    public String getSuperEntityClass() {
        return superEntityClass;
    }

    public String getSuperMapperClass() {
        return superMapperClass;
    }

    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }

    /**
     * 获取ID类型
     *
     * @return id生成方式
     */
    public String getIdType() {
        return idType;
    }

    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    /**
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息
     */
    public TemplateConfig getTemplate() {
        return template == null ? new TemplateConfig() : template;
    }


    /**
     * 处理包配置
     *
     * @param config PackageConfig
     */
    private void handlerPackage(String outputDir, PackageConfig config) {
        packageInfo = new HashMap<>();
        packageInfo.put(ConstVal.MODULENAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage("", outputDir.replace("java", "resources") + "/" + config.getXml()));
        packageInfo.put(ConstVal.SERVICEIMPL, joinPackage(config.getParent(), config.getDao()));

        pathInfo = new HashMap<>();
        pathInfo.put(ConstVal.ENTITY_PATH, joinPath(outputDir, packageInfo.get(ConstVal.ENTITY)));
        pathInfo.put(ConstVal.MAPPER_PATH, joinPath(outputDir, packageInfo.get(ConstVal.MAPPER)));
        pathInfo.put(ConstVal.XML_PATH, packageInfo.get(ConstVal.XML));
        pathInfo.put(ConstVal.SERVICEIMPL_PATH, joinPath(outputDir, packageInfo.get(ConstVal.SERVICEIMPL)));
    }

    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) throws Exception {
        connection = config.getConn();
        querySQL = getQuerySQL();
    }

    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }

    /**
     * 处理superClassName,IdClassType,IdType配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isBlank(config.getSuperServiceImplClass())) {
            superServiceImplClass = ConstVal.SUPERD_SERVICEIMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (StringUtils.isBlank(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPERD_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        idType = config.getIdGenType().name();
    }

    /**
     * 处理表对应的类名称
     *
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, StrategyConfig config, NamingStrategy strategy) {
        for (TableInfo tableInfo : tableList) {
            String ignorePrefix = config.getIgnorePrefix();
            String entityName = tableInfo.getName();
            if (ignorePrefix != null && !ignorePrefix.isEmpty()) {
                entityName = entityName.replace(ignorePrefix, "");
            }
            entityName = NamingStrategy.capitalFirst(processName(entityName, strategy));
            tableInfo.setEntityName(entityName);
            tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            tableInfo.setXmlName(tableInfo.getMapperName());
            tableInfo.setServiceImplName(entityName + ConstVal.SERVICEIMPL);
        }
        return tableList;
    }

    /**
     * 获取所有的数据库表信息
     *
     * @return 表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        List<String> includes = Arrays.asList(Optional.ofNullable(config.getInclude()).orElse(new String[0]));
        List<String> excludes = Arrays.asList(Optional.ofNullable(config.getExclude()).orElse(new String[0]));
        List<TableInfo> tableList = new ArrayList<TableInfo>();
        NamingStrategy strategy = config.getNaming();
        NamingStrategy fieldStrategy = config.getFieldNaming();
        String tablePrefix = config.getTablePrefix();
        PreparedStatement pstate = null;
        try {
            pstate = connection.prepareStatement(querySQL.getTableCommentsSql());
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                String tableName = results.getString(querySQL.getTableName());
                if (tablePrefix != null && !tableName.toUpperCase().startsWith(tablePrefix.toUpperCase())) {
                    continue;
                }
                if (StringUtils.isNotBlank(tableName)) {
                    String tableComment = results.getString(querySQL.getTableComment());
                    TableInfo tableInfo = new TableInfo();
                    if (isInclude) {
                        if (includes.contains(tableName)) {
                            tableInfo.setName(tableName);
                            tableInfo.setComment(tableComment);
                        }
                    } else {
                        tableInfo.setName(tableName);
                        tableInfo.setComment(tableComment);
                    }
                    if (isExclude) {
                        if (excludes.contains(tableName)) {
                            continue;
                        }
                    }
                    if (StringUtils.isNotBlank(tableInfo.getName())) {
                        List<TableField> fieldList = getListFields(tableInfo.getName(), fieldStrategy, config.getSuperEntityColumns());
                        tableInfo.setFields(fieldList);
                        tableList.add(tableInfo);
                    }
                } else {
                    LogUtil.error("当前数据库为空！！！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (pstate != null) {
                    pstate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return processTable(tableList, config, strategy);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableName 表名称
     * @param strategy  命名策略
     * @return 表信息
     */
    private List<TableField> getListFields(String tableName, NamingStrategy strategy, String[] superEntityColumns) throws SQLException {
        boolean havedId = false;

        PreparedStatement pstate = connection.prepareStatement(String.format(querySQL.getTableFieldsSql(), tableName));
        ResultSet results = pstate.executeQuery();

        List<TableField> fieldList = new ArrayList<>();
        while (results.next()) {
            String fieldName = results.getString(querySQL.getFieldName());
            if (superEntityColumns != null && Arrays.asList(superEntityColumns).contains(fieldName)) {
                continue;
            }
            TableField field = new TableField();
            String key = results.getString(querySQL.getFieldKey());
            // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
            boolean isId = StringUtils.isNotBlank(key) && key.equalsIgnoreCase("PRI");
            // 处理ID
            if (isId && !havedId) {
                field.setKeyFlag(true);
                havedId = true;
            } else {
                field.setKeyFlag(false);
            }
            // 处理其它信息
            field.setName(results.getString(querySQL.getFieldName()));
            field.setType(results.getString(querySQL.getFieldType()));
            field.setPropertyName(processName(field.getName(), strategy));
            field.setPropertyType(processFiledType(field.getType()));
            field.setComment(results.getString(querySQL.getFieldComment()));
            fieldList.add(field);
        }
        return fieldList;
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isBlank(parent)) {
            return subPackage;
        }
        return parent + "." + subPackage;
    }

    /**
     * 处理字段类型
     *
     * @return 转换成JAVA包装类型
     */
    private String processFiledType(String type) {
        if (QuerySQL.MYSQL == querySQL) {
            return processMySqlType(type);
        }
        return null;
    }

    /**
     * 处理字段名称
     */
    private String processName(String name, NamingStrategy strategy) {
        String propertyName = "";
        if (strategy == NamingStrategy.underline_to_camel) {
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            propertyName = name;
        }
        return propertyName;
    }

    /**
     * MYSQL字段类型转换
     *
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processMySqlType(String type) {
        String t = type.toLowerCase();
        if (t.contains("char") || t.contains("text")) {
            return "String";
        } else if (t.contains("bigint")) {
            return "Long";
        } else if (t.contains("int")) {
            return "Integer";
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return "Date";
        } else if (t.contains("text")) {
            return "String";
        } else if (t.contains("bit")) {
            return "Boolean";
        } else if (t.contains("decimal")) {
            return "BigDecimal";
        } else if (t.contains("blob")) {
            return "byte[]";
        } else if (t.contains("float")) {
            return "Float";
        } else if (t.contains("double")) {
            return "Double";
        } else if (t.contains("json") || t.contains("enum")) {
            return "String";
        }
        return "String";
    }

    /**
     * ORACLE字段类型转换
     *
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processOracleType(String type) {
        String t = type.toUpperCase();
        if (t.contains("CHAR")) {
            return "String";
        } else if (t.contains("DATE") || t.contains("TIMESTAMP")) {
            return "Date";
        } else if (t.contains("NUMBER")) {
            if (t.matches("NUMBER\\(+\\d{1}+\\)")) {
                return "Integer";
            } else if (t.matches("NUMBER\\(+\\d{2}+\\)")) {
                return "Long";
            }
            return "Double";
        } else if (t.contains("FLOAT")) {
            return "Float";
        } else if (t.contains("BLOB")) {
            return "Object";
        } else if (t.contains("RAW")) {
            return "byte[]";
        }
        return "String";
    }

    /**
     * 获取当前的SQL类型
     *
     * @return DB类型
     */
    private QuerySQL getQuerySQL() {
        return QuerySQL.MYSQL;
    }

}
