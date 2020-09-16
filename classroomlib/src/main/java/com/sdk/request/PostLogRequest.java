package com.sdk.request;


import com.sdk.Constants;
import com.sdk.core.Request;
import com.sdk.model.LogGroup;

/**
 * Created by wangzheng on 2017/10/11.
 */

public class PostLogRequest extends Request {

    //保存 log 的 project
    public String mProject;
    //保存 log 的 logstore
    public String mLogStoreName;
    //log 内容
    public LogGroup mLogGroup;
    //用户上传的数据类型。目前默认是json，如果用其他的要修改
    public String logContentType = Constants.APPLICATION_JSON;

    public PostLogRequest(String project, String logStoreName, LogGroup logGroup) {
        mProject = project;
        mLogStoreName = logStoreName;
        mLogGroup = logGroup;
    }
}
