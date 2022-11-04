package com.github.nikyotensai.config.rules;

import com.github.nikyotensai.config.ConstVal;
import org.apache.commons.lang.StringUtils;

/**
 * 从数据库表到文件的命名策略
 */
public enum NamingStrategy {
    /**
     * 不做任何改变，原样输出
     */
    nochange,
    /**
     * 下划线转驼峰命名
     */
    underline_to_camel;

    public static String underlineToCamel(String name) {
        // 快速检查
        if (StringUtils.isBlank(name)) {
            // 没必要转换
            return "";
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String camels[] = name.toLowerCase().split(ConstVal.UNDERLINE);
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (StringUtils.isBlank(camel)) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        }
        return result.toString();
    }

    /**
     * 去掉第一个下划线之前内容，后面原样输出
     *
     * @param name
     * @return
     */
    public static String removeUnderline(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        int idx = name.indexOf(ConstVal.UNDERLINE);
        if (idx == -1) {
            return name;
        }
        return name.substring(idx + 1);
    }

    /**
     * 去掉第一个下划线之前内容，后面转驼峰
     *
     * @param name
     * @return
     */
    public static String removeUnderlineAndCamel(String name) {
        return underlineToCamel(removeUnderline(name));
    }

    /**
     * 去掉指定的前缀
     *
     * @param name
     * @param prefix
     * @return
     */
    public static String removePrefix(String name, String prefix) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        int idx = -1;
        if (prefix != null && !"".equals(prefix.trim())) {
            idx = name.indexOf(ConstVal.UNDERLINE);
            if (name.toLowerCase().matches("^" + prefix.toLowerCase() + ".*")) { // 判断是否有匹配的前缀，然后截取前缀
                idx = prefix.length() - 1;
            }
        }
        if (idx == -1) {
            return name;
        }
        return name.substring(idx + 1);
    }

    /**
     * 去掉下划线前缀且将后半部分转成驼峰格式
     *
     * @param name
     * @param tablePrefix
     * @return
     */
    public static String removePrefixAndCamel(String name, String tablePrefix) {
        return underlineToCamel(removePrefix(name, tablePrefix));
    }

    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name) {
        if (StringUtils.isNotBlank(name)) {
            char[] array = name.toCharArray();
            array[0] -= 32;
            return String.valueOf(array);
        }
        return "";
    }

}
