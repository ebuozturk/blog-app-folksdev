package com.ebuozturk.blogapp.repository;

import com.ebuozturk.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {
}
