package com.uuabc.classroomlib.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.model.FlowerCountModel;
import com.uuabc.classroomlib.model.RCommonResult;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SOverClassModel;
import com.uuabc.classroomlib.model.SRChartResult;
import com.uuabc.classroomlib.model.SRCommonResult;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SaveQuestionResult;
import com.uuabc.classroomlib.model.StateToolResult;
import com.uuabc.classroomlib.utils.ObjectUtil;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 使用Retrofit对网络请求进行配置
 * Created by wb on 2017/9/30.
 */
public class ApiRetrofit extends BaseRetrofitApi {
    private RequestRetrofit mApi;
    private static ApiRetrofit mInstance;
    private static String currentToken;

    private ApiRetrofit() {
        super();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ObjectUtil.getString(RoomApplication.getInstance().ONLINE_SS_COURSE_HOST))
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        //在构造方法中完成对Retrofit接口的初始化
        mApi = builder.build().create(RequestRetrofit.class);
    }

    public static ApiRetrofit getInstance() {
        if (!TextUtils.equals(currentToken, RoomApplication.getInstance().roomToken)) {
            currentToken = RoomApplication.getInstance().roomToken;
            mInstance = new ApiRetrofit();
        }

        if (mInstance == null) {
            mInstance = new ApiRetrofit();
        }
        return mInstance;
    }

    private RequestBody getRequestBody(String request) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request);
    }

    //直播课送给老师星星
    public Observable<RCommonResult<FlowerCountModel>> liveSendFlowers(String request) {
        return mApi.liveSendFlowers(getRequestBody(request));
    }

    //直播课保存答题结果
    public Observable<SaveQuestionResult> liveSaveQuestion(String request) {
        return mApi.liveSaveQuestion(getRequestBody(request));
    }

    //获取教室信息
    public Observable<SRCommonResult<SClassRoomResult>> getClassRoomInfo() {
        return mApi.getClassRoomInfo();
    }

    //读取聊天记录
    public Observable<SRCommonResult<SRChartResult>> readChart() {
        return mApi.readChart();
    }

    //获取学生的开关状态
    public Observable<SRCommonResult<StateToolResult>> getStudentSwitchState() {
        return mApi.getStudentSwitchState();
    }

    //离开教室
    public Observable<SRCommonResult<SOverClassModel>> leaveClassRoom() {
        return mApi.leaveClassRoom();
    }

    //获取用户信息
    public Observable<SRCommonResult<SUserModel>> getUserInfo() {
        return mApi.getUserInfo();
    }

    //数据录制，保存上课过程中各项数据
    public Observable<SRCommonResult> recordCourseInfo(String request) {
        return mApi.recordCourseInfo(getRequestBody(request));
    }
}
