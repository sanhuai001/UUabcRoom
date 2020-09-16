package com.sdk.request;


import com.sdk.Constants;
import com.sdk.core.Request;

public class PostCachedLogRequest extends Request {
    //保存 log 的 project
    public String mProject;
    //保存 log 的 logstore
    public String mLogStoreName;
    //log 内容
    public String mJsonString;

    public final String logContentType = Constants.APPLICATION_JSON;

    public PostCachedLogRequest(String project, String logStoreName, String jsonString) {
        mProject = project;
        mLogStoreName = logStoreName;
        mJsonString = jsonString;
    }
}
