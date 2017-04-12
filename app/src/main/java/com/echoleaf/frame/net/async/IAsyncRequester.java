package com.echoleaf.frame.net.async;

import android.content.Context;

import com.echoleaf.frame.net.HttpMethod;
import com.echoleaf.frame.recyle.Trash;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dydyt on 2017/2/21.
 */

public interface IAsyncRequester extends Trash {

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, Header[] headers, String contentType, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, Header[] headers, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, String contentType, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, Header[] headers, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, Header[] headers, String contentType, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, String contentType, ResponseHandlerInterface responseHandler);


    RequestHandle request(HttpMethod method, Context context, String action, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, Header[] headers, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, String contentType, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, Header[] headers, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, Header[] headers, String contentType, int timeout, ResponseHandlerInterface responseHandler);

    RequestHandle request(HttpMethod method, Context context, String action, AsyncRequestParams params, String contentType, int timeout, ResponseHandlerInterface responseHandler);

    Header getContentType();

    String createUploadContentType();

}
