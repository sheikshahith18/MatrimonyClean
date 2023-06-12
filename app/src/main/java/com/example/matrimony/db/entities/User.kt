package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.*
import com.example.matrimony.db.converters.BitmapConverter
import com.example.matrimony.db.converters.DateConverter
import com.example.matrimony.utils.calculateAge
import java.util.Date


@Entity(
    tableName = "user",
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var user_id: Int = 0,
    var name: String,
    val gender: String,
    @TypeConverters(DateConverter::class) var dob: Date,
    var religion: String,
    var mother_tongue: String,
    var marital_status: String = "Never Married",
    var no_of_children: Int? = null,
    var caste: String? = null,
    var zodiac: String = "None",
    var star: String = "None",
    var country: String = "India",
    var state: String,
    var city: String? = null,
    var height: String = "Not Set",
    @TypeConverters(BitmapConverter::class)
    var profile_pic: Bitmap?,
    var physical_status: String = "Not Set",
    var education: String = "Not Set",
    var employed_in: String = "Not Set",
    var occupation: String = "Not Set",
    var annual_income: String = "Not Set",
    var family_status: String = "Not Set",
    var family_type: String = "Not Set",
    var about: String = "Not Set"
) {

    @ColumnInfo(name="age")
    var age: Int=0
        get() = calculateAge(dob)
}
