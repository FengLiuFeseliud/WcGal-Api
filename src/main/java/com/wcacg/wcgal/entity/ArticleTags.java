package com.wcacg.wcgal.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@Entity
@DynamicInsert
@Table(name = "tb_article_tags")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
public class ArticleTags extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", unique = true, nullable = false)
    private Integer tagId;
    @Column(name = "tag_name", unique = true, nullable = false)
    private String tagName;
    @Column(name = "tag_count", nullable = false)
    @ColumnDefault("0")
    private Integer tagCount;

    public ArticleTags(){
        super();
    }

    public ArticleTags(Integer tagId, String tagName, Integer tagCount) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagCount = tagCount;
    }

    public ArticleTags(String tagName, Integer tagCount) {
        this.tagName = tagName;
        this.tagCount = tagCount;
    }
}
