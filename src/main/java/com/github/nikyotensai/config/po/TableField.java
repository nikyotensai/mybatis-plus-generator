package com.github.nikyotensai.config.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 字段信息
 */
@Getter
@Setter
public class TableField {

    private boolean keyFlag;
    private String name;
    private String type;
    private String propertyName;
    private String propertyType;
    private String comment;

    public boolean isConvert() {
        return !name.equals(propertyName);
    }

}
