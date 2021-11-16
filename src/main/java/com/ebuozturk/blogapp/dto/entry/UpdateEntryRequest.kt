package com.ebuozturk.blogapp.dto.entry

import javax.validation.constraints.NotEmpty

data class UpdateEntryRequest @JvmOverloads constructor(
    @field:NotEmpty
    val title:String,
    @field:NotEmpty
    val content:String,
    @field:NotEmpty
    val userId:String
){}
