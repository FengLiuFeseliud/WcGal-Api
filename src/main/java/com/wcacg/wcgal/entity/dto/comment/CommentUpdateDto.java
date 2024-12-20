package com.wcacg.wcgal.entity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentUpdateDto {
    @NotNull
    private Long commentId = 0L;

    @Length(min = 1, max = 5000)
    @NotBlank
    private String content;
}
