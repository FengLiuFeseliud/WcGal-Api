package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PageDto {
    @NotNull
    @DecimalMin( value = "0")
    private Integer page;

    @NotNull
    @DecimalMin(value = "1")
    private Integer limit;
    private Boolean desc;

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Boolean getDesc() {
        return desc;
    }
}
