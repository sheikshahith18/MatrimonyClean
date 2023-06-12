package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase

import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetShortlistedProfilesUseCase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.RemoveShortlistUseCase

data class ShortlistsUseCases(
    val getShortlistedProfilesUseCase: GetShortlistedProfilesUseCase,
    val removeShortlistUseCase: RemoveShortlistUseCase
)