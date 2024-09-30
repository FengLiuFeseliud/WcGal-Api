package com.wcacg.wcgal.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class ArticleTagDto {
    private Integer tagId;
    @NotBlank
    private String tagName;
    private Integer tagCount;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }
}
