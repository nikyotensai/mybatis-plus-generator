package com.github.nikyotensai.config.rules;

/**
 * 数据库类型定义
 */
public enum DbType {

    MYSQL("myslq");

    private String value;

    DbType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
