package com.wcacg.wcgal.controller;

import com.wcacg.wcgal.annotation.NeedToken;
import com.wcacg.wcgal.entity.Comment;
import com.wcacg.wcgal.entity.dto.PageDto;
import com.wcacg.wcgal.entity.dto.comment.CommentAddDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDelDto;
import com.wcacg.wcgal.entity.dto.comment.CommentDto;
import com.wcacg.wcgal.entity.dto.comment.CommentUpdateDto;
import com.wcacg.wcgal.entity.message.PageMessage;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import com.wcacg.wcgal.service.CommentService;
import com.wcacg.wcgal.service.type.ResourceType;
import com.wcacg.wcgal.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /**
     * 获取资源评论
     * @param resourceId 资源 id (资源 id: 资源类型前两位_id)
     * @param pageDto 分页数据
     * @return 评论分页数据
     */
    @PostMapping("/{resourceId}")
    public PageMessage<CommentDto> getComments(@PathVariable String resourceId, @Validated @RequestBody PageDto pageDto){
        Page<? extends Comment> page = this.commentService.getComments(resourceId, pageDto);
        if (page == null){
            return PageMessage.dataError("资源id " + resourceId + " 不存在... qwq", null);
        }

        return PageMessage.success(page, this.commentService.findSubCommentToCommentDto(page));
    }

    /**
     * 发送评论
     * @param commentAddDto 发送评论数据
     * @param request 请求头
     */
    @NeedToken
    @PostMapping("/send")
    public ResponseMessage<CommentDto> sendComment(@Validated @RequestBody CommentAddDto commentAddDto, HttpServletRequest request){
        CommentDto comment = null;
        if (ResourceType.getType(commentAddDto.getResourceId()) == ResourceType.ARTICLE) {
            comment = this.commentService.addArticleComment(commentAddDto, TokenUtils.decodedTokenUserId(request));
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(comment);
    }

    /**
     * 编辑评论
     * @param commentUpdateDto 评论数据
     * @param request 请求头
     * @return 评论
     */
    @NeedToken
    @PostMapping("/update")
    public ResponseMessage<CommentDto> updateComment(@Validated @RequestBody CommentUpdateDto commentUpdateDto, HttpServletRequest request){
        return ResponseMessage.success(this.commentService.updateComment(commentUpdateDto, request));
    }

    /**
     * 删除评论
     * @param commentDelDto 评论数据
     * @param request 请求头
     * @return 响应状态消息
     */
    @NeedToken
    @PostMapping("/del")
        public ResponseMessage<Null> del(@Validated @RequestBody CommentDelDto commentDelDto, HttpServletRequest request){
        if (ResourceType.getType(commentDelDto.getResourceId()) == ResourceType.ARTICLE) {
            this.commentService.delArticleComment(commentDelDto, request);
        } else if(ResourceType.getType(commentDelDto.getResourceId()) == ResourceType.COMMENT) {
            this.commentService.delSubComment(commentDelDto, request);
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }
        return ResponseMessage.success(null);
    }
}
