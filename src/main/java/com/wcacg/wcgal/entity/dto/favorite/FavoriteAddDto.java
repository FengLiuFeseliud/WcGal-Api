package com.wcacg.wcgal.entity.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class FavoriteAddDto {
    @Length(min = 1, max = 50)
    @NotBlank
    private String favoriteName;

    @Length(min = 1, max = 500)
    private String favoriteDescribe;
    @NotNull
    private Boolean favoritePublic;

    @URL
    private String cover;
}
