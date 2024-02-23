package com.yuhao.deepoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评测结果枚举
 */
public enum JudgeResultEnum {
    Accepted("Accepted", "Accepted"),
    COMPILE_ERROR("Compile Error", "Compile Error"),
    PRESENTATION_ERROR("Presentation Error", "Presentation Error"),
    WRONG_ANSWER("Wrong Answer", "Wrong Answer"),
    RUNTIME_ERROR("Runtime Error", "Runtime Error"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "Time Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "Memory Limit Exceeded"),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "Output Limit Exceeded"),
    SYSTEM_ERROR("System Error", "System Error"),
    CANCELLED("Cancelled", "Cancelled"),
    HACKED("Hacked", "Hacked");

    private final String text;

    private final String value;

    JudgeResultEnum(String text, String value) {
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
    public static JudgeResultEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeResultEnum anEnum : JudgeResultEnum.values()) {
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
