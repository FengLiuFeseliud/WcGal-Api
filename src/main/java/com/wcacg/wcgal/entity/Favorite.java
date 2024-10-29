package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "tb_favorite")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Favorite extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", unique = true, nullable = false)
    private Long favoriteId;

    @Column(name = "favorite_name", nullable = false)
    private String favoriteName;

    @Lob
    @Column(name = "favorite_describe", columnDefinition = "text")
    private String favoriteDescribe;

    @ColumnDefault("true")
    @Column(name = "public", nullable = false)
    private Boolean favoritePublic;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id", referencedColumnName = "user_id", nullable = false)
    private User createUser;

    private String cover;

    @ColumnDefault("false")
    @Column(name = "in_like", nullable = false)
    private Boolean inLike;

    public Favorite(){
        this.inLike = false;
        this.size = 0;
    }

    public Favorite(String favoriteName, long userId, boolean inLike){
        this.favoriteName = favoriteName;
        this.createUser = new User(userId);
        this.inLike = inLike;
        this.favoritePublic = true;
        this.size = 0;
    }

    public static Favorite createLikeFavorite(long userId){
        return new Favorite("喜欢的资源", userId, true);
    }

    public String getCover() {
        return this.cover == null ? "/img/cover/favorite/favorite.jpg" : this.cover;
    }
}
