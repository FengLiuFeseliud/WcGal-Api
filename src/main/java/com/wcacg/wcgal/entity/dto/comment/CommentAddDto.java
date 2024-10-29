package com.wcacg.wcgal.entity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentAddDto {
    private Long commentId = 0L;

    @NotBlank
    private String resourceId;

    @Length(min = 1, max = 5000)
    @NotBlank
    private String content;
}
