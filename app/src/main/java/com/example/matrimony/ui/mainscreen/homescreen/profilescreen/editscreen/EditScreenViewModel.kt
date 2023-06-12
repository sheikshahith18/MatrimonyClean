package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen

import androidx.lifecycle.ViewModel

class EditScreenViewModel:ViewModel() {
    var about=""
    var name=""
    var dob=""
    var height=""
    var physicalStatus=""
    var maritalStatus=""
    var drinking=""
    var smoking=""
    var foodType=""
    var hobbies= sortedSetOf<String>()
    var fathersName=""
    var mothersName=""
    var noOfBrothers=""
    var noOfSisters=""
    var marriedBrothers=""
    var marriedSisters=""
    var familyStatus=""
    var familyType=""
    var familyState=""
    var familyCity=""


    var loaded=true

}