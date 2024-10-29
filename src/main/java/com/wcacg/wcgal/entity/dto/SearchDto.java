package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDto extends PageDto{
    @Length(min = 1, max = 200)
    @NotBlank
    private String keyword;
}
