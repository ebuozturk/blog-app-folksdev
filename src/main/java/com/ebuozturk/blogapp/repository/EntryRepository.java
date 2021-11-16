package com.ebuozturk.blogapp.repository;

import com.ebuozturk.blogapp.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry,String> {
    List<Entry> findByUser_id(String id);

}
