package com.github.nikyotensai.config;

import com.github.nikyotensai.config.rules.IdStrategy;
import com.github.nikyotensai.config.rules.NamingStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * 策略配置项
 */
@Getter
@Setter
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


    public NamingStrategy getFieldNaming() {
        if (fieldNaming == null) {
            return naming;
        }
        return fieldNaming;
    }


}
