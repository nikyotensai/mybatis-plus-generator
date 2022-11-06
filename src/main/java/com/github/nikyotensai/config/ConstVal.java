package com.github.nikyotensai.config;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 定义常量
 */
public interface ConstVal {

    String MODULENAME = "ModuleName";

    String ENTITY = "Entity";
    String SERVICEIMPL = "DAO";
    String MAPPER = "Mapper";
    String XML = "Xml";

    String ENTITY_PATH = "entity_path";
    String SERVICEIMPL_PATH = "serviceimpl_path";
    String MAPPER_PATH = "mapper_path";
    String XML_PATH = "xml_path";

    String JAVA_TMPDIR = "java.io.tmpdir";
    String UTF8 = Charset.forName("UTF-8").name();
    String UNDERLINE = "_";

    String JAVA_SUFFIX = ".java";
    String XML_SUFFIX = ".xml";

    String TEMPLATE_ENTITY = "/template/entity.java.vm";
    String TEMPLATE_MAPPER = "/template/mapper.java.vm";
    String TEMPLATE_XML = "/template/mapper.xml.vm";
    String TEMPLATE_SERVICEIMPL = "/template/serviceImpl.java.vm";

    String ENTITY_NAME = File.separator + "%s" + JAVA_SUFFIX;
    String MAPPER_NAME = File.separator + "%s" + MAPPER + JAVA_SUFFIX;
    String XML_NAME = File.separator + "%s" + MAPPER + XML_SUFFIX;
    String SERVICEIMPL_NAME = File.separator + "%s" + SERVICEIMPL + JAVA_SUFFIX;

    /**
     * 配置使用classloader加载资源
     */
    String VM_LOADPATH_KEY = "file.resource.loader.class";
    String VM_LOADPATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    String SUPERD_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    String SUPERD_SERVICEIMPL_CLASS = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

}
