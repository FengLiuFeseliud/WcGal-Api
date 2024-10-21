package com.wcacg.wcgal.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class MainComment extends Comment {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_comment_id", referencedColumnName = "comment_id")
    private List<Comment> subComment = new ArrayList<>();

    public MainComment(){

    }

    public MainComment(long commentId){
        this.setCommentId(commentId);
    }

    public List<Comment> getSubComment() {
        return subComment;
    }

    public void setSubComment(List<Comment> subComment) {
        this.subComment = subComment;
    }
}
