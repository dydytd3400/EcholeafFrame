package com.echoleaf.frame.recyle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Trash采集器，负责将TrashMonitor所检测的Trash对象收集整理并装载至TrashRecycler，交由TrashRecycler对Trash对象进行统一管理和回收
 */
public class TrashCollector {
    public static void monitor(TrashRecycler recycler) {
        if (recycler == null)
            return;
        Field[] fields = recycler.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof TrashMonitor) {
                        TrashMonitor trash = (TrashMonitor) annotation;
                        recycler.addTrash(getFieldValue(recycler, field), trash.on(), trash.sort());
                    }
                }
            }
        }
    }


    /**
     * 获取当前对象对应字段的属性（对象）
     * 声明，需要注意在NoSuchFieldException异常捕捉中捕获自己需要的属性字段进行拦截，告诉当查询这些属性名的时候，指定是查找的哪些对象，如果不告诉它，它是不知道的
     *
     * @param parent 当前对象
     * @return Object 当前对象指定属性值
     */
    public static Object getFieldValue(Object parent, Field field) {
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