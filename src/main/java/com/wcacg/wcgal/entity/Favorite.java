package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tb_favorite")
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

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public String getFavoriteDescribe() {
        return favoriteDescribe;
    }

    public void setFavoriteDescribe(String favoriteDescribe) {
        this.favoriteDescribe = favoriteDescribe;
    }

    public Boolean getFavoritePublic() {
        return favoritePublic;
    }

    public void setFavoritePublic(Boolean favoritePublic) {
        this.favoritePublic = favoritePublic;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public String getCover() {
        return this.cover == null ? "/img/cover/favorite/favorite.jpg" : this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Boolean getInLike() {
        return inLike;
    }

    public void setInLike(Boolean inLike) {
        this.inLike = inLike;
    }
}
