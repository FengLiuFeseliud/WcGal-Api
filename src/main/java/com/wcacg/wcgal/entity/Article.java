package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_article")
@EntityListeners(AuditingEntityListener.class)
public class Article extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", unique = true, nullable = false)
    private Long articleId;

    @Column(name = "article_title", nullable = false)
    private String articleTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_author_id", referencedColumnName = "user_id", nullable = false)
    private User articleAuthor;

    @Lob
    @Column(name = "article_content", nullable = false, columnDefinition="longtext")
    private String articleContent;

    @Column(nullable = false)
    private String cover;

    @ColumnDefault("\"\"")
    private String tags;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long favorites;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long likes;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long comments;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long views;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_article_comment")
    private List<Comment> commentList = new ArrayList<>();

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public User getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(User articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }
}
