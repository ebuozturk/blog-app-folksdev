package com.ebuozturk.blogapp.service;

import com.ebuozturk.blogapp.TestSupporter;
import com.ebuozturk.blogapp.converter.CommentConverter;
import com.ebuozturk.blogapp.dto.comment.CommentDto;
import com.ebuozturk.blogapp.dto.comment.CreateCommentRequest;
import com.ebuozturk.blogapp.dto.comment.UpdateCommentRequest;
import com.ebuozturk.blogapp.entity.Comment;
import com.ebuozturk.blogapp.entity.Entry;
import com.ebuozturk.blogapp.entity.User;
import com.ebuozturk.blogapp.exception.CommentNotFoundException;
import com.ebuozturk.blogapp.exception.EntryNotFoundException;
import com.ebuozturk.blogapp.exception.UserNotFoundException;
import com.ebuozturk.blogapp.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest extends TestSupporter {

    private CommentRepository commentRepository;
    private CommentConverter commentConverter;
    private UserService userService;
    private EntryService entryService;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentConverter = mock(CommentConverter.class);
        userService = mock(UserService.class);
        entryService = mock(EntryService.class);
        commentService = new CommentService(commentRepository,commentConverter,userService,entryService);
    }

    @Test
    public void testCreateComment_whenUserAndEntryExist_itShouldReturnCommentDto(){
        String userId="1";
        String entryId="2";
        String commentId="1";

        User user =  generateUser(userId);
        Entry entry = generateEntry(entryId,"title","content");
        Comment saveComment =  generateComment(userId,entryId,"comment",false);
        CreateCommentRequest request = new CreateCommentRequest(
          "comment",
          userId,
          entryId
        );
        Comment savedComment = generateComment(commentId,userId,entryId,"comment",false);
        CommentDto commentDto = generateCommentDto(commentId,userId,entryId,"comment",false);

        when(userService.findById(userId)).thenReturn(user);
        when(entryService.findById(entryId)).thenReturn(entry);
        when(commentRepository.save(saveComment)).thenReturn(savedComment);
        when(commentConverter.convert(savedComment)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(request);

        assertEquals(result,commentDto);

        verify(userService).findById(userId);
        verify(entryService).findById(entryId);
        verify(commentRepository).save(saveComment);
        verify(commentConverter).convert(savedComment);

    }

    @Test
    public void testCreateComment_whenUserNotExistAndEntryExist_itShouldThrowUserNotFoundException(){
        String userId="1";
        String entryId="2";

        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                userId,
                entryId
        );

        when(userService.findById(userId)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()->commentService.createComment(request));

        verify(userService).findById(userId);
        verifyNoInteractions(entryService);
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(commentConverter);

    }
    @Test
    public void testCreateComment_whenUserExistAndEntryNotExist_itShouldThrowEntryNotFoundException(){
        String userId="1";
        String entryId="2";

        User user =  generateUser(userId);
        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                userId,
                entryId
        );

        when(userService.findById(userId)).thenReturn(user);
        when(entryService.findById(entryId)).thenThrow(EntryNotFoundException.class);

        assertThrows(EntryNotFoundException.class,()->commentService.createComment(request));

        verify(userService).findById(userId);
        verify(entryService).findById(entryId);
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(commentConverter);

    }
    @Test
    public void testCreateComment_whenUserNotExistAndEntryNotExist_itShouldThrowUserNotFoundException(){
        String userId="1";
        String entryId="2";

        User user =  generateUser(userId);
        CreateCommentRequest request = new CreateCommentRequest(
                "comment",
                userId,
                entryId
        );

        when(userService.findById(userId)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,()->commentService.createComment(request));

        verify(userService).findById(userId);
        verifyNoInteractions(entryService);
        verifyNoInteractions(commentRepository);
        verifyNoInteractions(commentConverter);

    }

    @Test
    public void testUpdateComment_whenCommentExist_itShouldReturnCommentDto(){
        String commentId="1";
        String userId="2";
        String entryId="3";
        UpdateCommentRequest request = new UpdateCommentRequest(
                "updated comment",
                userId,
                entryId
        );
        Comment comment = generateComment(commentId,userId,entryId,"comment",false);
        Comment updateComment = generateComment(commentId,userId,entryId,"updated comment",true);
        Comment updatedComment = generateComment(commentId,userId,entryId,"updated comment",true);
        CommentDto commentDto = generateCommentDto(commentId,userId,entryId,"updated comment",true);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(updateComment)).thenReturn(updatedComment);
        when(commentConverter.convert(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment(commentId,request);

        assertEquals(result,commentDto);

        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(updateComment);
        verify(commentConverter).convert(updatedComment);
    }
    @Test
    public void testUpdateComment_whenCommentNotExist_itShouldThrowCommentNotFoundException(){
        String commentId="1";
        String userId="2";
        String entryId="3";

        UpdateCommentRequest request = new UpdateCommentRequest(
                "updated comment",
                userId,
                entryId
        );

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,()->commentService.updateComment(commentId,request));

        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentRepository);
        verifyNoInteractions(commentConverter);
    }

    @Test
    public void testDeleteComment_whenCommentExist_itShouldReturnVoid(){
        String commentId = "1";
        Comment comment = generateComment(commentId,"1","2","comment",true);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(commentId);

        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);

    }

    @Test
    public void testDeleteComment_whenCommentNotExist_itShouldThrowCommentNotFoundException(){
        String commentId = "1";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

       assertThrows(CommentNotFoundException.class,()-> commentService.deleteComment(commentId));

        verify(commentRepository).findById(commentId);
        verifyNoMoreInteractions(commentRepository);

    }

    @Test
    public void testGetById_whenCommentExist_itShouldReturnCommentDto(){
        String commentId = "1";
        Comment comment = generateComment(commentId,"1","2","comment",false);
        CommentDto commentDto = generateCommentDto(commentId,"1","2","comment",false);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getById(commentId);

        assertEquals(result,commentDto);

        verify(commentRepository).findById(commentId);
        verify(commentConverter).convert(comment);
    }
    @Test
    public void testGetById_whenCommentNotExist_itShouldThrowCommentNotFoundException(){
        String commentId = "1";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,()-> commentService.getById(commentId));

        verify(commentRepository).findById(commentId);
        verifyNoInteractions(commentConverter);
    }

    @Test
    public void testGetAllComments_itShouldReturnCommentDtoList(){
        List<Comment> commentList = generateCommentList(4);
        List<CommentDto> commentDtoList = generateCommentDtoList(4);

        when(commentRepository.findAll()).thenReturn(commentList);
        when(commentConverter.convert(commentList)).thenReturn(commentDtoList);

        List<CommentDto> result = commentService.getAllComments();

        assertEquals(result,commentDtoList);

        verify(commentRepository).findAll();
        verify(commentConverter).convert(commentList);
    }

    @Test
    public void testGetByUserId_itShouldReturnCommentDtoList(){
        String userId="1";
        List<Comment> commentList = generateCommentList(4);
        List<CommentDto> commentDtoList = generateCommentDtoList(4);

        when(commentRepository.findByUser_id(userId)).thenReturn(commentList);
        when(commentConverter.convert(commentList)).thenReturn(commentDtoList);

        List<CommentDto> result = commentService.getByUserId(userId);

        assertEquals(result,commentDtoList);

        verify(commentRepository).findByUser_id(userId);
        verify(commentConverter).convert(commentList);
    }

    @Test
    public void testGetByEntryId_itShouldReturnCommentDtoList(){
        String entryId="1";
        List<Comment> commentList = generateCommentList(4);
        List<CommentDto> commentDtoList = generateCommentDtoList(4);

        when(commentRepository.findByEntry_id(entryId)).thenReturn(commentList);
        when(commentConverter.convert(commentList)).thenReturn(commentDtoList);

        List<CommentDto> result = commentService.getByEntryId(entryId);

        assertEquals(result,commentDtoList);

        verify(commentRepository).findByEntry_id(entryId);
        verify(commentConverter).convert(commentList);
    }

    @Test
    public void testGetByUserIdAndEntryId_itShouldReturnCommentDtoList(){
        String userId= "2";
        String entryId="1";
        List<Comment> commentList = generateCommentList(4);
        List<CommentDto> commentDtoList = generateCommentDtoList(4);

        when(commentRepository.findByUser_idAndEntry_id(userId,entryId)).thenReturn(commentList);
        when(commentConverter.convert(commentList)).thenReturn(commentDtoList);

        List<CommentDto> result = commentService.getByUserIdAndEntryId(userId,entryId);

        assertEquals(result,commentDtoList);

        verify(commentRepository).findByUser_idAndEntry_id(userId,entryId);
        verify(commentConverter).convert(commentList);
    }

    @Test
    public void testFindById_whenCommentExist_itShouldReturnComment(){
        String commentId = "1";
        Comment comment = generateComment(commentId,"1","2","comment",false);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(commentId);

        assertEquals(result,comment);

        verify(commentRepository).findById(commentId);
    }

    @Test
    public void testFindById_whenCommentNotExist_itShouldThrowCommentNotFoundException(){
        String commentId = "1";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,()->commentService.findById(commentId));

        verify(commentRepository).findById(commentId);
    }

    @Test
    public void testFindByUserId_itShouldReturnCommentList(){
        String userId="1";
        List<Comment> commentList = generateCommentList(4);

        when(commentRepository.findByUser_id(userId)).thenReturn(commentList);

        List<Comment> result = commentService.findByUserId(userId);

        assertEquals(result,commentList);

        verify(commentRepository).findByUser_id(userId);

    }

    @Test
    public void testFindByEntryId_itShouldReturnCommentList(){
        String entryId="1";
        List<Comment> commentList = generateCommentList(4);

        when(commentRepository.findByEntry_id(entryId)).thenReturn(commentList);

        List<Comment> result = commentService.findByEntryId(entryId);

        assertEquals(result,commentList);

        verify(commentRepository).findByEntry_id(entryId);

    }

    @Test
    public void testFindByUserIdAndEntryId_itShouldReturnCommentList(){
        String userId="2";
        String entryId="1";
        List<Comment> commentList = generateCommentList(4);

        when(commentRepository.findByUser_idAndEntry_id(userId,entryId)).thenReturn(commentList);

        List<Comment> result = commentService.findByUserIdAndEntryId(userId,entryId);

        assertEquals(result,commentList);

        verify(commentRepository).findByUser_idAndEntry_id(userId,entryId);

    }
}