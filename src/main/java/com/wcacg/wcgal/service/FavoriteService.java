package com.wcacg.wcgal.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wcacg.wcgal.entity.*;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteAddDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteItemDto;
import com.wcacg.wcgal.entity.dto.favorite.FavoriteSetItemDto;
import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import com.wcacg.wcgal.exception.ClientError;
import com.wcacg.wcgal.repository.ArticleRepository;
import com.wcacg.wcgal.repository.FavoriteItemRepository;
import com.wcacg.wcgal.repository.FavoriteRepository;
import com.wcacg.wcgal.repository.UserRepository;
import com.wcacg.wcgal.service.type.ResourceType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FavoriteService {
    private final ArticleService articleService;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteItemRepository favoriteItemRepository;
    private final ArticleRepository articleRepository;

    public FavoriteService(ArticleService articleService,
                           UserRepository userRepository,
                           FavoriteRepository favoriteRepository,
                           FavoriteItemRepository favoriteItemRepository,
                           ArticleRepository articleRepository){
        this.articleService = articleService;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.favoriteItemRepository = favoriteItemRepository;
        this.articleRepository = articleRepository;
    }

    public static FavoriteDto favoriteToFavoriteDto(Favorite favorite){
        FavoriteDto favoriteDto = new FavoriteDto();
        BeanUtils.copyProperties(favorite, favoriteDto);
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(favorite.getCreateUser(), userInfoDto);
        favoriteDto.setCreateUser(userInfoDto);
        return favoriteDto;
    }

    public static List<FavoriteDto> favoriteListToFavoriteDtoList(List<Favorite> favorites){
        List<FavoriteDto> favoriteDtoList = new ArrayList<>();
        favorites.forEach(favorite -> {
            favorite.setCreateUser(new User(0));
            FavoriteDto favoriteDto = favoriteToFavoriteDto(favorite);
            favoriteDto.setCreateUser(null);
            favoriteDtoList.add(favoriteDto);
        });
        return favoriteDtoList;
    }

    public List<FavoriteItemDto> favoriteItemPageToDtoList(Page<FavoriteItem> page){
        return this.articleService.findArticleTagsToFavoriteItemDtoList(page);
    }

    public Favorite getFavoriteFromFavoriteId(long favoriteId, long userId){
        Favorite favorite = this.favoriteRepository.findById(favoriteId).orElse(null);
        if (favorite == null){
            throw new ClientError.NotFindException("收藏夹id " + favoriteId + " 不存在... qwq");
        }

        if (!favorite.getFavoritePublic() && favorite.getCreateUser().getUserId() != userId){
            throw new ClientError.NotPermissionsException("收藏夹id " + favoriteId + " 是私密的... qwq");
        }
        return favorite;
    }

    public FavoriteDto getFavorite(long favoriteId, long userId){
        return favoriteToFavoriteDto(this.getFavoriteFromFavoriteId(favoriteId, userId));
    }

    public List<FavoriteDto> getUserFavorites(long userId){
        return favoriteListToFavoriteDtoList(this.favoriteRepository.findAllByCreateUser(new User(userId)));
    }

    public Page<FavoriteItem> getFavoriteItems(PageDto pageDto, long favoriteId, long userId){
        this.getFavoriteFromFavoriteId(favoriteId, userId);
        this.favoriteItemRepository.findAll(QFavoriteItem.favoriteItem.favoriteId.eq(favoriteId),
                PageRequest.of(pageDto.getPage(), pageDto.getLimit()));

        BooleanExpression be = QFavoriteItem.favoriteItem.favoriteId.eq(favoriteId);
        if (pageDto.getDesc() == null){
            return this.favoriteItemRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit()));
        }
        return this.favoriteItemRepository.findAll(be, PageRequest.of(pageDto.getPage(), pageDto.getLimit(),
                Sort.by(pageDto.getDesc() ? Sort.Direction.DESC: Sort.Direction.ASC, "itemId")));
    }

    public FavoriteDto createFavorite(FavoriteAddDto favoriteAddDto, long userId){
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new ClientError.NotFindException("用户id " + userId + " 不存在... qwq");
        }

        if (user.getFavorites().size() == 30){
            throw new ClientError.MaxCreateException("不能创建更多收藏夹啦...");
        }

        Favorite favorite = new Favorite();
        BeanUtils.copyProperties(favoriteAddDto, favorite);
        favorite.setCreateUser(new User(userId));
        return favoriteToFavoriteDto(this.favoriteRepository.save(favorite));
    }

    public Article getArticleByResourceId(String resourceId){
        Article article = this.articleRepository.findById(ResourceType.getId(resourceId)).orElse(null);
        if (article == null){
            throw new ClientError.NotFindException("文章资源id " + resourceId + " 不存在... qwq");
        }
        return article;
    }

    public List<FavoriteItem> getFavoriteItems(String resourceId, List<Long> favoriteIds){
        QFavoriteItem qFavoriteItem = QFavoriteItem.favoriteItem;
        BooleanExpression be = qFavoriteItem.resourceId.eq(resourceId);
        BooleanExpression inBe = null;
        for (Long favoriteId : favoriteIds) {
            if (inBe == null){
                inBe = qFavoriteItem.favoriteId.eq(favoriteIds.get(0));
                continue;
            }
            inBe = inBe.in(qFavoriteItem.favoriteId.eq(favoriteId));
        }
        be.and(inBe);
        return (List<FavoriteItem>) this.favoriteItemRepository.findAll(be);
    }

    public List<Long> canFindFavoriteId(List<Long> favoriteIds, List<FavoriteItem> favoriteItems, boolean del){
        return favoriteIds.stream().filter(id -> {
            for (FavoriteItem favoriteItem : favoriteItems) {
                if (favoriteItem.getFavoriteId().equals(id)){
                    return del;
                }
            }
            return !del;
        }).toList();
    }

    public void canSetFavorite(List<Favorite> favorites, long userId){
        favorites.forEach(favorite -> {
            if (favorite.getCreateUser().getUserId() != userId){
                throw new ClientError.NotPermissionsException("你只能操作自己的收藏夹... 收藏夹id:" + favorite.getFavoriteId());
            }
        });
    }

    public void favoriteAddArticle(FavoriteSetItemDto favoriteAddItemDto, long userId) {
        Article article = this.getArticleByResourceId(favoriteAddItemDto.getResourceId());
        if (favoriteAddItemDto.getFavoriteIds().size() == 0){
            throw new ClientError.NotFindException("不存在收藏夹... qwq");
        }

        List<FavoriteItem> favoriteItems = this.getFavoriteItems(favoriteAddItemDto.getResourceId(), favoriteAddItemDto.getFavoriteIds());
        List<Long> favoriteIds = this.canFindFavoriteId(favoriteAddItemDto.getFavoriteIds(), favoriteItems, false);
        if (favoriteIds.size() == 0){
            throw new ClientError.HaveExistedException("文章id: " + article.getArticleId() + " 全部收藏夹都收藏过了... ");
        }

        List<Favorite> favorites = (List<Favorite>) this.favoriteRepository.findAllById(favoriteIds);
        if (favorites.size() == 0){
            throw new ClientError.NotFindException("不存在收藏夹... qwq");
        }

        this.canSetFavorite(favorites, userId);
        List<FavoriteItem> saveFavoriteItem = new ArrayList<>();

        AtomicBoolean flag = new AtomicBoolean(false);
        favorites.forEach(favorite -> {
            FavoriteItem favoriteItem = new FavoriteItem();
            favoriteItem.setFavoriteId(favorite.getFavoriteId());
            favoriteItem.setResourceId(favoriteAddItemDto.getResourceId());
            favoriteItem.setArticle(article);
            favoriteItem.setFavoriteUserId(userId);
            favorite.setSize(favorite.getSize() + 1);
            if (favorite.getInLike()){
                article.setLikes(article.getLikes() + 1);
                flag.set(true);
            }
            saveFavoriteItem.add(favoriteItem);
        });
        this.favoriteItemRepository.saveAll(saveFavoriteItem);
        this.favoriteRepository.saveAll(favorites);

        if(favoriteItems.size() == 1){
            Favorite likeFavorite = this.favoriteRepository.findById(favoriteItems.get(0).getFavoriteId()).orElse(null);
            if (likeFavorite != null && likeFavorite.getInLike()){
                article.setFavorites(article.getFavorites() + 1);
            }
        }

        if (favoriteItems.size() == 0 && !(flag.get() && favorites.size() == 1)){
            article.setFavorites(article.getFavorites() + 1);
        }
        this.articleRepository.save(article);
    }

    @Transactional
    public void favoriteDelArticle(FavoriteSetItemDto favoriteSetItemDto, long userId) {
        Article article = this.getArticleByResourceId(favoriteSetItemDto.getResourceId());
        if (favoriteSetItemDto.getFavoriteIds().size() == 0){
            throw new ClientError.NotFindException("不存在收藏夹... qwq");
        }

        List<FavoriteItem> favoriteItems = this.getFavoriteItems(favoriteSetItemDto.getResourceId(), favoriteSetItemDto.getFavoriteIds());
        List<Long> favoriteIds = this.canFindFavoriteId(favoriteSetItemDto.getFavoriteIds(), favoriteItems, true);
        if (favoriteIds.size() == 0){
            throw new ClientError.HaveExistedException("文章id: " + article.getArticleId() + " 全部收藏夹都不存在收藏... ");
        }

        List<Favorite> favorites = (List<Favorite>) this.favoriteRepository.findAllById(favoriteIds);
        if (favorites.size() == 0){
            throw new ClientError.NotFindException("不存在收藏夹... qwq");
        }

        this.canSetFavorite(favorites, userId);
        this.favoriteItemRepository.deleteAllByFavoriteIdIn(favoriteIds);

        AtomicBoolean likeFlag = new AtomicBoolean(false);
        favorites.forEach(favorite -> {
            favorite.setSize(favorite.getSize() > 0 ? favorite.getSize() - 1: 0);
            if (favorite.getInLike()){
                article.setLikes(article.getLikes()  > 0 ? article.getLikes() - 1: 0);
                likeFlag.set(true);
            }
        });

        this.favoriteRepository.saveAll(favorites);
        if (favorites.size() == favoriteItems.size()){
            article.setFavorites(article.getFavorites() > 0 ? article.getFavorites() - 1: 0);
            articleRepository.save(article);
            return;
        }

        List<FavoriteItem> favoriteItemList = favoriteItems.stream().filter(favoriteItem ->
                favorites.stream().filter(favorite -> favorite.getFavoriteId().equals(favoriteItem.getFavoriteId()))
                        .toList().isEmpty()).toList();
        if (favoriteItemList.size() == 1){
            Favorite likeFavorite = this.favoriteRepository.findById(favoriteItemList.get(0).getFavoriteId()).orElse(null);
            if (likeFavorite != null && likeFavorite.getInLike()){
                article.setFavorites(article.getFavorites() > 0 ? article.getFavorites() - 1: 0);
                articleRepository.save(article);
                return;
            }
        }

        if (likeFlag.get()){
            articleRepository.save(article);
        }
    }

    public void likeArticle(String resourceId, long userId) {
        Article article = this.getArticleByResourceId(resourceId);
        Favorite likeFavorite = this.favoriteRepository.findOne(QFavorite.favorite.inLike.eq(true)).orElse(null);
        if (likeFavorite == null){
            likeFavorite = this.favoriteRepository.save(Favorite.createLikeFavorite(userId));
        } else {
            QFavoriteItem qFavoriteItem = QFavoriteItem.favoriteItem;
            if (this.favoriteItemRepository.findOne(qFavoriteItem.favoriteId.eq(likeFavorite.getFavoriteId())
                    .and(qFavoriteItem.resourceId.eq(resourceId))).isPresent()){
                throw new ClientError.HaveExistedException("文章id: " + article.getArticleId() + " 己经喜欢了... ");
            }
        }

        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setFavoriteId(likeFavorite.getFavoriteId());
        favoriteItem.setResourceId(resourceId);
        favoriteItem.setArticle(article);
        favoriteItem.setFavoriteUserId(userId);
        likeFavorite.setSize(likeFavorite.getSize() + 1);
        article.setLikes(article.getLikes() + 1);

        this.favoriteItemRepository.save(favoriteItem);
        this.favoriteRepository.save(likeFavorite);
        this.articleRepository.save(article);
    }

    @Transactional
    public void unLikeArticle(String resourceId, long userId) {
        Article article = this.getArticleByResourceId(resourceId);
        Favorite likeFavorite = this.favoriteRepository.findOne(QFavorite.favorite.inLike.eq(true)).orElse(null);
        if (likeFavorite == null){
            this.favoriteRepository.save(Favorite.createLikeFavorite(userId));
            return;
        }

        QFavoriteItem qFavoriteItem = QFavoriteItem.favoriteItem;
        FavoriteItem favoriteItem = this.favoriteItemRepository.findOne(qFavoriteItem.favoriteId.eq(likeFavorite.getFavoriteId())
                .and(qFavoriteItem.resourceId.eq(resourceId))).orElse(null);
        if (favoriteItem == null){
            throw new ClientError.HaveExistedException("文章id: " + article.getArticleId() + " 己经不喜欢了... ");
        }

        this.favoriteItemRepository.delete(favoriteItem);
        likeFavorite.setSize(likeFavorite.getSize() == 0 ? 0: likeFavorite.getSize() - 1);
        article.setLikes(article.getLikes() == 0 ? 0: article.getLikes() - 1);
        this.favoriteRepository.save(likeFavorite);
        this.articleRepository.save(article);
    }
}
