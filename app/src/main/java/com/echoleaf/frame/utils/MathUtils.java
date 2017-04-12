package com.echoleaf.frame.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基础工具类
 *
 * @author 何常平
 * @version 1.0
 */
public class MathUtils {

    /**
     * 判断是否基本类型
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return false;
        }
    }

    /**
     * 将map中的key按字母排序
     *
     * @param hashMap
     * @return
     */
    public static <T> Map<String, T> sortMapByKey(Map<String, T> hashMap) {
        Map<String, T> resultMap = Collections.synchronizedMap(new LinkedHashMap<String, T>());
        TreeMap<String, T> sorted_map = new TreeMap<>(new Comparator<String>() {

            public int compare(String arg0, String arg1) {
                String s1 = "";
                String s2 = "";
                try {
                    s1 = new String(arg0.getBytes("gbk"), "ISO-8859-1");
                    s2 = new String(arg1.getBytes("gbk"), "ISO-8859-1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s1.compareTo(s2);
            }
        });
        sorted_map.putAll(hashMap);
        for (String key : sorted_map.keySet()) {
            resultMap.put(key, sorted_map.get(key));
        }
        return resultMap;
    }

    /**
     * 从区间内取若干个随机整数
     *
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static int[] random(int min, int max, int n) {
        boolean ve = false;
        if (min == 0) {
            ve = true;
            min += 1;
            max += 1;
        }
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        if (ve) {
            for (int i = 0; i < result.length; i++) {
                result[i]--;
            }
        }
        return result;
    }


    /**
     * 判断某个小数需要乘以多少以10为权的系数才能变为整数（数学意义） <br>
     * 例： 传入参数1.23，返回值100. <br>
     * 因1.23需要乘以 100 才能变为整数 123
     *
     * @param d      小数基数
     * @param maxCoefficient 最大系数限制，如果计算系数大于限制则返回-1
     * @return
     */
    public static Integer findCoefficient(Double d, int maxCoefficient) {
        Integer coefficient = 1;
        for (; coefficient <= maxCoefficient; coefficient *= 10) {
            if (isInt(d * coefficient))
                return coefficient;
        }
        return -1;
    }

    /**
     * 判断一个双精度数是否为整数（数学意义）
     *
     * @param d
     * @return
     */
    public static boolean isInt(Double d) {
        Integer i = d.intValue();
        return i / d == d / i;
    }


}
