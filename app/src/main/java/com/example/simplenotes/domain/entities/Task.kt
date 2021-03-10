package com.example.simplenotes.domain.entities

import java.util.*

data class Task (
    //id задачи
    var id: String,

    //название задачи
    var title: String,

    //описание задачи
    var description: String?,

    //срок выполнения задачи (мс)
    var deadline: Long?,

    //стандартное напоминание (мс)
    var notification: Long?,

    //приоритет (1 - не важно, 5 - важно)
    var priority: Int = 3,

    //категория. id категории
    var category: String?,

    //статус выполнения задачи
    var status: Boolean,

    //время последнего редактирования (мс)
    var timeLastEdit: Long

) {
    constructor() : this(
        id = "",
        title = "",
        description = "",
        deadline = 0,
        notification = 0,
        priority = 0,
        category = "",
        status = false,
        timeLastEdit = 0
    )

}