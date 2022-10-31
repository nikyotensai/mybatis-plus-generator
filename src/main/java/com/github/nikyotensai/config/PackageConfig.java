package com.github.nikyotensai.config;

import org.apache.commons.lang.StringUtils;

/**
 * 跟包相关的配置项
 */
public class PackageConfig {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent;

    /**
     * 父包模块名。
     */
    private String moduleName;

    /**
     * Entity包名
     */
    private String entity;

    /**
     * DAO包名
     */
    private String dao;

    /**
     * Mapper包名
     */
    private String mapper;

    /**
     * Mapper XML包名
     */
    private String xml;

    /**
     * Controller包名
     */
    private String controller;

    public String getParent() {
        if (moduleName != null && !"".equals(moduleName.trim())) {
            return parent + "." + moduleName;
        }
        return parent;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getEntity() {
        return entity;
    }

    public String getMapper() {
        return mapper;
    }

    public String getXml() {
        return xml;
    }

    public String getDao() {
        return dao;
    }

    public String getController() {
        if (StringUtils.isBlank(controller)) {
            return "web";
        }
        return controller;
    }
}
