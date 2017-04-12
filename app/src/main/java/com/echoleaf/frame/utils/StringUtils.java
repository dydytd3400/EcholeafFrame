package com.echoleaf.frame.utils;

import android.text.TextUtils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author 何常平
 * @version 0.1
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 字符替换
     *
     * @param src         原字符串
     * @param start       起始位置
     * @param end         结束位置
     * @param replaceChar 所需替换的字符
     * @return
     */
    public static String replace(String src, int start, int end, String replaceChar) {
        StringBuilder startStr = new StringBuilder(src.substring(0, start));
        StringBuilder endStr = new StringBuilder(src.substring(end, src.length()));
        return startStr.append(replaceChar).append(endStr).toString();
    }

    /**
     * 取指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        char[] ss = new char[length];
        int i = 0;
        while (i < length) {
            int f = (int) (Math.random() * 3);
            if (f == 0)
                ss[i] = (char) ('A' + Math.random() * 26);
            else if (f == 1)
                ss[i] = (char) ('a' + Math.random() * 26);
            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        String is = new String(ss);
        return is;
    }

    /**
     * 字符串连接
     *
     * @param strings
     * @return
     */
    public static String concat(String... strings) {
        return TextUtils.concat(strings).toString();
    }

    public static String concat(Collection<?> strings) {
        if (strings == null)
            return "";
        return concat(strings.toArray());
    }

    /**
     * 字符串连接
     *
     * @param strings
     * @return
     */
    public static String concat(Object... strings) {
        StringBuilder urlStr = new StringBuilder();
        for (Object str : strings) {
            urlStr.append(str == null ? "" : str.toString());
        }
        String result = urlStr.toString();
        urlStr.delete(0, urlStr.length());
        urlStr = null;
        return result;
    }

    public static String splice(String isolator, Collection<?> strings) {
        return splice(isolator, strings.toArray());
    }

    public static String splice(String isolator, String... strings) {
        String str = "";
        if (strings != null && strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                str += strings[i];
                if (i < strings.length - 1) {
                    str += isolator;
                }
            }
        }
        return str;
    }

    public static String splice(String isolator, Object... strings) {
        String str = "";
        if (strings != null && strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                str += (strings[i] == null ? "" : strings[i]);
                if (i < strings.length - 1) {
                    str += isolator;
                }
            }
        }
        return str;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        return obj.toString().equals("");
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Space space, Object obj) {
        if (space == null)
            return isEmpty(obj);
        else if (isEmpty(obj))
            return true;
        switch (space) {
            case EN:
                return obj.toString().trim().length() == 0;
            case ZH:
                return obj.toString().replace("　", "").length() == 0;
            default:
                return excise(obj.toString()).length() == 0;
        }
    }

    /**
     * 判断是否包含emoji表情字符
     *
     * @param str
     * @return
     */
    public static boolean containsEmoji(String str) {
        if (StringUtils.isEmpty(str))
            return false;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmoji(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 替换字符串中的emoji字符
     *
     * @param str
     * @param rc
     * @return
     */
    public static String replaceEmoji(String str, String rc) {
        if (StringUtils.isEmpty(str))
            return "";
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            sb.append(isEmoji(c) ? rc : c);
        }
        return sb.toString();
    }

    /**
     * 判断是否emoji字符
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmoji(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 判断字符串是否包含中文字符
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     *
     * @param s
     * @return
     */
    public static String excise(String s) {
        String result = "";
        if (notEmpty(s)) {
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     *
     * @param s
     * @return
     */
    public static String trim(String s) {
        String result = "";
        if (notEmpty(s)) {
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }
}
