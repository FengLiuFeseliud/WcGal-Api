package com.wcacg.wcgal.service;

import com.wcacg.wcgal.entity.ArticleTags;
import com.wcacg.wcgal.entity.QArticleTags;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.TagsDto;
import com.wcacg.wcgal.repository.ArticleTagsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleTagService {
    ArticleTagsRepository getArticleTagsRepository();

    default ArticleTags getArticleTag(Long tagId) {
        return this.getArticleTagsRepository().findById(tagId).orElse(null);
    }

    default Iterable<ArticleTags> getArticleTagsByTagNames(String[] tagNames) {
        return this.getArticleTagsRepository().findAll(QArticleTags.articleTags.tagName.in(tagNames));
    }

    private void createArticleTag(ArticleTags articleTags){
        this.getArticleTagsRepository().save(articleTags);
    }

    default void addArticleTag(TagsDto tags) {
        tags.getTags().forEach(tag -> {
            if (this.getArticleTagsRepository().findByTagName(tag) != null) {
                return;
            }

            this.createArticleTag(new ArticleTags(tag, 1));
        });
    }

    default void countTags(TagsDto tags) {
        tags.getTags().forEach(tag -> {
            ArticleTags articleTag = this.getArticleTagsRepository().findByTagName(tag);
            if (articleTag != null) {
                articleTag.setTagCount(articleTag.getTagCount() + 1);
                this.createArticleTag(articleTag);
                return;
            }

            this.createArticleTag(new ArticleTags(tag, 1));
        });
    }

    default Page<ArticleTags> getArticleTags(PageDto pageDto) {
        return this.getArticleTagsRepository().findAll(PageRequest.of(pageDto.getPage(), pageDto.getLimit()));
    }

    @Transactional
    default List<ArticleTags> getAllArticleTags() {
        return this.getArticleTagsRepository().selectSmall();
    }

    default Long tagsCount() {
        return this.getArticleTagsRepository().count();
    }
}
