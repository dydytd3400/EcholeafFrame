package com.echoleaf.frame.recyle;

import com.echoleaf.frame.utils.CollectionUtils;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Trash采集器，负责将TrashMonitor所检测的Trash对象收集整理并进行统一管理和回收
 */
public class TrashCollector {

    /**
     * 对目标对象成员中所有监视对象进行回收
     *
     * @param target
     */
    public static void recycle(Object target) {
        recycle(target, TrashMonitor.On.ANYTIME);
    }

    /**
     * 对目标对象成员中符合回收条件TrashMonitor.On的监视对象进行回收
     *
     * @param target
     * @param on
     */
    public static void recycle(Object target, TrashMonitor.On on) {
        if (target == null)
            return;
        List tarshes = new ArrayList<>();
        List<Field> all = new ArrayList<>(Arrays.asList(target.getClass().getFields()));
        all.addAll(Arrays.asList(target.getClass().getDeclaredFields()));
        for (Field field : all) {
            field.setAccessible(true);
            TrashMonitor trashMonitor = field.getAnnotation(TrashMonitor.class);
            if (trashMonitor != null && (on == null || on == TrashMonitor.On.ANYTIME || on == trashMonitor.on())) {
                Object trash = getFieldValue(target, field);
                if (!CollectionUtils.inCollection(trash, tarshes))
                    if (trashMonitor.sort() < 0)
                        tarshes.add(trash);
                    else
                        tarshes.add(trashMonitor.sort(), trash);
            }
        }

        for (Object o : tarshes) {
            recycleUnknown(o);
        }
        tarshes.clear();
    }

    private static void recycleUnknown(Object o) {
        if (o != null)
            if (o instanceof Trash) {
                ((Trash) o).recycle();
            } else if (o instanceof Map) {
                recycle((Map) o);
            } else if (o instanceof Collection) {
                recycle((Collection) o);
            } else if (o instanceof Reference) {
                recycle((Reference) o);
            }
    }

    private static void recycle(Collection collection) {
        if (collection == null)
            return;
        for (Object o : collection) {
            recycleUnknown(o);
        }
        collection.clear();
    }

    private static void recycle(Map map) {
        if (map == null)
            return;
        for (Object o : map.values()) {
            recycleUnknown(o);
        }
        map.clear();
    }

    private static void recycle(Reference reference) {
        if (reference == null)
            return;
        if (reference instanceof Trash) {
            ((Trash) reference).recycle();
        }
        reference.clear();
    }

    /**
     * 获取当前对象对应字段的属性（对象）
     * 声明，需要注意在NoSuchFieldException异常捕捉中捕获自己需要的属性字段进行拦截，告诉当查询这些属性名的时候，指定是查找的哪些对象，如果不告诉它，它是不知道的
     *
     * @param parent 当前对象
     * @return Object 当前对象指定属性值
     */
    private static Object getFieldValue(Object parent, Field field) {
        Object fieldValue = null;
        try {
            field.setAccessible(true);
            fieldValue = field.get(parent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

}