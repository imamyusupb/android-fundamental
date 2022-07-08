package com.codeyuba.bfaa3submission.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
open class UserEntity(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @field:ColumnInfo(name = "isFavorites")
    var isFavorites: Boolean
)