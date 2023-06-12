package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.matrimony.db.entities.Habits
import com.example.matrimony.db.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HabitsViewModel @Inject constructor(private val habitsRepository: HabitsRepository) :
    ViewModel() {

    suspend fun insertHabit(habits: Habits) {
        habitsRepository.insertHabit(habits)
    }

    suspend fun getUserHabits(userId: Int): LiveData<Habits?> {
        return habitsRepository.getUserHabits(userId)
    }

    suspend fun updateHabits(userId:Int,drinking: String, smoking: String, foodType: String) {
        habitsRepository.updateHabits(userId,drinking, smoking, foodType)
    }


}