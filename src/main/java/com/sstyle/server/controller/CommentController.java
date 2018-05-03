package com.sstyle.server.controller;

import com.sstyle.server.domain.Comment;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.CommentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ss
 * @Date 2018/4/28 15:18
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/save-comment")
    public JSONResult queryComments(@RequestBody Comment comment) {
        Comment comm = commentService.saveComment(comment);
        if (comm !=null) {
            return new JSONResult(comm);
        }
        return new JSONResult("failed");
    }

    @RequestMapping("/delete-comment")
    public JSONResult delComments(@RequestBody Comment comment) {
       return new JSONResult(commentService.deleteComment(comment));
    }
}
