package com.example.matrimony

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.*
import com.example.matrimony.db.repository.*
import com.example.matrimony.utils.getDateFromString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.logging.Handler
import javax.inject.Inject


const val BITMAP_TAG="bitmap"
@HiltViewModel
class InjectDataViewModel
@Inject
constructor(
    private val injectDataRepository: InjectDataRepository,
    private val userRepository: UserRepository,
    private val shortlistsRepository: ShortlistsRepository,
    private val connectionsRepository: ConnectionsRepository,
    private val partnerPreferenceRepository: PartnerPreferenceRepository,
    private val meetingsRepository: MeetingsRepository,
    private val habitsRepository: HabitsRepository,
    private val familyDetailsRepository: FamilyDetailsRepository,
    private val albumRepository: AlbumRepository,
    private val privacySettingsRepository: PrivacySettingsRepository,

) :
    ViewModel() {

    lateinit var context: Application

//    val applicationContext=context.applicationContext
//    val handler= android.os.Handler(Looper.getMainLooper())

    var initial=true
    val count=62

    suspend fun addAccount() {
        Log.i(TAG, "initAccount")

        val emailArray = arrayOf(
            "simbu@gmail.com",
            "anirudh@gmail.com",
            "gvp@gmail.com",
            "yuvan@gmail.com",
            "nani@gmail.com",
            "dulquer@gmail.com",
            "harish@gmail.com",
            "siva@gmail.com",
            "sidhu@gmail.com",
            "hrithik@gmail.com",
            "tovino@gmail.com",
            "dhanush@gmail.com",
            "atharva@gmail.com",
            "makapa@gmail.com",
            "ashok@gmail.com",
            "trisha@gmail.com",
            "megha@gmail.com",
            "sridivya@gmail.com",
            "anupama@gmail.com",
            "amy@gmail.com",
            "shreya@gmail.com",
            "nazriya@gmail.com",
            "priyankamohan@gmail.com",
            "priya@gmail.com",
            "vani@gmail.com",
            "shwetha@gmail.com",
            "samantha@gmail.com",
            "priyankadesh@gmail.com",
            "sai@gmail.com",
            "deepika@gmail.com",
            "keerthi@gmail.com",

            "simbu1@gmail.com",
            "anirudh1@gmail.com",
            "gvp1@gmail.com",
            "yuvan1@gmail.com",
            "nani1@gmail.com",
            "dulquer1@gmail.com",
            "harish1@gmail.com",
            "siva1@gmail.com",
            "sidhu1@gmail.com",
            "hrithik1@gmail.com",
            "tovino1@gmail.com",
            "dhanush1@gmail.com",
            "atharva1@gmail.com",
            "makapa1@gmail.com",
            "ashok1@gmail.com",
            "trisha1@gmail.com",
            "megha1@gmail.com",
            "sridivya1@gmail.com",
            "anupama1@gmail.com",
            "amy1@gmail.com",
            "shreya1@gmail.com",
            "nazriya1@gmail.com",
            "priyankamohan1@gmail.com",
            "priya1@gmail.com",
            "vani1@gmail.com",
            "shwetha1@gmail.com",
            "samantha1@gmail.com",
            "priyankadesh1@gmail.com",
            "sai1@gmail.com",
            "deepika1@gmail.com",
            "keerthi1@gmail.com",
        )
        val mobileArray = arrayOf(
            "8248471682",
            "9999999990",
            "9999999991",
            "9999999992",
            "9999999993",
            "9999999994",
            "9999999995",
            "9999999996",
            "9999999997",
            "9999999998",
            "9999999999",
            "9999999910",
            "9999999911",
            "9999999912",
            "9999999913",
            "9092731796",
            "9999999914",
            "9999999915",
            "9999999916",
            "9999999917",
            "9999999918",
            "9999999919",
            "9999999920",
            "9999999921",
            "9999999922",
            "9999999923",
            "9999999924",
            "9999999925",
            "9999999926",
            "9999999927",
            "9999999928",

            "9248471682",
            "8999999990",
            "8999999991",
            "8999999992",
            "8999999993",
            "8999999994",
            "8999999995",
            "8999999996",
            "8999999997",
            "8999999998",
            "8999999999",
            "8999999910",
            "8999999911",
            "8999999912",
            "8999999913",
            "8092731796",
            "8999999914",
            "8999999915",
            "8999999916",
            "8999999917",
            "8999999918",
            "8999999919",
            "8999999920",
            "8999999921",
            "8999999922",
            "8999999923",
            "8999999924",
            "8999999925",
            "8999999926",
            "8999999927",
            "8999999928",
        )
        val accountsArray = Array<Account>(count) {
            Account(email = emailArray[it], mobile_no = mobileArray[it], password = "Qwerty@123")
        }

//        viewModelScope.launch {

        accountsArray.forEach {
            injectDataRepository.addAccount(it)
        }

        for (i in 1..count) {
            privacySettingsRepository.addPrivacySettings(
                PrivacySettings(
                    i,
                    "Everyone",
//                    if(i%3==0)"Connections Only" else "Everyone",
                    if (i % 3 == 0) "Everyone" else "Connections Only",
                    "Everyone"
                )
            )
        }
//        }

        Log.i(TAG,"Accounts Loaded")
    }

    suspend fun addUsers() {
        val nameArr = arrayOf(
            "Silambarasan",
            "Anirudh",
            "Prakash",
            "Yuvan",
            "Nani",
            "Dulquer",
            "Harish",
            "Siva Kumar",
            "Siddharth",
            "Roshan",
            "Tovino",
            "Dhanush",
            "Atharva",
            "Anand",
            "Ashok",
            "Trisha",
            "Megha",
            "Divya",
            "Anupama",
            "Amy",
            "Shreya",
            "Nazriya",
            "Priyanka",
            "Priya Bhavani",
            "Vani",
            "Shwetha",
            "Samantha",
            "Priyanka",
            "Pallavi",
            "Deepika",
            "Keerthy",


            "Raj",
            "Ram",
            "Rishi",
            "Aarya",
            "Arun",
            "Karthick",
            "Kannan",
            "Kishore",
            "Vijay",
            "Vishwa",
            "Aravind",
            "Hariesh",
            "Ganesh",
            "Navin",
            "Ajay",
            "Aishwarya",
            "Ramya",
            "Aalia",
            "Rehsmi",
            "Preethi",
            "Anjali",
            "Vaishnavi",
            "Lavanya",
            "Shivani",
            "Shivanya",
            "Ankitha",
            "Rithika",
            "Aadhira",
            "Manju",
            "Aasha",
            "Kavya",
        )
        val genderArr = arrayOf(
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",

            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
        )

        val dobArr = arrayOf(
            getDateFromString("03/02/1983")!!,
            getDateFromString("16/10/1990")!!,
            getDateFromString("13/06/1987")!!,
            getDateFromString("31/08/1979")!!,
            getDateFromString("24/02/1982")!!,
            getDateFromString("28/07/1986")!!,
            getDateFromString("30/06/1990")!!,
            getDateFromString("17/02/1985")!!,
            getDateFromString("17/04/1979")!!,
            getDateFromString("10/01/1980")!!,
            getDateFromString("21/01/1989")!!,
            getDateFromString("28/07/1983")!!,
            getDateFromString("07/05/1989")!!,
            getDateFromString("20/05/1986")!!,
            getDateFromString("08/11/1989")!!,
            getDateFromString("04/05/1983")!!,
            getDateFromString("26/10/1995")!!,
            getDateFromString("01/04/1993")!!,
            getDateFromString("18/02/1996")!!,
            getDateFromString("31/01/1992")!!,
            getDateFromString("12/03/1984")!!,
            getDateFromString("20/12/1994")!!,
            getDateFromString("20/11/1994")!!,
            getDateFromString("31/12/1989")!!,
            getDateFromString("28/10/1988")!!,
            getDateFromString("19/11/1985")!!,
            getDateFromString("28/04/1987")!!,
            getDateFromString("28/04/1992")!!,
            getDateFromString("09/05/1992")!!,
            getDateFromString("05/01/1986")!!,
            getDateFromString("17/10/1992")!!,


            getDateFromString("16/10/1990")!!,
            getDateFromString("03/02/1983")!!,
            getDateFromString("31/08/1979")!!,
            getDateFromString("13/06/1987")!!,
            getDateFromString("28/07/1986")!!,
            getDateFromString("24/02/1982")!!,
            getDateFromString("17/02/1985")!!,
            getDateFromString("30/06/1990")!!,
            getDateFromString("10/01/1980")!!,
            getDateFromString("17/04/1979")!!,
            getDateFromString("28/07/1983")!!,
            getDateFromString("21/01/1989")!!,
            getDateFromString("20/05/1986")!!,
            getDateFromString("07/05/1989")!!,
            getDateFromString("04/05/1983")!!,
            getDateFromString("08/11/1989")!!,
            getDateFromString("01/04/1993")!!,
            getDateFromString("26/10/1995")!!,
            getDateFromString("31/01/1992")!!,
            getDateFromString("18/02/1996")!!,
            getDateFromString("20/12/1994")!!,
            getDateFromString("12/03/1984")!!,
            getDateFromString("31/12/1989")!!,
            getDateFromString("20/11/1994")!!,
            getDateFromString("19/11/1985")!!,
            getDateFromString("28/10/1988")!!,
            getDateFromString("28/04/1992")!!,
            getDateFromString("28/04/1987")!!,
            getDateFromString("05/01/1986")!!,
            getDateFromString("17/10/1992")!!,
            getDateFromString("09/05/1992")!!,
        )

//        val dobArr = Array<Date>(12) {
//            val localDate = LocalDate.of(2004 - it, 12 - it, 30 - it)
//            val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
//            Log.i(TAG, date.toString())
//            date
//        }
        val religionArr = arrayOf(
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Muslim",
            "Hindu",
            "Christian",
            "Atheism",
            "Muslim",
            "Christian",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Hindu",
            "Muslim",
            "Muslim",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Hindu",
            "Atheism",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Hindu",

            "Christian",
            "Hindu",
            "Muslim",
            "Hindu",
            "Muslim",
            "Atheism",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Hindu",
            "Christian",
            "Christian",
            "Hindu",
            "Hindu",
            "Hindu",
            "Muslim",
            "Muslim",
            "Christian",
            "Hindu",
            "Muslim",
            "Hindu",
            "Atheism",
            "Hindu",
            "Hindu",
            "Hindu",
            "Hindu",
            "Christian",
            "Atheism",
            "Hindu",
            "Muslim",
        )

        val casteArr = arrayOf(
            "Nadar",
            "Adventist",
            "Gounder",
            "Ansari",
            null,
            "Lebbai",
            "Sourashtra",
            "Roman Catholic",
            null,
            "Hanafi",
            "Baptist",
            "Others",
            "Others",
            "Latin Catholic",
            "Brahmin",
            "Chettiyar",
            "Malik",
            "Lebbai",
            "Gounder",
            "Unspecified",
            "Unspecified",
            "Unspecified",
            "Nadar",
            null,
            "Sourashtra",
            "Others",
            "Evangelist",
            "Gounder",
            "Lebbai",
            null,
            "Chettiyar",


            "Adventist",
            "Nadar",
            "Ansari",
            "Gounder",
            "Lebbai",
            null,
            "Roman Catholic",
            "Sourashtra",
            "Hanafi",
            null,
            "Others",
            "Baptist",
            "Latin Catholic",
            "Others",
            "Chettiyar",
            "Brahmin",
            "Lebbai",
            "Malik",
            "Unspecified",
            "Gounder",
            "Unspecified",
            "Unspecified",
            null,
            "Nadar",
            "Others",
            "Sourashtra",
            "Gounder",
            "Evangelist",
            null,
            "Chettiyar",
            "Lebbai",
        )
        val zodiacArr = arrayOf(
            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "Capricorn",
            "Aquarius",
            "None",
            "Cancer",
            "Leo",


            "Aries",
            "None",
            "Gemini",
            "Taurus",
            "Leo",
            "Cancer",
            "Libra",
            "Vigro",
            "Sagittarius",
            "Scorpio",
            "Aquarius",
            "Capricorn",
            "None",
            "Pisces",
            "Taurus",
            "Aries",
            "Cancer",
            "Gemini",
            "Vigro",
            "Leo",
            "Scorpio",
            "Libra",
            "Capricorn",
            "Sagittarius",
            "Pisces",
            "Aquarius",
            "Aquarius",
            "Capricorn",
            "Cancer",
            "Leo",
            "None",
        )
        val starArr = arrayOf(
            "None",
            "Ashwini",
            "Bharani",
            "Krittika",
            "Rohini",
            "Mrigashira",
            "Ardra",
            "Punarvasu",
            "Pushya",
            "Ashlesha",
            "Magha",
            "Purva Phalguni",
            "Uttara Phalguni",
            "Hasta",
            "Chitra",
            "Swati",
            "Vishakha",
            "Anuradha",
            "Jyeshtha",
            "Mula",
            "Purva Ashadha",
            "Uttara Ashadha",
            "Shravana",
            "Shravana",
            "Dhanishta",
            "Shatabhisha",
            "Purva Bhadrapada",
            "Uttara Bhadrapada",
            "Uttara Bhadrapada",
            "Revati",
            "Revati",


            "Ashwini",
            "None",
            "Krittika",
            "Bharani",
            "Mrigashira",
            "Rohini",
            "Punarvasu",
            "Ardra",
            "Ashlesha",
            "Pushya",
            "Purva Phalguni",
            "Magha",
            "Hasta",
            "Uttara Phalguni",
            "Swati",
            "Chitra",
            "Anuradha",
            "Vishakha",
            "Mula",
            "Jyeshtha",
            "Uttara Ashadha",
            "Purva Ashadha",
            "Shravana",
            "Dhanishta",
            "Shatabhisha",
            "Shravana",
            "Uttara Bhadrapada",
            "Purva Bhadrapada",
            "Revati",
            "Revati",
            "Uttara Bhadrapada",
        )

        val stateArray = arrayOf(
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Tamilnadu",


            "Karnataka",
            "Andhra Pradesh",
            "Tamilnadu",
            "Kerala",
            "Andhra Pradesh",
            "Others",
            "Kerala",
            "Karnataka",
            "Others",
            "Tamilnadu",
            "Karnataka",
            "Andhra Pradesh",
            "Tamilnadu",
            "Kerala",
            "Andhra Pradesh",
            "Others",
            "Kerala",
            "Karnataka",
            "Others",
            "Tamilnadu",
            "Karnataka",
            "Andhra Pradesh",
            "Tamilnadu",
            "Kerala",
            "Andhra Pradesh",
            "Others",
            "Kerala",
            "Karnataka",
            "Others",
            "Tamilnadu",
            "Tamilnadu",
        )

        val cityArray = arrayOf(
            "Amaravati",
            "Bangalore",
            "Aluva",
            "Chennai",
            null,
            "Guntur",
            "Hubbali",
            "Cochin",
            "Coimbatore",
            null,
            "Nellore",
            "Mangaluru",
            "Kollam",
            "Madurai",
            null,
            "Tirupati",
            "Mysore",
            "Kozhikode",
            "Ooty",
            null,
            "Visakhapatnam",
            "Udupi",
            "Tiruvanandapuram",
            "Nagercoil",
            null,
            "Others",
            "Others",
            "Others",
            "Others",
            null,
            "Chennai",

            "Bangalore",
            "Amaravati",
            "Chennai",
            "Aluva",
            "Guntur",
            null,
            "Cochin",
            "Hubbali",
            null,
            "Coimbatore",
            "Mangaluru",
            "Nellore",
            "Madurai",
            "Kollam",
            "Tirupati",
            null,
            "Kozhikode",
            "Mysore",
            null,
            "Ooty",
            "Udupi",
            "Amaravati",
            "Nagercoil",
            "Tiruvanandapuram",
            "Amaravati",
            null,
            "Others",
            "Others",
            null,
            "Others",
            "Chennai",
        )
        val heightArray = arrayOf(
            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",
            "5 ft 6 in",
            "5 ft 7 in",
            "5 ft 8 in",
            "5 ft 9 in",
            "5 ft 10 in",
            "5 ft 11 in",
            "6 ft",
            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",


            "4 ft 7 in",
            "4 ft 6 in",
            "4 ft 9 in",
            "4 ft 8 in",
            "4 ft 11 in",
            "4 ft 10 in",
            "5 ft 1 in",
            "5 ft",
            "5 ft 3 in",
            "5 ft 2 in",
            "5 ft 5 in",
            "5 ft 4 in",
            "5 ft 7 in",
            "5 ft 6 in",
            "5 ft 9 in",
            "5 ft 8 in",
            "5 ft 11 in",
            "5 ft 10 in",
            "4 ft 6 in",
            "6 ft",
            "4 ft 8 in",
            "4 ft 7 in",
            "4 ft 10 in",
            "4 ft 9 in",
            "5 ft",
            "4 ft 11 in",
            "5 ft 2 in",
            "5 ft 1 in",
            "5 ft 4 in",
            "5 ft 5 in",
            "5 ft 3 in",
        )

        val educationArray = arrayOf(
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "MBBS",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "MBBS",
            "B.E",
            "MBBS",


            "B.Tech",
            "B.E",
            "B.Arch",
            "MBBS",
            "B.E",
            "B.Sc",
            "MBBS",
            "B.Tech",
            "B.Sc",
            "Others",
            "B.Arch",
            "Others",
            "MBBS",
            "B.E",
            "B.Arch",
            "B.Tech",
            "B.E",
            "B.Sc",
            "MBBS",
            "B.Tech",
            "B.Sc",
            "B.Arch",
            "Others",
            "Others",
            "B.Tech",
            "B.E",
            "MBBS",
            "B.Sc",
            "MBBS",
            "B.Arch",
            "B.E",
        )

        val employedInArray=Array(count){
            if(it%2==0) "Government"
            else "Private"
        }

        val occupationArray = arrayOf(
            "Accountant",
            "Entrepreneur",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Doctor",
            "Supervisor",
            "Technician",
            "Others",
            "Accountant",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Technician",
            "Doctor",
            "Accountant",
            "Others",
            "Entrepreneur",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Doctor",
            "Others",
            "Doctor",


            "Entrepreneur",
            "Accountant",
            "Manager",
            "Doctor",
            "Software Professional",
            "Marketing Professional",
            "Doctor",
            "Studying",
            "Technician",
            "Supervisor",
            "Accountant",
            "Others",
            "Manager",
            "Doctor",
            "Software Professional",
            "Marketing Professional",
            "Supervisor",
            "Studying",
            "Doctor",
            "Technician",
            "Others",
            "Accountant",
            "Manager",
            "Entrepreneur",
            "Software Professional",
            "Marketing Professional",
            "Supervisor",
            "Doctor",
            "Studying",
            "Others",
            "Doctor",
        )
        val annualIncomeArray = arrayOf(
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",


            "4-5 Lakhs",
            "3-4 Lakhs",
            "6-7 Lakhs",
            "5-6 Lakhs",
            "8-9 Lakhs",
            "7-8 Lakhs",
            "Above 10 Lakhs",
            "9-10 Lakhs",
            "4-5 Lakhs",
            "3-4 Lakhs",
            "6-7 Lakhs",
            "5-6 Lakhs",
            "8-9 Lakhs",
            "7-8 Lakhs",
            "Above 10 Lakhs",
            "9-10 Lakhs",
            "4-5 Lakhs",
            "3-4 Lakhs",
            "6-7 Lakhs",
            "5-6 Lakhs",
            "8-9 Lakhs",
            "7-8 Lakhs",
            "Above 10 Lakhs",
            "9-10 Lakhs",
            "4-5 Lakhs",
            "3-4 Lakhs",
            "6-7 Lakhs",
            "5-6 Lakhs",
            "Above 10 Lakhs",
            "9-10 Lakhs",
            "7-8 Lakhs",
        )

        val familyTypeArray =Array<String>(count){
            if (it%2==0) "Joint" else "Nuclear"
        }



//        handler.post {
//        Toast.makeText(context.applicationContext,"Before Loading Images1",Toast.LENGTH_SHORT).show()
//        }

        val options=BitmapFactory.Options()
        options.inJustDecodeBounds=false
        options.inSampleSize=calculateInSampleSize(options,500,500)

        val pic1=BitmapFactory.decodeResource(context.resources, R.drawable.simbu1,options)
        val pic2=BitmapFactory.decodeResource(context.resources, R.drawable.anirudh1,options)
        val pic3=BitmapFactory.decodeResource(context.resources, R.drawable.gvp1,options)
        val pic4=BitmapFactory.decodeResource(context.resources, R.drawable.yuvan1,options)
        val pic5=BitmapFactory.decodeResource(context.resources, R.drawable.nani1,options)
        val pic6=BitmapFactory.decodeResource(context.resources, R.drawable.dulquer1,options)
        val pic7=BitmapFactory.decodeResource(context.resources, R.drawable.harish1,options)
        val pic8=BitmapFactory.decodeResource(context.resources, R.drawable.siva1,options)
        val pic9=BitmapFactory.decodeResource(context.resources, R.drawable.siddharth1,options)
        val pic10=BitmapFactory.decodeResource(context.resources, R.drawable.hirthik1,options)
        val pic11=BitmapFactory.decodeResource(context.resources, R.drawable.tovino1,options)
        val pic12=BitmapFactory.decodeResource(context.resources, R.drawable.dhanush1,options)
        val pic13=BitmapFactory.decodeResource(context.resources, R.drawable.adharva1,options)
        val pic14=BitmapFactory.decodeResource(context.resources, R.drawable.makapa1,options)
        val pic15=BitmapFactory.decodeResource(context.resources, R.drawable.ashok1,options)

        val pic16=BitmapFactory.decodeResource(context.resources, R.drawable.trisha1,options)
        val pic17=BitmapFactory.decodeResource(context.resources, R.drawable.megha1,options)
        val pic18=BitmapFactory.decodeResource(context.resources, R.drawable.sri1,options)
        val pic19=BitmapFactory.decodeResource(context.resources, R.drawable.anupama1,options)
        val pic20=BitmapFactory.decodeResource(context.resources, R.drawable.amy1,options)
        val pic21=BitmapFactory.decodeResource(context.resources, R.drawable.shreya1,options)
        val pic22=BitmapFactory.decodeResource(context.resources, R.drawable.nazriya1,options)
        val pic23=BitmapFactory.decodeResource(context.resources, R.drawable.priyankamohan1,options)
        val pic24=BitmapFactory.decodeResource(context.resources, R.drawable.priyabhavani1,options)
        val pic25=BitmapFactory.decodeResource(context.resources, R.drawable.vani1,options)
        val pic26=BitmapFactory.decodeResource(context.resources, R.drawable.shwetha1,options)
        val pic27=BitmapFactory.decodeResource(context.resources, R.drawable.samantha1,options)
        val pic28=BitmapFactory.decodeResource(context.resources, R.drawable.priyanka1,options)
        val pic29=BitmapFactory.decodeResource(context.resources, R.drawable.pallavi1,options)
        val pic30=BitmapFactory.decodeResource(context.resources, R.drawable.deepika1,options)
        val pic31=BitmapFactory.decodeResource(context.resources, R.drawable.keerthy1,options)


        val profilePicArray = arrayOf(
            pic1,
            pic2,
            pic3,
            pic4,
            pic5,
            pic6,
            pic7,
            pic8,
            pic9,
            pic10,
            pic11,
            pic12,
            pic13,
            pic14,
            pic15,

            pic16,
            pic17,
            pic18,
            pic19,
            pic20,
            pic21,
            pic22,
            pic23,
            pic24,
            pic25,
            pic26,
            pic27,
            pic28,
            pic29,
            pic30,
            pic31,

            pic1,
            pic2,
            pic3,
            pic4,
            pic5,
            pic6,
            pic7,
            pic8,
            pic9,
            pic10,
            pic11,
            pic12,
            pic13,
            pic14,
            pic15,

            pic16,
            pic17,
            pic18,
            pic19,
            pic20,
            pic21,
            pic22,
            pic23,
            pic24,
            pic25,
            pic26,
            pic27,
            pic28,
            pic29,
            pic30,
            pic31,
        )


        for (i in 1..count) {
            albumRepository.addAlbum(Album(0, i, profilePicArray[i - 1], true))
        }
        addAlbum()

        val userArray = Array<User>(count) {
            User(
//                0,
                name=nameArr[it],
                gender=genderArr[it],
                dob=dobArr[it],
                religion = religionArr[it],
                mother_tongue = "English",
                marital_status = "Never Married",
                no_of_children = null,
                caste = casteArr[it],
                zodiac = zodiacArr[it],
                star = starArr[it],
                country = "India",
                state = stateArray[it],
                city = cityArray[it],
                height = heightArray[it],
//                BitmapFactory.decodeResource(context.resources, R.drawable.default_profile_pic),
                profile_pic = profilePicArray[it],
                physical_status = "Normal",
                education = educationArray[it],
                employed_in = employedInArray[it],
                occupation = occupationArray[it],
                annual_income = annualIncomeArray[it],
                family_status = "Middle Class",
                family_type = familyTypeArray[it],
                about = "I am a software engineer. I am currently living in Chennai. I come from a middle class, nuclear family background."
            )
        }


        userArray.forEach {
            Log.i(TAG, it.toString())
            injectDataRepository.addUser(it)
        }

        Log.i(TAG,"Users Loaded")

//        handler.post {
////            Toast.makeText(context.applicationContext,"Loading Complete",Toast.LENGTH_SHORT).show()
//        }
    }

    private suspend fun addAlbum() {
        val options=BitmapFactory.Options()
        options.inJustDecodeBounds=false
        options.inSampleSize=calculateInSampleSize(options,500,500)
        albumRepository.addAlbum(Album(0, 1, BitmapFactory.decodeResource(context.resources, R.drawable.simbu2,options)))
        albumRepository.addAlbum(Album(0, 2, BitmapFactory.decodeResource(context.resources, R.drawable.anirudh2,options)))
        albumRepository.addAlbum(Album(0, 3, BitmapFactory.decodeResource(context.resources, R.drawable.gvp2,options)))
        albumRepository.addAlbum(Album(0, 4, BitmapFactory.decodeResource(context.resources, R.drawable.yuvan2,options)))
        albumRepository.addAlbum(Album(0, 5, BitmapFactory.decodeResource(context.resources, R.drawable.nani2,options)))
        albumRepository.addAlbum(Album(0, 10, BitmapFactory.decodeResource(context.resources, R.drawable.hirthik2,options)))
        albumRepository.addAlbum(Album(0, 13, BitmapFactory.decodeResource(context.resources, R.drawable.atharva2,options)))

        albumRepository.addAlbum(Album(0, 16, BitmapFactory.decodeResource(context.resources, R.drawable.trisha2,options)))
        albumRepository.addAlbum(Album(0, 17, BitmapFactory.decodeResource(context.resources, R.drawable.megha2,options)))
        albumRepository.addAlbum(Album(0, 18, BitmapFactory.decodeResource(context.resources, R.drawable.sri2,options)))
        albumRepository.addAlbum(Album(0, 19, BitmapFactory.decodeResource(context.resources, R.drawable.anupama2,options)))
        albumRepository.addAlbum(Album(0, 20, BitmapFactory.decodeResource(context.resources, R.drawable.amy2,options)))
        albumRepository.addAlbum(Album(0, 26, BitmapFactory.decodeResource(context.resources, R.drawable.shwetha2,options)))
        albumRepository.addAlbum(Album(0, 30, BitmapFactory.decodeResource(context.resources, R.drawable.deepika2,options)))
    }

    suspend fun addHabits() {
        val habitsArray = mutableListOf<Habits>()

        for (i in 1..count) {
//            habitsArray.add(Habits(1,"Never","Occasionally","Non-Vegetarian"))
            habitsRepository.insertHabit(Habits(i, "Never", "Occasionally", "Non-Vegetarian"))
        }

        Log.i(TAG,"Habits Loaded")

    }

    suspend fun addFamilyDetails() {
//        val habitsArray = mutableListOf<Habits>()

        for (i in 1..count) {
//            habitsArray.add(Habits(1,"Never","Occasionally","Non-Vegetarian"))
            familyDetailsRepository.setFamilyDetails(
                FamilyDetails(
                    i,
                    "John Abraham",
                    "Jennifer Lawrence",
                    1,
                    0,
                    2,
                    1
                )
            )
//            habitsRepository.insertHabit(Habits(i, "Never", "Occasionally", "Non-Vegetarian"))
        }
//        addSuccessStories()

        Log.i(TAG,"Family Details Loaded")
        addShortlists()
        addConnections()
        addMeetings()
        addPartnerPreferences()
    }


    private suspend fun addShortlists(){
        val shortlistsArray= arrayOf(
            Shortlists(user_id = 1, shortlisted_user_id = 16),
            Shortlists(user_id = 1, shortlisted_user_id = 17),
            Shortlists(user_id = 1, shortlisted_user_id = 18),
            Shortlists(user_id = 1, shortlisted_user_id = 19),
            Shortlists(user_id = 1, shortlisted_user_id = 20),
            Shortlists(user_id = 1, shortlisted_user_id = 21),
            Shortlists(user_id = 1, shortlisted_user_id = 22),
            Shortlists(user_id = 1, shortlisted_user_id = 23),
            Shortlists(user_id = 1, shortlisted_user_id = 24),
            Shortlists(user_id = 1, shortlisted_user_id = 25),
            Shortlists(user_id = 16, shortlisted_user_id = 1),
            Shortlists(user_id = 16, shortlisted_user_id = 2),
            Shortlists(user_id = 16, shortlisted_user_id = 3),
            Shortlists(user_id = 16, shortlisted_user_id = 4),
            Shortlists(user_id = 16, shortlisted_user_id = 5),
            Shortlists(user_id = 16, shortlisted_user_id = 6),
            Shortlists(user_id = 16, shortlisted_user_id = 7),
            Shortlists(user_id = 16, shortlisted_user_id = 8),
            Shortlists(user_id = 16, shortlisted_user_id = 9),
            Shortlists(user_id = 16, shortlisted_user_id = 10),
        )

        shortlistsArray.forEach {
            shortlistsRepository.addShortlist(it)
        }
    }

    private suspend fun addConnections(){
        val connectionsArray= arrayOf(
            Connections(user_id = 16, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 17, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 18, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 19, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 20, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 21, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 22, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 23, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 24, connected_user_id = 1, status = "REQUESTED"),
            Connections(user_id = 25, connected_user_id = 1, status = "REQUESTED"),

            Connections(user_id = 26, connected_user_id = 1, status = "CONNECTED"),
            Connections(user_id = 27, connected_user_id = 1, status = "CONNECTED"),
            Connections(user_id = 28, connected_user_id = 1, status = "CONNECTED"),
            Connections(user_id = 30, connected_user_id = 1, status = "CONNECTED"),
            Connections(user_id = 29, connected_user_id = 1, status = "CONNECTED"),


            Connections(user_id = 1, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 2, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 3, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 4, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 5, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 6, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 7, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 8, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 9, connected_user_id = 16, status = "REQUESTED"),
            Connections(user_id = 10, connected_user_id = 16, status = "REQUESTED"),

            Connections(user_id = 11, connected_user_id = 16, status = "CONNECTED"),
            Connections(user_id = 12, connected_user_id = 16, status = "CONNECTED"),
            Connections(user_id = 13, connected_user_id = 16, status = "CONNECTED"),
            Connections(user_id = 14, connected_user_id = 16, status = "CONNECTED"),
            Connections(user_id = 15, connected_user_id = 16, status = "CONNECTED"),
        )
        connectionsArray.forEach {
            connectionsRepository.addConnection(it)
        }
    }

    private suspend fun addMeetings(){
        val meetingsArray= arrayOf(
            Meetings(sender_user_id =26 , receiver_user_id =1 , title ="New Meet 1" , date = getDateFromString("05/06/2023")!!, time = "18:30", place = "GreenPark", status = "UPCOMING"),
            Meetings(sender_user_id =28 , receiver_user_id =1 , title ="New Meet 2" , date = getDateFromString("06/06/2023")!!, time = "17:30", place = "Paris", status = "UPCOMING"),
            Meetings(sender_user_id =27 , receiver_user_id =1 , title ="New Meet 3" , date = getDateFromString("07/06/2023")!!, time = "16:30", place = "Skywalk", status = "UPCOMING"),
            Meetings(sender_user_id =30 , receiver_user_id =1 , title ="New Meet 4" , date = getDateFromString("08/06/2023")!!, time = "15:30", place = "Phoenix", status = "COMPLETED"),
            Meetings(sender_user_id =29 , receiver_user_id =1 , title ="New Meet 5" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "CANCELLED"),

            Meetings(sender_user_id =11 , receiver_user_id =16 , title ="New Meet 6" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "UPCOMING"),
            Meetings(sender_user_id =12 , receiver_user_id =16 , title ="New Meet 7" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "UPCOMING"),
            Meetings(sender_user_id =13 , receiver_user_id =16 , title ="New Meet 8" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "UPCOMING"),
            Meetings(sender_user_id =14 , receiver_user_id =16 , title ="New Meet 9" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "COMPLETED"),
            Meetings(sender_user_id =15 , receiver_user_id =16 , title ="New Meet 10" , date = getDateFromString("20/06/2023")!!, time = "14:30", place = "Nexus", status = "CANCELLED"),
        )
        meetingsArray.forEach {
            meetingsRepository.addNewMeeting(it)
        }
    }

    private suspend fun addPartnerPreferences(){
        val preferenceArray= arrayOf(
            PartnerPreferences(user_id = 1, state = "Tamilnadu"),
            PartnerPreferences(user_id = 2, state = "Kerala"),
            PartnerPreferences(user_id = 3, education = listOf("B.E")),
            PartnerPreferences(user_id = 4, state = "Andhra Pradesh"),
            PartnerPreferences(user_id = 5, state = "Karnataka"),
            PartnerPreferences(user_id = 6, state = "Others"),
            PartnerPreferences(user_id = 7, religion = "Muslim"),
            PartnerPreferences(user_id = 8, religion = "Hindu"),
            PartnerPreferences(user_id = 9, religion = "Christian"),
            PartnerPreferences(user_id = 10, religion = "Atheism"),

            PartnerPreferences(user_id = 16, state = "Tamilnadu"),
            PartnerPreferences(user_id = 17, state = "Kerala"),
            PartnerPreferences(user_id = 18, education = listOf("B.E")),
            PartnerPreferences(user_id = 19, state = "Andhra Pradesh"),
            PartnerPreferences(user_id = 20, state = "Karnataka"),
            PartnerPreferences(user_id = 21, state = "Others"),
            PartnerPreferences(user_id = 22, religion = "Muslim"),
            PartnerPreferences(user_id = 23, religion = "Hindu"),
            PartnerPreferences(user_id = 24, religion = "Christian"),
            PartnerPreferences(user_id = 25, religion = "Atheism"),
        )

        preferenceArray.forEach {
            partnerPreferenceRepository.addPreference(it)
        }
    }


    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }




}