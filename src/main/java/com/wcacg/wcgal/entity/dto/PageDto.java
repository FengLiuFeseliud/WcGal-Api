package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PageDto {
    @NotNull
    @DecimalMin(value = "0")
    private Integer page;

    @NotNull
    @Range(min = 1, max = 50)
    private Integer limit;
    private Boolean desc;
}
