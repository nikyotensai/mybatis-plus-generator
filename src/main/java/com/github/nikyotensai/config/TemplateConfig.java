package com.github.nikyotensai.config;

/**
 * 模板路径配置项
 */
public class TemplateConfig {

    private String entity;

    private String serviceImpl;

    private String mapper;

    private String xml;

    public String getEntity() {
        if (entity == null)
            return ConstVal.TEMPLATE_ENTITY;
        return entity;
    }


    public String getServiceImpl() {
        if (serviceImpl == null)
            return ConstVal.TEMPLATE_SERVICEIMPL;
        return serviceImpl;
    }

    public String getMapper() {
        if (mapper == null)
            return ConstVal.TEMPLATE_MAPPER;
        return mapper;
    }

    public String getXml() {
        if (xml == null)
            return ConstVal.TEMPLATE_XML;
        return xml;
    }

}
