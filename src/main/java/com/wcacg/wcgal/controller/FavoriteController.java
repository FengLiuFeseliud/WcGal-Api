package com.wcacg.wcgal.controller;

import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.FavoriteItem;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteAddDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteItemDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteSetItemDto;
import com.wcacg.wcgal.entity.message.PageMessage;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.service.FavoriteService;
import com.wcacg.wcgal.service.type.ResourceType;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }

    @NeedToken
    @PostMapping("/my")
    public ResponseMessage<List<FavoriteDto>> getMyFavorites(HttpServletRequest request){
        return ResponseMessage.success(this.favoriteService.getUserFavorites(TokenUtils.decodedTokenUserId(request)));
    }

    @PostMapping("/{favoriteId}")
    public ResponseMessage<FavoriteDto> getFavorite(@PathVariable long favoriteId, HttpServletRequest request){
        return ResponseMessage.success(this.favoriteService.getFavorite(favoriteId, TokenUtils.decodedTokenUserIdOrNotUserId(request)));
    }

    @PostMapping("/list/{favoriteId}")
    public PageMessage<FavoriteItemDto> getFavoriteItems(@PathVariable long favoriteId, @Validated @RequestBody PageDto pageDto, HttpServletRequest request){
        Page<FavoriteItem> page = this.favoriteService.getFavoriteItems(pageDto, favoriteId, TokenUtils.decodedTokenUserIdOrNotUserId(request));
        return PageMessage.success(page, this.favoriteService.favoriteItemPageToDtoList(page));
    }

    /**
     * 创建收藏夹
     * @param favoriteAddDto 收藏夹数据
     * @param request 请求头
     * @return 收藏夹
     */
    @NeedToken
    @PostMapping("/create")
    public ResponseMessage<FavoriteDto> createFavorite(@Validated @RequestBody FavoriteAddDto favoriteAddDto, HttpServletRequest request){
        return ResponseMessage.success(this.favoriteService.createFavorite(favoriteAddDto, TokenUtils.decodedTokenUserId(request)));
    }

    /**
     * 收藏资源
     * @param favoriteAddItemDto 收藏数据
     * @param request 请求头
     * @return 响应状态消息
     */
    @NeedToken
    @PostMapping("/add")
    public ResponseMessage<Null> favoriteAddItem(@Validated @RequestBody FavoriteSetItemDto favoriteAddItemDto, HttpServletRequest request){
        if (ResourceType.getType(favoriteAddItemDto.getResourceId()) == ResourceType.ARTICLE) {
            this.favoriteService.favoriteAddArticle(favoriteAddItemDto, TokenUtils.decodedTokenUserId(request));
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(null);
    }


    /**
     * 收藏资源
     * @param favoriteAddItemDto 收藏数据
     * @param request 请求头
     * @return 响应状态消息
     */
    @NeedToken
    @PostMapping("/del")
    public ResponseMessage<Null> favoriteDelItem(@Validated @RequestBody FavoriteSetItemDto favoriteAddItemDto, HttpServletRequest request){
        if (ResourceType.getType(favoriteAddItemDto.getResourceId()) == ResourceType.ARTICLE) {
            this.favoriteService.favoriteDelArticle(favoriteAddItemDto, TokenUtils.decodedTokenUserId(request));
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(null);
    }


    @NeedToken
    @PostMapping("/like/{resourceId}")
    public ResponseMessage<Null> like(@PathVariable String resourceId, HttpServletRequest request){
        if (ResourceType.getType(resourceId) == ResourceType.ARTICLE) {
            this.favoriteService.likeArticle(resourceId, TokenUtils.decodedTokenUserId(request));
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(null);
    }

    @NeedToken
    @PostMapping("/unlike/{resourceId}")
    public ResponseMessage<Null> unlike(@PathVariable String resourceId, HttpServletRequest request){
        if (ResourceType.getType(resourceId) == ResourceType.ARTICLE) {
            this.favoriteService.unLikeArticle(resourceId, TokenUtils.decodedTokenUserId(request));
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(null);
    }
}
