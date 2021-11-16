package com.ebuozturk.blogapp.repository;

import com.ebuozturk.blogapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,String> {

    List<Comment> findByUser_id(String id);
    List<Comment> findByEntry_id(String id);
    List<Comment> findByUser_idAndEntry_id(String userId,String entryId);
}
