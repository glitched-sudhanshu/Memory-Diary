package com.example.memodiary.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//@Parcelize to pass objects over other screens easily
//@Entity means we are marking this as an entity (database vala theory)
//then we are giving the table name
@Parcelize
@Entity(tableName = "memo_diary_table")
data class MemoDiary(
    //then we are defining various columns
    //@ColumnInfo(<column_name>) val <variable_name> : String,
    @ColumnInfo val image : String,
    @ColumnInfo val imageSource : String,
    @ColumnInfo val title : String,
    @ColumnInfo val type : String,
    @ColumnInfo val date : String,
    @ColumnInfo val peopleInvolved : String,
    @ColumnInfo (name = "time_of_adding") val time : String,
    @ColumnInfo (name = "description_of_memory") val description : String,
    @ColumnInfo (name = "favourite_memory") val favouriteMemory : Boolean =  false,
    //this line means that primary key column would be automatically generated for us. And the id entry would automatically incremented whenever a new entry is introduced
    @PrimaryKey(autoGenerate = true) val id: Int = 0
//Parcelable to pass objects over other screens easily
):Parcelable