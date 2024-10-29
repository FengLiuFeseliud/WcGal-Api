package com.wcacg.wcgal.entity.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class FavoriteSetItemDto {
    @Size(min = 1, max = 31)
    @NotNull
    private List<Long> favoriteIds;

    @NotBlank
    private String resourceId;
}
