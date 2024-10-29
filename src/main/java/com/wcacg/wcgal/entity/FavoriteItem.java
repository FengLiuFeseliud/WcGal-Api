package com.wcacg.wcgal.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@Table(name = "tb_favorite_item")
@EntityListeners(AuditingEntityListener.class)
public class FavoriteItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    private long itemId;

    @Column(name = "favorite_id", nullable = false)
    private Long favoriteId;

    @Column(name = "favorite_user_id", nullable = false)
    private Long favoriteUserId;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    protected Date createTime;
}
