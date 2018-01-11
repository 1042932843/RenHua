package com.example.administrator.renhua.entity;

/**
 * Created by K on 2016/6/30.
 */
public class SeekDetailTwo {
    //问题记录和查询列表
    public String askTitle;
    public String askContent;
    public String replyContent;

    public String status;
    public String questionType;

    //查询、生产服务通用
    public long createDate;
    public String id;

    //缴费记录
    public String transDate;
    public String type;
    public String phoneNumber;
    public float transAmt;

    //生产服务
    public String newsTitle;

    public String getAskTitle() {
        return askTitle;
    }

    public void setAskTitle(String askTitle) {
        this.askTitle = askTitle;
    }

    public String getAskContent() {
        return askContent;
    }

    public void setAskContent(String askContent) {
        this.askContent = askContent;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getTransAmt() {
        return transAmt;
    }

    public void setTransAmt(float transAmt) {
        this.transAmt = transAmt;
    }
}
