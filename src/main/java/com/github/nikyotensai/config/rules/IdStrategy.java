package com.github.nikyotensai.config.rules;

/**
 * ID生成策略
 */
public enum IdStrategy {
    auto("AUTO"), none("NONE"), assign_id("ASSIGN_ID"), assign_uuid("ASSIGN_UUID"), id_worker("ID_WORKER"), uuid("UUID"), input("INPUT");

    private String value;

    IdStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
