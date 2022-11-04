package com.github.nikyotensai.config.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 表数据查询
 * </p>
 */
@AllArgsConstructor
@Getter
public enum QuerySQL {
    MYSQL("mysql", "show tables", "show table status",
            "show full fields from %s",
            "NAME", "COMMENT", "FIELD", "TYPE", "COMMENT", "KEY"),
    ;

    private final String dbType;
    private final String tablesSql;
    private final String tableCommentsSql;
    private final String tableFieldsSql;
    private final String tableName;
    private final String tableComment;
    private final String fieldName;
    private final String fieldType;
    private final String fieldComment;
    private final String fieldKey;


}
