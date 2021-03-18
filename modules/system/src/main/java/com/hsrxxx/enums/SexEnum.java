package com.hsrxxx.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SexEnum implements IEnum<String> {
    man("0", "男"),
    woman("1", "女"),
    other("2", "未知");

    @EnumValue
    private final String value;
    private final String name;

    SexEnum(String value, String name) {
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
