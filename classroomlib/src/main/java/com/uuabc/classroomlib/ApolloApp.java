package com.uuabc.classroomlib;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.db.RoomDbManager;
import com.uuabc.classroomlib.model.db.LottieRecord;
import com.uuabc.classroomlib.utils.UserAgentUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import uuabc.GetEmoticonsQuery;

public class ApolloApp extends RoomApplication {
    private final CompositeDisposable disposables = new CompositeDisposable();
    public ApolloClient apolloClient;
    public boolean getEmoticonsSuccess;
    private String emoticonsDownLoadUrl;
    private int emoticonsUpdateTime;

    public void initApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("authorization", "")
                            .addHeader("user_agent", UserAgentUtil.getUserAgent())
                            .build();
                    return chain.proceed(request);
                }).build();

        apolloClient = ApolloClient.builder()
                .serverUrl(SPUtils.getInstance().getString(RoomConstant.GRAPHQL_URL))
                .okHttpClient(okHttpClient)
                .build();
    }

    /**
     * 检测表情是否下载成功
     */
    public void checkEmoticonsDownLoad() {
        if (getEmoticonsSuccess) {
            RoomApplication.getInstance().downLoadTask(emoticonsDownLoadUrl, emoticonsUpdateTime);
        } else {
            getEmoticons();
        }
    }

    /**
     * 表情动画是否下载成功
     */
    public boolean isEmoticonsDownLoadSuccess() {
        return getEmoticonsSuccess && FileUtils.isFileExists(saveLottiePath + emoticonsUpdateTime + emojiZipFileName);
    }

    /**
     * 获取表情
     */
    public void getEmoticons() {
        if (!NetworkUtils.isConnected()) return;
        ApolloQueryCall<GetEmoticonsQuery.Data> entryQuery = apolloClient.query(new GetEmoticonsQuery());

        disposables.add(Rx2Apollo.from(entryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<GetEmoticonsQuery.Data>>() {
                    @Override
                    public void onNext(Response<GetEmoticonsQuery.Data> dataResponse) {
                        if (dataResponse.hasErrors()) return;
                        GetEmoticonsQuery.Data data = dataResponse.data();
                        if (data == null) return;
                        List<GetEmoticonsQuery.Emoticon> emoticons = data.emoticons();
                        if (emoticons == null || emoticons.size() == 0) return;
                        GetEmoticonsQuery.Emoticon emoticon = emoticons.get(0);
                        if (emoticon == null) return;
                        String groupName = emoticon.groupName();
                        List<GetEmoticonsQuery.Emoticon1> emoticon1s = emoticon.emoticon();
                        if (emoticon1s == null || emoticon1s.size() == 0) return;
                        int lastUpdateTime = SPUtils.getInstance().getInt(RoomConstant.SP_EMOTICON_UPDATED_TIME);
                        emoticonsUpdateTime = emoticon.updated_at();
                        List<LottieRecord> lottieList = RoomDbManager.getInstance().queryLottieRecord(emoticonsUpdateTime);
                        if (lottieList == null || lottieList.size() == 0 || lastUpdateTime != emoticonsUpdateTime) {
                            SPUtils.getInstance().put(RoomConstant.SP_EMOTICON_UPDATED_TIME, emoticonsUpdateTime);
                            List<LottieRecord> lottieRecords = new ArrayList<>();
                            LottieRecord lottieRecord;
                            GetEmoticonsQuery.Emoticon1 emoticon1;
                            for (int i = 0; i < emoticon1s.size(); i++) {
                                lottieRecord = new LottieRecord();
                                emoticon1 = emoticon1s.get(i);
                                lottieRecord.setReserve5(emoticonsUpdateTime);
                                lottieRecord.setCode(emoticon1.code());
                                lottieRecord.setName(emoticon1.name());
                                lottieRecord.setGroupName(groupName);
                                lottieRecord.setLottieJsonPath(saveLottiePath + groupName + "/" + emoticon1.name() + ".json");
                                lottieRecord.setSmallImgPath(saveLottiePath + groupName + "/" + emoticon1.name() + ".png");
                                lottieRecords.add(lottieRecord);
                            }

                            RoomDbManager.getInstance().deleteAllLottieRecords();
                            RoomDbManager.getInstance().saveLottieRecords(lottieRecords);
                        } else {
                            List<LottieRecord> lottieRecords = RoomDbManager.getInstance().queryAllLottieRecords();
                            for (LottieRecord lottieRecord : lottieRecords) {
                                if (lottieRecord.getReserve5() != emoticonsUpdateTime) {
                                    RoomDbManager.getInstance().deleteLottieRecord(lottieRecord);
                                }
                            }
                        }

                        getEmoticonsSuccess = true;
                        RoomApplication.getInstance().downLoadTask(emoticonsDownLoadUrl = emoticon.zipUrl(), emoticonsUpdateTime);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getEmoticons", (e == null || e.getMessage() == null) ? "unknow" : e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
