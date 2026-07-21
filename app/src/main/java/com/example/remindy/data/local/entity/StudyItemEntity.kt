package com.example.remindy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_items")
data class StudyItemEntity(
    @PrimaryKey val id: String,
    val kind: String,
    val prompt: String,
    val answer: String,
    val enabled: Boolean,
)
