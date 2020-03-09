package com.uuabc.classroomlib.model;

import android.os.Parcel;

import java.util.List;

public class RChartResut extends RCommonResult<List<RChartResut>> {
    private String id;
    private String class_appoint_course_id;
    private String send_id;
    private String send_type;
    private String receive_type;
    private String content;
    private int create_time;
    private String name;
    private String photo;

    protected RChartResut(Parcel in) {
        super(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_appoint_course_id() {
        return class_appoint_course_id;
    }

    public void setClass_appoint_course_id(String class_appoint_course_id) {
        this.class_appoint_course_id = class_appoint_course_id;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getReceive_type() {
        return receive_type;
    }

    public void setReceive_type(String receive_type) {
        this.receive_type = receive_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
