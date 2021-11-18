package com.ebuozturk.blogapp.dto.entry

import com.ebuozturk.blogapp.dto.user.UserDto
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

data class EntryDto @JvmOverloads constructor(
    val id:String?="",
    val title:String,
    val content:String,
    val user: UserDto,
    val createdDate:LocalDateTime
): RepresentationModel<EntryDto>()
