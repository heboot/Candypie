package com.example.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * Created by Heboot on 16/6/26.
 */
public class HttpRequest {

    private Map<String, Object> params = new HashMap<>();
    private Map<String, String> header = new HashMap<>();

    private String httpUrl;

    private String tag;

    private int method;

    private String debugTag;

    private boolean shouldCacheFlag;

    private boolean isGuide;

    private FormBody formBody;


    public HttpRequest(String url) {
        this(url, Method.POST, false);
    }

    public HttpRequest(String url, int method, boolean isGuide) {
        this.httpUrl = url;
        this.tag = httpUrl;
        this.method = method;
        this.debugTag = "NoDebugTag";
        this.shouldCacheFlag = false;
        this.isGuide = isGuide;
    }

    public HttpRequest(String url, int method, boolean isGuide, FormBody formBody) {
        this.httpUrl = url;
        this.tag = httpUrl;
        this.method = method;
        this.debugTag = "NoDebugTag";
        this.shouldCacheFlag = false;
        this.isGuide = isGuide;
        this.formBody = formBody;
    }

    public String matchMethod(int method) {
        String methodString = "GET";
        switch (method) {
            case 0:
                methodString = "GET";
                break;
            case 1:
                methodString = "POST";
                break;
            case 2:
                methodString = "PUT";
                break;
            case 3:
                methodString = "DELETE";
                break;
            case 4:
                methodString = "HEAD";
                break;
            case 5:
                methodString = "OPTIONS";
                break;
            case 6:
                methodString = "TRACE";
                break;
            case 7:
                methodString = "PATCH";
                break;
        }
        return methodString;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void addHeader(String key, String value) {
        if (value != null) {
            header.put(key, value);
        }
    }

    public interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public void addParams(String key, String value) {
        if (value != null) {
            params.put(key, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(debugTag);
        stringBuilder.append(" : ");
        stringBuilder.append("HttpRequest:[ ");
        stringBuilder.append("httpUrl:" + httpUrl);
        stringBuilder.append(" , ");
        stringBuilder.append("method:" + method);
        stringBuilder.append(" , ");
        stringBuilder.append("header:" + header.toString());
        stringBuilder.append(" , ");
        stringBuilder.append("params:" + params.toString());
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    public boolean isGuide() {
        return isGuide;
    }

    public void setGuide(boolean guide) {
        isGuide = guide;
    }

    public FormBody getFormBody() {
        return formBody;
    }

    public void setFormBody(FormBody formBody) {
        this.formBody = formBody;
    }
}
