package com.wcacg.wcgal.entity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CommentUpdateDto {
    @NotNull
    private Long commentId = 0L;

    @Length(min = 1, max = 5000)
    @NotBlank
    private String content;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
