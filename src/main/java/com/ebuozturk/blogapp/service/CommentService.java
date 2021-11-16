package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.converter.CommentConverter;
import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.comment.CreateCommentRequest;
import com.ebuozturk.blogapp.dto.comment.UpdateCommentRequest;
import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.CommentNotFoundException;
import com.ebuozturk.blogapp.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final UserService userService;
    private final EntryService entryService;

    public CommentService(CommentRepository commentRepository, CommentConverter commentConverter, UserService userService, EntryService entryService) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.userService = userService;
        this.entryService = entryService;
    }

    public CommentDto getById(String id){
        return commentConverter.convert(findById(id));
    }

    public List<CommentDto> getAllComments(){
        return commentConverter.convert(commentRepository.findAll());
    }

    public List<CommentDto> getByUserId(String id){
        return commentConverter.convert(findByUserId(id));
    }
    public List<CommentDto> getByEntryId(String id) {
        return commentConverter.convert(findByEntryId(id));
    }
    public List<CommentDto> getByUserIdAndEntryId(String userId,String entryId){
        return  commentConverter.convert(findByUserIdAndEntryId(userId,entryId));
    }
    public CommentDto createComment(CreateCommentRequest request){
        User user = userService.findById(request.getUserId());
        Entry entry = entryService.findById(request.getEntryId());

        Comment comment = new Comment(
                request.getComment(),
                entry,
                user,
                false
        );

        return commentConverter.convert(commentRepository.save(comment));
    }

    public CommentDto updateComment(String id, UpdateCommentRequest request){

        Comment comment = findById(id);

        Comment updateComment = new Comment(
                comment.getId(),
                request.getComment(),
                comment.getEntry(),
                comment.getUser(),
                true
        );
        return commentConverter.convert(commentRepository.save(updateComment));
    }

    public void deleteComment(String id){
        Comment comment = findById(id);
        commentRepository.delete(comment);
    }
    protected Comment findById(String id){
        return commentRepository.findById(id).orElseThrow(()-> new CommentNotFoundException("The comment couldn't be found by following id: "+id));
    }
    protected List<Comment> findByUserId(String id){
        return commentRepository.findByUser_id(id);
    }
    protected List<Comment> findByEntryId(String id){
        return commentRepository.findByEntry_id(id);
    }
    protected List<Comment> findByUserIdAndEntryId(String userId,String entryId){
        return commentRepository.findByUser_idAndEntry_id(userId,entryId);
    }

}
