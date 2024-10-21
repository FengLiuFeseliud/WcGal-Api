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
import com.wcacg.wcgal.service.type.ServiceErrorType;
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
     *
     * @param resourceId 资源 id (资源 id: 资源类型前两位_id)
     * @param pageDto 分页数据
     * @return 响应状态消息
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
    public ResponseMessage<Null> sendComment(@Validated @RequestBody CommentAddDto commentAddDto, HttpServletRequest request){
        Comment comment = null;
        if (ResourceType.getType(commentAddDto.getResourceId()) == ResourceType.ARTICLE) {
            comment = this.commentService.addArticleComment(commentAddDto, TokenUtils.decodedTokenUserId(request));
        }

        if (comment == null){
            return ResponseMessage.dataError("评论发送失败... qwq", null);
        }
        return ResponseMessage.success(null);
    }

    @NeedToken
    @PostMapping("/update")
    public ResponseMessage<Null> updateComment(@Validated @RequestBody CommentUpdateDto commentUpdateDto, HttpServletRequest request){
        ServiceErrorType type = this.commentService.updateComment(commentUpdateDto, request);
        if (type == ServiceErrorType.NOT_FIND){
            return ResponseMessage.dataError("评论 " + commentUpdateDto.getCommentId() + " 不存在... qwq", null);
        }

        if(type == ServiceErrorType.NOT_PERMISSIONS){
            return ResponseMessage.dataError("你只能修改自己的评论...", null);
        }
        return ResponseMessage.success(null);
    }

    @NeedToken
    @PostMapping("/del")
        public ResponseMessage<Null> del(@Validated @RequestBody CommentDelDto commentDelDto, HttpServletRequest request){
        ServiceErrorType type;
        if (ResourceType.getType(commentDelDto.getResourceId()) == ResourceType.ARTICLE) {
            type = this.commentService.delArticleComment(commentDelDto, request);
        } else if(ResourceType.getType(commentDelDto.getResourceId()) == ResourceType.COMMENT) {
            type = this.commentService.delSubComment(commentDelDto, request);
        } else {
            return ResponseMessage.dataError("不存在的资源类型... qwq", null);
        }

        if (type == ServiceErrorType.NOT_FIND){
            return ResponseMessage.dataError("资源id " + commentDelDto.getResourceId() + " 不存在... qwq", null);
        }

        if(type == ServiceErrorType.NOT_PERMISSIONS){
            return ResponseMessage.dataError("你只能删除自己的评论...", null);
        }
        return ResponseMessage.success(null);
    }
}
