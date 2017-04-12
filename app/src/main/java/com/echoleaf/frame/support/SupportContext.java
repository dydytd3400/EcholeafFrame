package com.echoleaf.frame.support;

import com.echoleaf.frame.recyle.TrashRecycler;
import com.echoleaf.frame.support.controller.TouchEventController;

/**
 * Created by 何常平 on 2017/2/20.
 */
public interface SupportContext extends TrashRecycler {

    void addTouchEventController(TouchEventController... controller);

}
