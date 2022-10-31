package com.github.nikyotensai;

import com.github.nikyotensai.config.DataSourceConfig;
import com.github.nikyotensai.config.PackageConfig;
import com.github.nikyotensai.config.StrategyConfig;
import com.github.nikyotensai.config.TemplateConfig;

/**
 * 插件基类，用于属性配置 设计成抽象类主要是用于后期可扩展，共享参数配置。
 */
public class GeneratorConfig {

    /**
     * 数据源配置
     */
    private DataSourceConfig dataSource;

    /**
     * 数据库表配置
     */
    private StrategyConfig strategy;

    /**
     * 包 相关配置
     */
    private PackageConfig packageInfo;

    /**
     * 模板 相关配置
     */
    private TemplateConfig template;

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord;

    public DataSourceConfig getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public StrategyConfig getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyConfig strategy) {
        this.strategy = strategy;
    }

    public PackageConfig getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageConfig packageInfo) {
        this.packageInfo = packageInfo;
    }

    public TemplateConfig getTemplate() {
        return template;
    }

    public void setTemplate(TemplateConfig template) {
        this.template = template;
    }


    public boolean isFileOverride() {
        return fileOverride;
    }

    public void setFileOverride(boolean fileOverride) {
        this.fileOverride = fileOverride;
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    public boolean isActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(boolean activeRecord) {
        this.activeRecord = activeRecord;
    }

}
