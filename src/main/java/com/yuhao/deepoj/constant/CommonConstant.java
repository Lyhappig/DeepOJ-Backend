package com.yuhao.deepoj.constant;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 1 KB = 1024 B
     */
    Long KB = 1024L;

    /**
     * 1 MB = 1024 KB = 2^{20} MB
     */
    Long MB = KB * 1024L;

    /**
     * 1 GB = 1024 MB = 2^{20} KB = 2^{30} KB
     */
    Long GB = MB * 1024L;
}
