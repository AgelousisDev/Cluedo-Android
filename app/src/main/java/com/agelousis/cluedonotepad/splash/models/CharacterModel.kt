package com.agelousis.cluedonotepad.splash.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.dialog.enumerations.Character
import com.agelousis.cluedonotepad.stats.models.StatsModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterModel(val characterNameHint: String,
                          var characterName: String? = null,
                          var character: Int? = null,
                          @DrawableRes var characterIcon: Int = R.drawable.ic_person,
                          var characterEnum: Character? = null) : Parcelable {

    @IgnoredOnParcel
    var playerIsSelected = false

    val statsModel: StatsModel
        get() = StatsModel(playerName = characterName ?: "", playerColor = character ?: 0x000000, playerScore = 0)

}