package com.example.expensare.data.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "avatar")
data class AvatarEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "avatar_uri")
    val avatar: Uri,
    @ColumnInfo(name = "from_avatar_picker")
    val fromAvatarPicker: Boolean
    ) : Serializable {
    companion object {
        val EMPTY: AvatarEntity = AvatarEntity(
            0,
            Uri.EMPTY,
            false
        )
    }
}
