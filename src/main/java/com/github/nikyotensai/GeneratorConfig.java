package com.github.nikyotensai;

import com.github.nikyotensai.config.DataSourceConfig;
import com.github.nikyotensai.config.PackageConfig;
import com.github.nikyotensai.config.StrategyConfig;
import com.github.nikyotensai.config.TemplateConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 插件基类，用于属性配置 设计成抽象类主要是用于后期可扩展，共享参数配置。
 */
@Getter
@Setter
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



}
