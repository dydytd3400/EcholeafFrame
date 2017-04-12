package com.echoleaf.frame.recyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * TrashMonitor注解对象
 * 负责对Trash或非Trash等可被TrashRecycler管理和回收的任意对象进行监测，并提交至TrashCollector集中收集
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrashMonitor {
    int DISORDERED = -1;

    enum On {
        FINISH, DESTORY
    }

    On on() default On.DESTORY;

    int sort() default (int) DISORDERED;
}
