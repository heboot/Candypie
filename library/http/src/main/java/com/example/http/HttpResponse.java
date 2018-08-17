package com.example.http;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Heboot on 16/6/27.
 */
public class HttpResponse<T> {

    public void onResponse(T model) {

    }

    public void onResponse(String result) {

    }

    public void onFailure(Call call, IOException e) {

    }

}
