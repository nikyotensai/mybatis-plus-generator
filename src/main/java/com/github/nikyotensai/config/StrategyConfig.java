package com.github.nikyotensai.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.nikyotensai.config.rules.NamingStrategy;
import com.github.nikyotensai.config.util.ClassLoaderUtil;
import com.github.nikyotensai.config.util.LogUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

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

    private String author = "nikyotensai";

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * Entity 中的ID生成类型
     */
    private IdType idGenType;

    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superEntityClass;

    /**
     * 自定义基础的Entity类，公共字段
     */
    private String[] superEntityColumns;

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


    public String[] getSuperEntityColumns() {
        if (superEntityColumns != null || StringUtils.isBlank(superEntityClass)) {
            return this.superEntityColumns;
        }

        try {
            Class<?> clazz = ClassLoaderUtil.getClassLoader().loadClass(superEntityClass);
            List<Field> fields = TableInfoHelper.getAllFields(clazz);
            this.superEntityColumns = fields.stream().map(field -> {
                if (null == getFieldNaming() || getFieldNaming() == NamingStrategy.nochange) {
                    return field.getName();
                }
                return StringUtils.camelToUnderline(field.getName());
            }).distinct().toArray(String[]::new);
        } catch (Exception ex) {
            LogUtil.error("getSuperEntityColumns error:" + ex.getMessage());
        }
        return this.superEntityColumns;
    }

}
