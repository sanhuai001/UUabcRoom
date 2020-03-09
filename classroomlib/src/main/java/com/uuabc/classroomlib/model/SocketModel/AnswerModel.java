package com.uuabc.classroomlib.model.SocketModel;

public class AnswerModel {

    /**
     * id : 469
     * topic_type : 1
     * topic_type_id : 108
     * topic_content_type : 1
     * topic_content :  Letter _____.
     * topic_img : https://uutest2.uuabc.com/topic_dry_1488531949026
     * topic_file_type : audio/mp3
     * answer : C
     * a : https://uutest2.uuabc.com/topic_options_1489486432556
     * b : https://uutest2.uuabc.com/topic_options_1489486437057
     * c : https://uutest2.uuabc.com/topic_options_1489486442704
     * d : null
     * e : null
     * courseware_status : 0
     * is_record : false
     * index : 1
     */

    private String id;
    private String topic_type;
    private String topic_type_id;
    private String topic_content_type;
    private String topic_content;
    private String topic_img;
    private String topic_file_type;
    private String answer;
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String courseware_status;
    private boolean is_record;
    private int index;
    private boolean tasked;
    private boolean finished;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_type_id() {
        return topic_type_id;
    }

    public void setTopic_type_id(String topic_type_id) {
        this.topic_type_id = topic_type_id;
    }

    public String getTopic_content_type() {
        return topic_content_type;
    }

    public void setTopic_content_type(String topic_content_type) {
        this.topic_content_type = topic_content_type;
    }

    public String getTopic_content() {
        return topic_content;
    }

    public void setTopic_content(String topic_content) {
        this.topic_content = topic_content;
    }

    public String getTopic_img() {
        return topic_img;
    }

    public void setTopic_img(String topic_img) {
        this.topic_img = topic_img;
    }

    public String getTopic_file_type() {
        return topic_file_type;
    }

    public void setTopic_file_type(String topic_file_type) {
        this.topic_file_type = topic_file_type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getCourseware_status() {
        return courseware_status;
    }

    public void setCourseware_status(String courseware_status) {
        this.courseware_status = courseware_status;
    }

    public boolean isIs_record() {
        return is_record;
    }

    public void setIs_record(boolean is_record) {
        this.is_record = is_record;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isTasked() {
        return tasked;
    }

    public void setTasked(boolean tasked) {
        this.tasked = tasked;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
