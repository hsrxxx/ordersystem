package com.hsrxxx.system.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum implements IEnum<String> {
    normal("0", "正常"),
    stop("1", "停用");

    @EnumValue
    private final String value;
    private final String name;

    StatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
