package com.uuabc.classroomlib.model;

import java.io.Serializable;

/**
 * Message
 * Created by wb on 2018/3/21.
 */

public class Message implements Serializable {
    private String messageId;
    private String authorName;
    private String title;
    private String content;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
