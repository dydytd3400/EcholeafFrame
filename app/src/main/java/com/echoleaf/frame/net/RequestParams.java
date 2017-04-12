package com.echoleaf.frame.net;


import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.utils.CodecUtils;
import com.echoleaf.frame.utils.MathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by dydyt on 2017/2/21.
 */

public class RequestParams implements Trash {

    private HttpMethod method;
    private String action;
    private Map<String, Object> params;
    private Map<String, File[]> fileParams;
    private List<Header> headers;
    private String contentType;
    private int timeout;

    public RequestParams() {
        this(null, HttpMethod.GET);
    }

    public RequestParams(String action) {
        this(action, HttpMethod.GET);
    }

    public RequestParams(HttpMethod method) {
        this(null, method);
    }

    public RequestParams(String action, HttpMethod method) {
        this.action = action;
        this.method = method;
    }

    public void addHeader(String key, String value) {
        addHeader(new Header(key, value));
    }

    public void addHeader(Header header) {
        if (headers == null)
            headers = new ArrayList<>();
        headers.add(header);
    }

    public void put(String key, Object value) {
        if (params == null)
            params = new HashMap<>();
        params.put(key, value);
    }

    public void put(String key, File file) {
        put(key, new File[]{file});
    }

    public void put(String key, File[] files) {
        if (files == null || files.length == 0)
            return;
        if (fileParams == null)
            fileParams = new HashMap<>();
        fileParams.put(key, files);
    }

    public void putArray(String key, Object o) {
        if (params == null)
            params = new HashMap<>();
        if (!params.containsKey(key))
            params.put(key, new ArrayList<Objects>());
        List<Object> list = (List<Object>) params.get(key);
        if (list == null) {
            list = new ArrayList<>();
            params.put(key, list);
        }
        list.add(o);
    }

    public void putArray(String key, List list) {
        if (list == null || list.size() == 0) {
            putArray(key, (Object[]) null);
        } else {
            Object[] objects = list.toArray();
            putArray(key, objects);
        }
    }

    public void putArray(String key, Object[] arry) {
        if (params == null)
            params = new HashMap<>();
        if (!params.containsKey(key))
            params.put(key, new ArrayList<Objects>());
        List<Object> list = (List<Object>) params.get(key);
        if (list == null) {
            list = new ArrayList<>();
            params.put(key, list);
        }
        if (arry != null && arry.length > 0) {
            for (Object o : arry) {
                list.add(o);
            }
        } else {
            list.add(null);
        }

    }

    public void clearParam() {
        if (params != null)
            params.clear();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, File[]> getFileParams() {
        return fileParams;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String generateUniqueCode() {
        StringBuilder sb = new StringBuilder(getAction());
        sb.append(getMethod().name());
        if (params != null || fileParams != null) {
            Map<String, Object> serviceParams = new HashMap<>();
            if (params != null)
                serviceParams.putAll(params);
            if (fileParams != null) {
                serviceParams.putAll(fileParams);
            }
            Map<String, Object> sortMap = MathUtils.sortMapByKey(serviceParams);
            for (String key : sortMap.keySet()) {
                Object value = sortMap.get(key);
                if (value == null) {
                    sb.append(key).append("");
                } else if (value instanceof List) {
                    List list = (List) value;
                    Collections.sort(list);
                    for (Object o : list) {
                        sb.append(key).append(o == null ? "" : o.toString());
                    }

                } else if (value instanceof File[]) {
                    File[] files = (File[]) value;
                    Arrays.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            String str1 = o1 == null ? "" : o1.getAbsolutePath();
                            String str2 = o2 == null ? "" : o1.getAbsolutePath();
                            return str1.compareTo(str2);
                        }
                    });
                    for (File f : files) {
                        sb.append(key).append(f == null ? "" : f.getAbsolutePath());
                    }
                } else {
                    sb.append(key).append(value.toString());
                }

            }

        }
        return CodecUtils.MD5.encode(sb.toString());
    }

    @Override
    public void recycle() {
        method = null;
        action = null;
        if (params != null) {
            params.clear();
            params = null;
        }
        if (headers != null) {
            headers.clear();
        }
        contentType = null;
    }
}
