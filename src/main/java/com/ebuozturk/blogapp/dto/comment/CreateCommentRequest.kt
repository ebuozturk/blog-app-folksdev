package com.ebuozturk.blogapp.dto.comment

import javax.validation.constraints.NotEmpty

data class CreateCommentRequest @JvmOverloads constructor(
    @field:NotEmpty
    val comment:String,
    @field:NotEmpty
    val userId:String,
    @field:NotEmpty
    val entryId:String
){

}
