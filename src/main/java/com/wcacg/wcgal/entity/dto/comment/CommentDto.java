package com.wcacg.wcgal.entity.dto.comment;

import com.wcacg.wcgal.entity.dto.user.UserInfoDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentDto {
    private Long commentId;
    private String resourceId;
    private String content;
    private UserInfoDto commentAuthor;
    private Long likes;
    private Date createTime;
    private Date updateTime;
    private int subCommentCount;
    private List<CommentDto> subComment = new ArrayList<>();

    public void addSubComments(CommentDto commentDto) {
        this.subComment.add(commentDto);
    }
}
