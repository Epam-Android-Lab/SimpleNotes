package com.example.simplenotes.domain.entities

import java.util.*

data class Task(
    //id задачи
    val id: Int,

    //название задачи
    val title: String,

    //описание задачи
    val description: String?,

    //срок выполнения задачи (мс)
    val deadline: Long?,

    //стандартное напоминание (мс)
    val notification: Long?,

    //приоритет (1 - важно, 5 - не важно)
    val priority: Int = 3,

    //категория. id категории
    val category: String?,

    //статус выполнения задачи
    val status: Boolean,

    //время последнего редактирования (мс)
    val timeLastEdit: Long

)