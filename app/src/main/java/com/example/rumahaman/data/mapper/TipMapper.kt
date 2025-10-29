package com.example.rumahaman.data.mapper

import com.example.rumahaman.data.remote.firebase.dto.TipDto
import com.example.rumahaman.domain.model.Tip

fun TipDto.toDomain(): Tip {
    return Tip(        id = this.id,
        title = this.title,
        type = this.type,
        link = this.link
    )
}
