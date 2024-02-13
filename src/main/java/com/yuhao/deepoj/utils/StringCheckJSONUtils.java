package com.yuhao.deepoj.utils;

import org.apache.commons.lang3.StringUtils;

public class StringCheckJSONUtils {
    public static boolean isJSONString(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) return false;
        if (!jsonStr.startsWith("{") || !jsonStr.endsWith("}")) return false;
        return true;
    }

    public static boolean isJsonArrayString(String jsonArrayStr) {
        if (StringUtils.isBlank(jsonArrayStr)) return false;
        jsonArrayStr = jsonArrayStr.trim();
        if (!jsonArrayStr.startsWith("[") || !jsonArrayStr.endsWith("]")) {
            return false;
        }
        String jsonStrings = jsonArrayStr.substring(1, jsonArrayStr.length() - 1).trim();
        if (StringUtils.isBlank(jsonStrings)) return false;
        return true;
    }
}
