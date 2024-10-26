package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tb_comment")
@EntityListeners(AuditingEntityListener.class)
public class Comment extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", unique = true, nullable = false)
    private Long commentId;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Lob
    @Column(nullable = false, columnDefinition = "mediumtext")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_author_id", referencedColumnName = "user_id", nullable = false)
    private User commentAuthor;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long likes;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(User commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }
}
