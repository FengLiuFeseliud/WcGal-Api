package com.wcacg.wcgal.entity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CommentAddDto {
    private Long commentId = 0L;

    @NotBlank
    private String resourceId;

    @Length(min = 1, max = 5000)
    @NotBlank
    private String content;

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
}
