package com.example.myjournal;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String content;
    String tags;
    Timestamp timestamp;

    public Note() {
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

    public Timestamp getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {

        this.timestamp = timestamp;
    }

    public String getTags() {

        return tags;
    }

    public void setTags(String tags) {

        this.tags = tags;
    }


}
