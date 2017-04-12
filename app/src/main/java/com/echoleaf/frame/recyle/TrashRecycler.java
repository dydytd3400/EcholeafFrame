package com.echoleaf.frame.recyle;

/**
 * 垃圾回收管理接口，通过addTrash将TrashCollector所采集的需要回收的对象进行集中管理，并在恰当的时候进行统一回收
 */
public interface TrashRecycler {
    void addTrash(Object trash, TrashMonitor.On on, int sort);
}
