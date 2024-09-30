package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;
import java.util.List;

public class TagsDto {
    @NotEmpty
    private List<String> tags;

    public TagsDto() {
        super();
    }

    public TagsDto(List<String> tags) {
        this.tags = tags;
    }

    public @NotEmpty List<String> getTags() {
        return tags;
    }

    public void setTags(@NotEmpty List<String> tags) {
        this.tags = tags;
    }
}
