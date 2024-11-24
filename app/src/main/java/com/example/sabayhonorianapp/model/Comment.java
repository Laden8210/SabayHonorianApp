package com.example.sabayhonorianapp.model;

import com.google.firebase.Timestamp;

public class Comment {
    private String commenterName;
    private String commentText;
    private Timestamp timestamp;

    private String authorUID;

    public Comment() {
    }

    public Comment(String commenterName, String commentText, Timestamp timestamp, String authorUID) {
        this.commenterName = commenterName;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.authorUID = authorUID;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }
}
