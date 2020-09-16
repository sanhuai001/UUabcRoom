package com.sdk;

import android.content.Context;
import android.util.Log;

import com.sdk.core.AsyncTask;
import com.sdk.core.RequestOperation;
import com.sdk.core.callback.CompletedCallback;
import com.sdk.request.PostCachedLogRequest;
import com.sdk.request.PostLogRequest;
import com.sdk.result.PostCachedLogResult;
import com.sdk.result.PostLogResult;
import com.uuabc.classroomlib.model.db.LogEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.WeakHashMap;

public class LOGClient {
    private String mEndPoint;
    private String mHttpType;
    private URI endpointURI;
    private RequestOperation requestOperation;
    private CacheManager cacheManager;
    private Boolean cachable = false;
    private ClientConfiguration.NetworkPolicy policy;
    private Context context;
    private WeakHashMap<PostLogRequest, CompletedCallback<PostLogRequest, PostLogResult>> mCompletedCallbacks = new WeakHashMap<PostLogRequest, CompletedCallback<PostLogRequest, PostLogResult>>();
    private CompletedCallback<PostLogRequest, PostLogResult> callbackImp;

    public LOGClient(Context context, String endpoint, ClientConfiguration conf) {
        try {
            mHttpType = "http://";
            if (!endpoint.trim().equals("")) {
                mEndPoint = endpoint;
            } else {
                throw new NullPointerException("endpoint is null");
            }

            if (mEndPoint.startsWith("http://")) {
                mEndPoint = mEndPoint.substring(7);
            } else if (mEndPoint.startsWith("https://")) {
                mEndPoint = mEndPoint.substring(8);
                mHttpType = "https://";
            }

            while (mEndPoint.endsWith("/")) {
                mEndPoint = mEndPoint.substring(0, mEndPoint.length() - 1);
            }

            this.endpointURI = new URI(mHttpType + mEndPoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Endpoint must be a string like 'http://cn-****.log.aliyuncs.com'," +
                    "or your cname like 'http://image.cnamedomain.com'!");
        }

        if (context == null) {
            throw new IllegalArgumentException("context can't be null.");
        }

        if (conf != null) {
            this.cachable = conf.getCachable();
            this.policy = conf.getConnectType();
        }

        requestOperation = new RequestOperation(endpointURI, (conf == null ? ClientConfiguration.getDefaultConf() : conf));
        this.context = context;

        if (this.cachable) {
            SLSDatabaseManager.getInstance().setupDB(context);
            cacheManager = new CacheManager(this);
            cacheManager.setupTimer();
        }

        callbackImp = new CompletedCallback<PostLogRequest, PostLogResult>() {
            @Override
            public void onSuccess(PostLogRequest request, PostLogResult result) {
                CompletedCallback<PostLogRequest, PostLogResult> callback = mCompletedCallbacks.get(request);
                if (callback != null) {
                    try {
                        callback.onSuccess(request, result);
                    } catch (Exception ignore) {
                        // The callback throws the exception, ignore it
                    }
                }
            }

            @Override
            public void onFailure(PostLogRequest request, LogException exception) {
                if (cachable) {
                    LogEntity item = new LogEntity();
                    item.setProject(request.mProject);
                    item.setStore(request.mLogStoreName);
                    item.setEndPoint(mEndPoint);
                    item.setJsonString(request.mLogGroup.LogGroupToJsonString());
                    item.setTimestamp(System.currentTimeMillis());
                    SLSDatabaseManager.getInstance().insertRecordIntoDB(item);
                }

                CompletedCallback<PostLogRequest, PostLogResult> callback = mCompletedCallbacks.get(request);
                if (callback != null) {
                    try {
                        callback.onFailure(request, exception);
                    } catch (Exception ignore) {
                        // The callback throws the exception, ignore it
                    }
                }
            }
        };
    }

    public AsyncTask<PostLogResult> asyncPostLog(PostLogRequest request, CompletedCallback<PostLogRequest, PostLogResult> completedCallback) throws LogException {
        mCompletedCallbacks.put(request, completedCallback);
        return requestOperation.postLog(request, callbackImp);
    }

    public AsyncTask<PostCachedLogResult> asyncPostCachedLog(PostCachedLogRequest request, final CompletedCallback<PostCachedLogRequest, PostCachedLogResult> completedCallback)
            throws LogException {
        return requestOperation.postCachedLog(request, completedCallback);
    }

    public String GetEndPoint() {
        return mEndPoint;
    }

    public ClientConfiguration.NetworkPolicy getPolicy() {
        return policy;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d("SLS SDK", "LOGClient finalize");
    }
    //=================================================
}
