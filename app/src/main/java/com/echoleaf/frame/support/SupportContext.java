package com.echoleaf.frame.support;

import com.echoleaf.frame.recyle.TrashMonitor;
import com.echoleaf.frame.support.controller.TouchEventController;

/**
 * Created by 何常平 on 2017/2/20.
 */
public interface SupportContext {

    void addTouchEventController(TouchEventController... controller);

    void addTrash(Object trash, TrashMonitor.On on, int sort);
}
