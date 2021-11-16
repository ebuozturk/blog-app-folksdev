package com.ebuozturk.blogapp.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
data class Comment @JvmOverloads constructor(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    val id:String?="",
    val comment:String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id",referencedColumnName = "id")
    val entry: Entry,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    val user:User,

    val isUpdated:Boolean

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (id!= null && id != other.id) return false
        if (comment != other.comment) return false
        if (entry != other.entry) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + comment.hashCode()
        result = 31 * result + entry.id.hashCode()
        result = 31 * result + user.id.hashCode()
        return result
    }
}
