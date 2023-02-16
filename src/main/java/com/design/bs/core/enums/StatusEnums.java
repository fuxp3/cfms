package com.design.bs.core.enums;

import lombok.Getter;

/**
 * @description: 状态枚举
 **/
public enum StatusEnums {
    INVALID(0, "invalid"),
    VALID(1, "valid"),
    LOCKED(2, "locked");

    @Getter
    private Integer code;

    @Getter
    private String msg;

    StatusEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static StatusEnums getInstance(Integer code) {
        StatusEnums[] statusEnums = StatusEnums.values();
        for (StatusEnums status : statusEnums) {
            if (status.getCode() == code.intValue())
                return status;
        }

        throw new IllegalArgumentException("code参数非法，无法找到对应的枚举对象");
    }

}
