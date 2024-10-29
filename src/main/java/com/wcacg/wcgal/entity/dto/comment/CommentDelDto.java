package com.wcacg.wcgal.entity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDelDto {
    @NotNull
    private Long commentId = 0L;
    @NotBlank
    private String resourceId;
}
