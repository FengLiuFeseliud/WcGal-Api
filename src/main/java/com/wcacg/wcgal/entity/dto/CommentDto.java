package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDto {
    @NotBlank
    private String comment;

    public @NotBlank String getComment() {
        return comment;
    }

    public void setComment(@NotBlank String comment) {
        this.comment = comment;
    }
}
