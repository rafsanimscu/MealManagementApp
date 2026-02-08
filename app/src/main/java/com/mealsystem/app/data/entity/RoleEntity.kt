package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey(autoGenerate = true) val roleId: Int = 0,
    val roleName: String, // e.g., "Admin", "Member"
    val permissions: String // JSON string e.g. '{"canEditUsers":true}'
)