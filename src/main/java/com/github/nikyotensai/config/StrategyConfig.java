package com.github.nikyotensai.config;

import com.github.nikyotensai.config.rules.IdStrategy;
import com.github.nikyotensai.config.rules.NamingStrategy;

/**
 * 策略配置项
 */
public class StrategyConfig {

    /**
     * 数据库表映射到实体的命名策略
     */
    private NamingStrategy naming;

    private NamingStrategy fieldNaming;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * Entity 中的ID生成类型
     */
    private IdStrategy idGenType;

    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superEntityClass;

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    private String superMapperClass;

    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    private String superServiceImplClass;

    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superControllerClass;

    /*
     * 需要包含的表名（与exclude二选一配置）
     */
    private String[] include = null;

    /**
     * 需要排除的表名
     */
    private String[] exclude = null;

    public NamingStrategy getNaming() {
        return naming;
    }

    public NamingStrategy getFieldNaming() {
        if (fieldNaming == null) {
            return naming;
        }
        return fieldNaming;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public IdStrategy getIdGenType() {
        return idGenType;
    }

    public String[] getInclude() {
        return include;
    }

    public String[] getExclude() {
        return exclude;
    }

    public String getSuperEntityClass() {
        return superEntityClass;
    }

    public void setSuperEntityClass(String superEntityClass) {
        this.superEntityClass = superEntityClass;
    }

    public String getSuperMapperClass() {
        return superMapperClass;
    }

    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }

    public String getSuperControllerClass() {
        return superControllerClass;
    }

}
