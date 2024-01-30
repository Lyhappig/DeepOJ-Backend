package com.yuhao.deepoj.model.dto.problem;

import lombok.Data;

/**
 * 判题用例（保存文件路径）
 */
@Data
public class JudgeCase {
    /**
     * 输入文件路径
     */
    private String inputPath;

    /**
     * 输出文件路径
     */
    private String outputPath;
}
