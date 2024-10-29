package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "tb_article")
@EqualsAndHashCode(callSuper = true)
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
}
