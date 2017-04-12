package com.echoleaf.frame.net.async;

import com.echoleaf.frame.utils.MathUtils;
import com.echoleaf.frame.utils.StringUtils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by dydyt on 2016/8/30.
 */
public class AsyncRequestParams extends RequestParams {


    public Map<String, String> getBaseParams() {
        return getBaseParams(false);
    }

    public Map<String, String> getBaseParams(boolean sort) {
        Map<String, String> result = new HashMap<>();
        List<BasicNameValuePair> params = getParamsList();
        for (BasicNameValuePair kv : params) {
            if (StringUtils.notEmpty(kv.getValue())) {
                if (result.containsKey(kv.getName())) {
                    result.put(kv.getName(), result.get(kv.getName()) + "," + kv.getValue());
                } else {
                    result.put(kv.getName(), kv.getValue());
                }
            }
        }
        return sort ? MathUtils.sortMapByKey(result) : result;
    }
}
