package com.ebuozturk.blogapp.dto.comment

import com.ebuozturk.blogapp.dto.entry.EntryDto
import com.ebuozturk.blogapp.dto.user.UserDto
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel

data class CommentDto @JvmOverloads constructor(
    val id:String?="",
    val comment:String,
    val user: UserDto,
    val entry:EntryDto,
    val isUpdated:Boolean
): RepresentationModel<CommentDto>()
