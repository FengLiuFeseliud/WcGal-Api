package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TagsDto {
    @NotEmpty
    private List<String> tags;

    public TagsDto() {
        super();
    }

    public TagsDto(List<String> tags) {
        this.tags = tags;
    }
}
