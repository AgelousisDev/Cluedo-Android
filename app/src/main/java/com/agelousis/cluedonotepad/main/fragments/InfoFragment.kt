package com.agelousis.cluedonotepad.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.databinding.InfoFragmentLayoutBinding
import com.agelousis.cluedonotepad.extensions.applyLightScaleAnimation
import com.agelousis.cluedonotepad.extensions.makeSoundNotification
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.main.models.InfoModel
import com.agelousis.cluedonotepad.main.timer.TimerHelper
import com.agelousis.cluedonotepad.main.timer.TimerListener
import kotlinx.android.synthetic.main.info_fragment_layout.*

class InfoFragment: Fragment(), TimerListener {

    override fun onTimeUpdate(time: String) {
        infoModel.timerValue.set(time)
    }

    override fun onEveryMinute() {
        context?.makeSoundNotification()
    }

    private val infoModel by lazy {
        InfoModel(
            title = (activity as? NotePadActivity)?.gameTypeModel?.channel ?: resources.getString(R.string.app_name),
            ObservableField()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        InfoFragmentLayoutBinding.inflate(
            inflater,
            container,
            false
        ).also {
            it.infoModel = infoModel
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        configureTimer()
    }

    private fun setupUI() {
        cluedoImageView.applyLightScaleAnimation()
    }

    private fun configureTimer() {
        TimerHelper(timerListener = this)
    }

}