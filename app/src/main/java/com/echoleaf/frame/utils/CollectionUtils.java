package com.echoleaf.frame.utils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * 数组集合工具类
 *
 * @author 何常平
 * @version 1.0
 */
public class CollectionUtils {

    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        if (obj instanceof Object[])
            return ((Object[]) obj).length == 0;
        return true;
    }

    public static boolean notEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 将集合以分割符连接成字符串
     *
     * @param delimiter 切割符，例如“，”“；”等
     * @param objs      数据值
     * @return
     */
    public static String linkToString(String delimiter, Collection<?> objs) {
        String[] ints = new String[objs.size()];
        return linkToString(delimiter, objs.toArray(ints));
    }

    /**
     * 将集合以分割符连接成字符串
     *
     * @param delimiter
     * @param objs
     * @return
     */
    @SafeVarargs
    public static final <M> String linkToString(String delimiter, M... objs) {
        if (objs == null || objs.length == 0)
            return "";
        String str = "";
        for (M obj : objs) {
            str += ((obj != null ? obj.toString() : "") + delimiter);
        }
        if (str.length() > delimiter.length())
            str = str.substring(0, str.length() - delimiter.length());
        return str;
    }

    /**
     * 判断某个值是否在当前集合中
     *
     * @param objs
     * @param element
     * @return
     */
    public static boolean inCollection(Object element, Collection<?> objs) {
        return indexOf(element, objs) >= 0;
    }

    /**
     * 判断某个值是否在当前集合中
     *
     * @param element
     * @param objs
     * @return
     */
    public static boolean inCollection(Object element, Object... objs) {
        return indexOf(element, objs) >= 0;
    }

    /**
     * 获取某个值在当前数组中的下标
     *
     * @param objs
     * @param element
     * @return
     */
    public static int indexOf(Object element, Collection<?> objs) {
        return indexOf(element, objs.toArray());
    }

    /**
     * 获取某个值在当前数组中的下标
     *
     * @param objs
     * @param element
     * @return
     */
    public static int indexOf(Object element, Object[] objs) {
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                Object candidate = objs[i];
                if (candidate == element || candidate.equals(element)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 从集合在移除指定项并返回变更后的数组
     *
     * @param index
     * @param objs
     * @return
     */
    public static Object[] removeIndex(int index, Object... objs) {
        Object[] newchars = new Object[objs.length - 1];
        for (int i = 0; i < newchars.length; i++) {
            if (index <= i) {
                newchars[i] = objs[i + 1];
            } else {
                newchars[i] = objs[i];
            }
        }
        return newchars;
    }

    /**
     * Append the given object to the given array, returning a new array
     * consisting of the input array contents plus the given object.
     * @param array the array to append to (can be {@code null})
     * @param obj the object to append
     * @return the new array (of the same component type; never {@code null})
     */
    public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        }
        else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }
}
