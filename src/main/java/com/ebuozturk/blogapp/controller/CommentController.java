package com.ebuozturk.blogapp.controller;

import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.comment.CreateCommentRequest;
import com.ebuozturk.blogapp.dto.comment.UpdateCommentRequest;
import com.ebuozturk.blogapp.service.CommentService;
import com.ebuozturk.blogapp.utils.HateoasLinkSupporter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/comment")
public class CommentController extends HateoasLinkSupporter {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@RequestParam("id") String userId ){
        List<CommentDto> commentDtoList = commentService.getByUserId(userId);
        commentDtoList.forEach(this::addLinkToCommentDto);
        return ResponseEntity.ok(commentDtoList);
    }
    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable String id){
        CommentDto commentDto = commentService.getById(id);
        addLinkToCommentDto(commentDto);
        return ResponseEntity.ok(commentDto);
    }
    @GetMapping("/entry")
    public ResponseEntity<List<CommentDto>> getCommentsByEntryId(@RequestParam("id") String id){
        List<CommentDto> commentDtoList = commentService.getByEntryId(id);
        commentDtoList.forEach(this::addLinkToCommentDto);
        return ResponseEntity.ok(commentDtoList);
    }
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByEntryIdAndUserId(@RequestParam("entryId") String entryId, @RequestParam("userId") String userId){
        List<CommentDto> commentDtoList = commentService.getByUserIdAndEntryId(userId,entryId);
        commentDtoList.forEach(this::addLinkToCommentDto);
        return ResponseEntity.ok(commentDtoList);
    }
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest request){
        return new ResponseEntity<>(commentService.createComment(request), HttpStatus.CREATED);
    }
    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String id, @Valid @RequestBody UpdateCommentRequest request){
        return ResponseEntity.ok(commentService.updateComment(id,request));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

}
