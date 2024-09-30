package com.wcacg.wcgal.controller;

import com.wcacg.wcgal.entity.Article;
import com.wcacg.wcgal.entity.ArticleTags;
import com.wcacg.wcgal.entity.dto.*;
import com.wcacg.wcgal.entity.message.PageMessage;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.service.ArticleService;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/galgame")
public class GalGameArticleController {
     private final ArticleService service;

    public GalGameArticleController(ArticleService service) {
        this.service = service;
    }

    /**
     * 获取文章
     * @param articleId 文章 id
     * @return 文章
     */
    @PostMapping("/{articleId}")
    public ResponseMessage<ArticleDto> get(@PathVariable Long articleId){
        return ResponseMessage.success(service.findArticleTagsToArticleDto(service.getArticle(articleId)));
    }

    /**
     * 上传更新文章, 传入文章 id 将更新文章
     * @param articleDto 文章内容
     * @return 文章
     */
    @PostMapping("/add")
    public ResponseMessage<Article> add(@Validated @RequestBody ArticleDto articleDto){
        return ResponseMessage.success(service.addArticle(articleDto));
    }

    /**
     * 上传更新文章, 传入文章 id 将更新文章
     * @param articleDto 文章内容
     * @return 文章
     */
    @PostMapping("/update")
    public ResponseMessage<Article> update(@Validated @RequestBody ArticleDto articleDto){
        Article article = service.updateArticle(articleDto);
        if (article == null) {
            return ResponseMessage.dataError("文章 id 错误了...", null);
        }
        return ResponseMessage.success(article);
    }

    /**
     * 删除文章
     * @param articleId 文章 id
     * @return 文章 id
     */
    @PostMapping("/del/{articleId}")
    public ResponseMessage<Long> del(@PathVariable Long articleId){
        return ResponseMessage.success(service.deleteArticle(articleId));
    }

    /**
     * 获取文章列表
     * @param pageDto 分页数据
     * @return 文章列表
     */
    @PostMapping("/list")
    public PageMessage<ArticleDto> list(@Validated @RequestBody PageDto pageDto){
        Page<Article> page = service.getArticles(pageDto);
        return PageMessage.success(page, service.findArticleTagsToArticleDtoList(page));
    }

    @PostMapping("/search")
    public PageMessage<ArticleDto> search(@Validated @RequestBody SearchDto pageDto){
        Page<Article> page = service.searchArticles(pageDto);
        return PageMessage.success(page, service.findArticleTagsToArticleDtoList(page));
    }

    /**
     * 获取标签
     * @param tagId 标签 id
     * @return 标签
     */
    @PostMapping("/tags/{tagId}")
    public ResponseMessage<ArticleTags> getTag(@PathVariable Long tagId){
        return ResponseMessage.success(service.getArticleTag(tagId));
    }

    /**
     * 获取标签列表
     * @param pageDto 分页数据
     * @return 标签列表
     */
    @PostMapping("/tags/list")
    public PageMessage<ArticleTags> listTags(@Validated @RequestBody PageDto pageDto){
        return PageMessage.success(service.getArticleTags(pageDto));
    }

    @PostMapping("/tags/all")
    public ResponseMessage<List<ArticleTags>> allTags(){
        return ResponseMessage.success(service.getAllArticleTags());
    }
}