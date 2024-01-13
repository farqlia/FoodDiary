package com.example.fooddiary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fooddiary.R
import java.io.Serializable

enum class Category {
    DINNER, BREAKFAST, LUNCH, NONE
}

@Entity(tableName="items")
class Item : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name="title")
    var title: String = "Title"

    @ColumnInfo(name="drawableResource")
    var drawableResource: Int = 0

    @ColumnInfo(name="placeName")
    var placeName: String = "Place"

    @ColumnInfo(name="category")
    var category: Category = Category.NONE

    @ColumnInfo(name="satisfaction")
    var satisfaction: Float = 2.0f

    @ColumnInfo(name="petsFriendly")
    var isPetFriendly: Boolean = false

    constructor()

    constructor(title: String = "Dinner at luxury restaurant",
                drawableResource: Int = R.drawable.baseline_food_bank_24,
                placeName: String = "Saint Paul Square",
                category: Category = Category.DINNER,
                satisfaction: Float? = 0.0f,
                petsFriendly: Boolean? = false){
        this.title = title
        this.drawableResource = drawableResource
        this.placeName = placeName
        this.category = category
        this.satisfaction = satisfaction ?: 0.0f
        this.isPetFriendly = petsFriendly ?: false
    }


}