package com.yuhao.deepoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提交状态枚举
 * todo 是否可以将 value 修改为 Integer
 */
public enum SubmissionStatusEnum {
    SUBMITTED_FAILED("Submit Failed", "Submit Failed"),
    PENDING("Pending", "Pending"),
    COMPILING("Compiling", "Compiling"),
    JUDGING("Judging", "Judging"),
    SUCCEED("Succeed", "Succeed");

    private final String text;

    private final String value;

    SubmissionStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SubmissionStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SubmissionStatusEnum anEnum : SubmissionStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
