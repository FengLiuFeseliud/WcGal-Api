package com.wcacg.wcgal.entity.dto.comment;

import com.wcacg.wcgal.entity.dto.user.UserInfoDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDto {
    private Long commentId;
    private String resourceId;
    private String content;
    private UserInfoDto commentAuthor;
    private Long likes;
    private Date createTime;
    private Date updateTime;
    private int subCommentCount;
    private List<CommentDto> subComment = new ArrayList<>();

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfoDto getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(UserInfoDto commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<CommentDto> getSubComment() {
        return subComment;
    }

    public int getSubCommentCount() {
        return subCommentCount;
    }

    public void setSubCommentCount(int subCommentCount) {
        this.subCommentCount = subCommentCount;
    }

    public void setSubComment(List<CommentDto> subComment) {
        this.subComment = subComment;
    }

    public void addSubComments(CommentDto commentDto) {
        this.subComment.add(commentDto);
    }
}
