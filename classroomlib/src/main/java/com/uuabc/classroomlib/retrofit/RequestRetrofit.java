package com.uuabc.classroomlib.retrofit;

import com.uuabc.classroomlib.model.FlowerCountModel;
import com.uuabc.classroomlib.model.RCommonResult;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SOverClassModel;
import com.uuabc.classroomlib.model.SRChartResult;
import com.uuabc.classroomlib.model.SRCommonResult;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SaveQuestionResult;
import com.uuabc.classroomlib.model.StateToolResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求接口
 * Created by wb on 2017/11/3.
 */
public interface RequestRetrofit {
    //直播课送给老师星星
    @POST("liveSendFlowers")
    Observable<RCommonResult<FlowerCountModel>> liveSendFlowers(@Body RequestBody body);

    //直播课保存答题结果
    @POST("liveSaveQuestion")
    Observable<SaveQuestionResult> liveSaveQuestion(@Body RequestBody body);

    //获取教室信息
    @POST("classroom/info")
    Observable<SRCommonResult<SClassRoomResult>> getClassRoomInfo();

    //获取教室信息
    @POST("classroom/chat_record")
    Observable<SRCommonResult<SRChartResult>> readChart();

    //获取学生的开关状态
    @POST("record/state/load")
    Observable<SRCommonResult<StateToolResult>> getStudentSwitchState();

    //离开教室
    @POST("classroom/leave")
    Observable<SRCommonResult<SOverClassModel>> leaveClassRoom();

    //获取用户信息
    @POST("classroom/user")
    Observable<SRCommonResult<SUserModel>> getUserInfo();

    //数据录制，保存上课过程中各项数据
    @POST("record/save")
    Observable<SRCommonResult> recordCourseInfo(@Body RequestBody body);
}

