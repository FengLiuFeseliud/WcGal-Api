package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "tb_comment")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
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
}
