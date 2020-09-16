package com.sdk.core;

import com.sdk.ClientConfiguration;
import com.sdk.CommonHeaders;
import com.sdk.Constants;
import com.sdk.LogException;
import com.sdk.ResponseParsers;
import com.sdk.SLSLog;
import com.sdk.core.callback.CompletedCallback;
import com.sdk.core.http.HttpMethod;
import com.sdk.core.parser.ResponseParser;
import com.sdk.model.LogGroup;
import com.sdk.request.PostCachedLogRequest;
import com.sdk.request.PostLogRequest;
import com.sdk.result.PostCachedLogResult;
import com.sdk.result.PostLogResult;
import com.sdk.utils.HttpHeaders;
import com.sdk.utils.Utils;
import com.sdk.utils.VersionInfoUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Created by zhuoqin on 10/10/17.
 */
public class RequestOperation {

    private volatile URI endpoint;
    private OkHttpClient innerClient;
    private int maxRetryCount = Constants.DEFAULT_RETRY_COUNT;

    private static ExecutorService executorService = Executors.newFixedThreadPool(Constants.DEFAULT_BASE_THREAD_POOL_SIZE);

    public RequestOperation(final URI endpoint, ClientConfiguration conf) {
        this.endpoint = endpoint;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .retryOnConnectionFailure(false)
                .cache(null)
                .hostnameVerifier((hostname, session) -> {
                    return true;
//                        return HttpsURLConnection.getDefaultHostnameVerifier().verify(endpoint.getHost(), session);
                });

        if (conf != null) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(conf.getMaxConcurrentRequest());

            builder.connectTimeout(conf.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(conf.getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(conf.getSocketTimeout(), TimeUnit.MILLISECONDS)
                    .dispatcher(dispatcher);

            if (conf.getProxyHost() != null && conf.getProxyPort() != 0) {
                builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(conf.getProxyHost(), conf.getProxyPort())));
            }
            this.maxRetryCount = conf.getMaxErrorRetry();
        }

        this.innerClient = builder.build();
    }

    public OkHttpClient getInnerClient() {
        return innerClient;
    }

    private void buildUrl(PostLogRequest postLogRequest, RequestMessage requestMessage) throws
            LogException {
        if (postLogRequest == null || requestMessage == null) {
            LogException exception = new LogException("", "postLogRequest or requestMessage when buildUrl is not null", null, "");
            throw exception;
        }
        String logStoreName = postLogRequest.mLogStoreName;
        String project = postLogRequest.mProject;
        String scheme = endpoint.getScheme();
        String host = project + "." + endpoint.getHost();
        requestMessage.url = scheme + "://" + host + "/logstores/" + logStoreName + "/track?APIVersion=0.6.0";
        requestMessage.method = HttpMethod.POST;
    }

    private void buildHeaders(PostLogRequest postLogRequest, RequestMessage requestMessage) throws
            LogException {
        if (postLogRequest == null || requestMessage == null) {
            LogException exception = new LogException("", "postLogRequest or requestMessage when buildheaders is not null", null, "");
            throw exception;
        }
        LogGroup logGroup = postLogRequest.mLogGroup;
        String logStoreName = postLogRequest.mLogStoreName;
        String project = postLogRequest.mProject;
        String contentType = postLogRequest.logContentType;
        String host = project + "." + endpoint.getHost();

        Map<String, String> headers = requestMessage.headers;
        headers.put(CommonHeaders.COMMON_HEADER_APIVERSION, Constants.API_VERSION);
        headers.put(CommonHeaders.COMMON_HEADER_SIGNATURE_METHOD, Constants.SIGNATURE_METHOD);
        headers.put(CommonHeaders.COMMON_HEADER_COMPRESSTYPE, Constants.COMPRESSTYPE_DEFLATE);
        headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        headers.put(HttpHeaders.DATE, Utils.GetMGTTime());
        headers.put(HttpHeaders.HOST, host);


        try {
            byte[] httpPostBody = logGroup.LogGroupToJsonString().getBytes("UTF-8");
            byte[] httpPostBodyZipped = Utils.GzipFrom(httpPostBody);
            requestMessage.setUploadData(httpPostBodyZipped);
            headers.put(HttpHeaders.CONTENT_MD5, Utils.ParseToMd5U32(httpPostBodyZipped));
            headers.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(httpPostBodyZipped.length));
            headers.put(CommonHeaders.COMMON_HEADER_BODYRAWSIZE, String.valueOf(httpPostBody.length));
        } catch (Exception e) {
            LogException exception = new LogException("", "postLogRequest or requestMessage is not null", null, "");
            throw exception;
        }

        StringBuilder signStringBuf = new StringBuilder("POST" + "\n").
                append(headers.get(HttpHeaders.CONTENT_MD5) + "\n").
                append(headers.get(HttpHeaders.CONTENT_TYPE) + "\n").
                append(headers.get(HttpHeaders.DATE) + "\n");

        signStringBuf.append(CommonHeaders.COMMON_HEADER_APIVERSION + ":" + Constants.API_VERSION + "\n").
                append(CommonHeaders.COMMON_HEADER_BODYRAWSIZE + ":" + headers.get(CommonHeaders.COMMON_HEADER_BODYRAWSIZE) + "\n").
                append(CommonHeaders.COMMON_HEADER_COMPRESSTYPE + ":" + Constants.COMPRESSTYPE_DEFLATE + "\n").
                append(CommonHeaders.COMMON_HEADER_SIGNATURE_METHOD + ":" + Constants.SIGNATURE_METHOD + "\n").
                append("/logstores/" + logStoreName + "/track?APIVersion=0.6.0");
        String signString = signStringBuf.toString();

        SLSLog.logDebug("signed content: " + signString, false);
        headers.put(HttpHeaders.USER_AGENT, VersionInfoUtils.getUserAgent());
    }

    private void buildCachedUrl(PostCachedLogRequest request, RequestMessage requestMessage) throws
            LogException {
        if (request == null || requestMessage == null) {
            LogException exception = new LogException("", "postCachedLogRequest or requestMessage when buildUrl is not null", null, "");
            throw exception;
        }
        String logStoreName = request.mLogStoreName;
        String project = request.mProject;
        String scheme = endpoint.getScheme();
        String host = project + "." + endpoint.getHost();
        requestMessage.url = scheme + "://" + host + "/logstores/" + logStoreName + "/track?APIVersion=0.6.0";
        requestMessage.method = HttpMethod.POST;
    }

    private void buildCachedHeaders(PostCachedLogRequest request, RequestMessage requestMessage) throws LogException {
        if (request == null || requestMessage == null) {
            LogException exception = new LogException("", "postCachedLogRequest or requestMessage when buildheaders is not null", null, "");
            throw exception;
        }

        String logStoreName = request.mLogStoreName;
        String project = request.mProject;
        String contentType = request.logContentType;
        String host = project + "." + endpoint.getHost();

        Map<String, String> headers = requestMessage.headers;
        headers.put(CommonHeaders.COMMON_HEADER_APIVERSION, Constants.API_VERSION);
        headers.put(CommonHeaders.COMMON_HEADER_SIGNATURE_METHOD, Constants.SIGNATURE_METHOD);
        headers.put(CommonHeaders.COMMON_HEADER_COMPRESSTYPE, Constants.COMPRESSTYPE_DEFLATE);
        headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        headers.put(HttpHeaders.DATE, Utils.GetMGTTime());
        headers.put(HttpHeaders.HOST, host);

        try {
            byte[] httpPostBody = request.mJsonString.getBytes("UTF-8");
            byte[] httpPostBodyZipped = Utils.GzipFrom(httpPostBody);
            requestMessage.setUploadData(httpPostBodyZipped);
            headers.put(HttpHeaders.CONTENT_MD5, Utils.ParseToMd5U32(httpPostBodyZipped));
            headers.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(httpPostBodyZipped.length));
            headers.put(CommonHeaders.COMMON_HEADER_BODYRAWSIZE, String.valueOf(httpPostBody.length));
        } catch (Exception e) {
            LogException exception = new LogException("", "postLogRequest or requestMessage is not null", null, "");
            throw exception;
        }

        StringBuilder signStringBuf = new StringBuilder("POST" + "\n").
                append(headers.get(HttpHeaders.CONTENT_MD5) + "\n").
                append(headers.get(HttpHeaders.CONTENT_TYPE) + "\n").
                append(headers.get(HttpHeaders.DATE) + "\n");

        signStringBuf.append(CommonHeaders.COMMON_HEADER_APIVERSION + ":" + Constants.API_VERSION + "\n").
                append(CommonHeaders.COMMON_HEADER_BODYRAWSIZE + ":" + headers.get(CommonHeaders.COMMON_HEADER_BODYRAWSIZE) + "\n").
                append(CommonHeaders.COMMON_HEADER_COMPRESSTYPE + ":" + Constants.COMPRESSTYPE_DEFLATE + "\n").
                append(CommonHeaders.COMMON_HEADER_SIGNATURE_METHOD + ":" + Constants.SIGNATURE_METHOD + "\n").
                append("/logstores/" + logStoreName + "/shards/lb");
        String signString = signStringBuf.toString();


        String signature = "---initValue---";
        SLSLog.logDebug("signed content: " + signString + "   \n ---------   signature: " + signature, false);
        headers.put(CommonHeaders.AUTHORIZATION, signature);
        headers.put(HttpHeaders.USER_AGENT, VersionInfoUtils.getUserAgent());
    }

    public AsyncTask<PostLogResult> postLog(PostLogRequest postLogRequest, CompletedCallback<PostLogRequest, PostLogResult> completedCallback) throws
            LogException {

        RequestMessage requestMessage = new RequestMessage();

        try {
            buildUrl(postLogRequest, requestMessage);
            buildHeaders(postLogRequest, requestMessage);
        } catch (LogException e) {
            throw e;
        }

        ResponseParser<PostLogResult> parser = new ResponseParsers.PostLogResponseParser();

        ExecutionContext<PostLogRequest> executionContext = new ExecutionContext<PostLogRequest>(getInnerClient(), postLogRequest);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }

        Callable<PostLogResult> callable = new RequestTask<PostLogResult>(requestMessage, parser, executionContext, maxRetryCount);

        return AsyncTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }

    public AsyncTask<PostCachedLogResult> postCachedLog(PostCachedLogRequest request, CompletedCallback<PostCachedLogRequest, PostCachedLogResult> completedCallback) throws
            LogException {
        RequestMessage requestMessage = new RequestMessage();

        try {
            buildCachedUrl(request, requestMessage);
            buildCachedHeaders(request, requestMessage);
        } catch (LogException e) {
            throw e;
        }

        ResponseParser<PostCachedLogResult> parser = new ResponseParsers.PostCachedLogResponseParser();

        ExecutionContext<PostCachedLogRequest> executionContext = new ExecutionContext<PostCachedLogRequest>(getInnerClient(), request);
        if (completedCallback != null) {
            executionContext.setCompletedCallback(completedCallback);
        }

        Callable<PostCachedLogResult> callable = new RequestTask<PostCachedLogResult>(requestMessage, parser, executionContext, maxRetryCount);

        return AsyncTask.wrapRequestTask(executorService.submit(callable), executionContext);
    }
}
