package com.uuabc.classroomlib.retrofit;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.uuabc.classroomlib.RoomApplication;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 配置Retrofit（配置网络缓存cache、配置持久cookie免登录）
 * Created by wb on 2017/9/30.
 */
class BaseRetrofitApi {

    private final OkHttpClient mClient;

    OkHttpClient getClient() {
        return mClient;
    }

    BaseRetrofitApi() {
        //cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(RoomApplication.getInstance()));

        Interceptor REWRITE_HEADER_CONTROL_INTERCEPTOR = chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("token", TextUtils.isEmpty(RoomApplication.getInstance().roomToken) ? "" : RoomApplication.getInstance().roomToken)
                    .build();
            return chain.proceed(request);
        };

        mClient = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR)
                .addInterceptor(new LoggingInterceptor())
                .cookieJar(cookieJar)
                .build();

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = (hostname, session) -> true;

        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(mClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(mClient, Objects.requireNonNull(sc).getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoggingInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            LogUtils.i("NetRequest", "发送请求:" + request.url() + "\n 接收响应:" + responseBody.string());
            return response;
        }
    }
}
