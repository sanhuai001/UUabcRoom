package com.sdk.core;


import com.sdk.core.callback.CompletedCallback;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;

/**
 * Created by zhouzhuo on 11/22/15.
 */
public class ExecutionContext<T extends Request> {

    private T request;
    private OkHttpClient client;
    private CancellationHandler cancellationHandler = new CancellationHandler();

//    private CompletedCallback completedCallback;

    private WeakReference<CompletedCallback> completedCallback;

    public ExecutionContext(OkHttpClient client, T request) {
        setClient(client);
        setRequest(request);
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public CancellationHandler getCancellationHandler() {
        return cancellationHandler;
    }

    public CompletedCallback getCompletedCallback() {
        return completedCallback.get();
    }

    public void setCompletedCallback(CompletedCallback completedCallback) {
        this.completedCallback = new WeakReference<CompletedCallback>(completedCallback);
//        this.completedCallback = completedCallback;
    }
}
