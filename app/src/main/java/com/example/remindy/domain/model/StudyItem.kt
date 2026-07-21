package com.example.remindy.domain.model

enum class StudyItemKind { QA, TERM }

data class StudyItem(
    val id: String,
    val kind: StudyItemKind,
    val prompt: String,
    val answer: String,
    val enabled: Boolean,
)
