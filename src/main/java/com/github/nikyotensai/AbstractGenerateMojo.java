package com.github.nikyotensai;

import com.github.nikyotensai.config.builder.ConfigBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 插件基类，用于属性配置 设计成抽象类主要是用于后期可扩展，共享参数配置。
 */
public abstract class AbstractGenerateMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;

    @Parameter
    private String configurationFile;

    protected ConfigBuilder config;

    protected GeneratorConfig generatorConfig;


    /**
     * 日志工具
     */
    protected Log log = getLog();

    /**
     * 初始化配置
     */
    protected void initConfig() throws Exception {
        if (generatorConfig == null) {
            InputStream fis = getConfigFileStream();
            generatorConfig = yaml2Config(fis);
        }
        if (config == null) {
            config = new ConfigBuilder(generatorConfig.getPackageInfo(), generatorConfig.getDataSource(), generatorConfig.getStrategy(), generatorConfig.getTemplate(), project.getBasedir() + "/src/main/java");
        }
    }

    private InputStream getConfigFileStream() throws FileNotFoundException {
        if (StringUtils.isNotEmpty(configurationFile)) {
            return new FileInputStream(configurationFile);
        }
        return this.getClass().getClassLoader().getResourceAsStream("generatorConfig.yml");
    }

    private GeneratorConfig yaml2Config(InputStream inputStream) {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.loadAs(inputStream, GeneratorConfig.class);
    }

}
