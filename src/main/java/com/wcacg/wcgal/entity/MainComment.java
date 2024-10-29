package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
public class MainComment extends Comment {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_comment_id", referencedColumnName = "comment_id")
    private List<Comment> subComment = new ArrayList<>();

    public MainComment(){

    }

    public MainComment(long commentId){
        this.setCommentId(commentId);
    }
}
