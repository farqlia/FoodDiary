package com.example.fooddiary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fooddiary.R
import java.io.Serializable

enum class Category {
    DINNER, BREAKFAST, LUNCH, DRINKS, DESSERT, NONE
}

@Entity(tableName="item_table")
class DBItem : Serializable{
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
    var satisfaction: Float = 2.0F

    @ColumnInfo(name="atmosphere")
    var atmosphere: Int = 0

    @ColumnInfo(name="englishSupport")
    var englishSupport: Boolean = false

    @ColumnInfo(name="petsFriendly")
    var petsFriendly: Boolean = false

    constructor()

    constructor(title: String = "Dinner at luxury restaurant",
                drawableResource: Int = R.drawable.baseline_food_bank_24,
                placeName: String = "Saint Paul Square",
                category: Category = Category.DINNER,
                satisfaction: Float? = 0.0F,
                atmosphere: Int? = 0,
                englishSupport: Boolean? = false,
                petsFriendly: Boolean? = false){
        this.title = title
        this.drawableResource = drawableResource
        this.placeName = placeName
        this.category = category
        this.satisfaction = satisfaction ?: 0.0F
        this.atmosphere = atmosphere ?: 0
        this.englishSupport = englishSupport ?: false
        this.petsFriendly = petsFriendly ?: false
    }


}