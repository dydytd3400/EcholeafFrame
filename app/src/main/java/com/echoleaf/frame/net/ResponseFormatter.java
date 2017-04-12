package com.echoleaf.frame.net;

import com.echoleaf.frame.utils.JsonUtils;
import com.echoleaf.frame.utils.StringUtils;

/**
 * Created by dydyt on 2017/2/21.
 */

public interface ResponseFormatter<T> {
    T formate(String response);

    class None implements ResponseFormatter<String> {

        @Override
        public String formate(String response) {
            return response;
        }

    }

    class FString implements ResponseFormatter<String> {

        @Override
        public String formate(String response) {
            if (response == null)
                response = "";
            return response;
        }
    }

    class FInteger implements ResponseFormatter<Integer> {

        @Override
        public Integer formate(String response) {
            if (StringUtils.isEmpty(response))
                return 0;
            return Integer.valueOf(response);
        }
    }

    class JsonResponseFormatter<T> implements ResponseFormatter<T> {

        private Class<T> cls;

        public JsonResponseFormatter(Class<T> cls) {
            this.cls = cls;
        }

        @Override
        public T formate(String response) {
            if (StringUtils.isEmpty(response))
                return null;
            return JsonUtils.read(response, cls);
        }

    }
}
