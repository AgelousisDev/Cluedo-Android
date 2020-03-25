package com.agelousis.cluedonotepad.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.constants.Constants
import com.agelousis.cluedonotepad.dialog.BasicDialog
import com.agelousis.cluedonotepad.dialog.models.BasicDialogType
import com.agelousis.cluedonotepad.extensions.isNightMode
import com.agelousis.cluedonotepad.extensions.run
import com.agelousis.cluedonotepad.main.NotePadActivity
import com.agelousis.cluedonotepad.splash.adapters.PlayersAdapter
import com.agelousis.cluedonotepad.splash.models.CharacterModel
import com.agelousis.cluedonotepad.splash.viewModels.CharacterViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences(Constants.PREFERENCES_TAG, Context.MODE_PRIVATE)
    }

    private var characterViewModel: CharacterViewModel? = null
        set(value) {
            field = value
            value?.addDefaultRow(CharacterModel(characterNameHint = resources.getString(R.string.key_your_name_hint)))
        }

    private var lastSeekBarProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        setupNightModeIdSaved()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        configureViewModel()
        setupUI()
    }

    private fun setupNightModeIdSaved() {
        when (sharedPreferences.isNightMode) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupUI() {
        darkModeSwitch.isChecked = sharedPreferences?.isNightMode == 1 || isNightMode == 1
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences?.edit()) {
                this?.putInt(Constants.DARK_MODE_VALUE, if (isChecked) 1 else 0)
                this?.apply()
            }
            this@SplashActivity.finish()
            startActivity(Intent(this@SplashActivity, SplashActivity::class.java))
        }

        playersSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                lastSeekBarProgress = seekBar?.progress ?: 0
                playButton.isEnabled = seekBar?.progress ?: 0 > 0
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cluedoImageView.visibility = if (progress > 0) View.GONE else View.VISIBLE
                darkModeSwitch.visibility = if (progress > 0) View.GONE else View.VISIBLE
                if (progress > lastSeekBarProgress)
                    (progress - lastSeekBarProgress).run {
                        characterViewModel?.addCharacter(characterModel = CharacterModel(characterNameHint = resources.getString(R.string.key_player_name_hint)))
                    }
                else if (progress < lastSeekBarProgress)
                    (lastSeekBarProgress - progress).run {
                        characterViewModel?.removeCharacter()
                    }

                (playersRecyclerView.adapter as? PlayersAdapter)?.update(appendState = progress > lastSeekBarProgress)
                lastSeekBarProgress = progress
            }
        })

        setupRecyclerView()
        playButton.setOnClickListener {
            openMainActivity()
        }
    }

    private fun setupRecyclerView() {
        val flexLayoutManager = FlexboxLayoutManager(this@SplashActivity, FlexDirection.ROW)
        flexLayoutManager.flexDirection = FlexDirection.ROW
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        playersRecyclerView.layoutManager = flexLayoutManager
        val playersAdapter = PlayersAdapter(context = this, characterListModel = characterViewModel?.characterArray ?: listOf())
        playersRecyclerView.adapter = playersAdapter
    }

    private fun configureViewModel() {
        characterViewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)
    }

    private fun openMainActivity() {
        if (((characterViewModel?.characterArray?.size ?: 0) < 2 && characterViewModel?.characterArray?.none { it.character == null || it.characterName.isNullOrEmpty() } == true) ||
                characterViewModel?.characterArray?.mapNotNull { it.character }?.distinct()?.size ?: 0 < characterViewModel?.characterArray?.size ?: -1)
            BasicDialog.show(supportFragmentManager = supportFragmentManager, dialogType = BasicDialogType(
                title = resources.getString(R.string.key_warning_label),
                text = resources.getString(R.string.key_not_selected_players_message)
            )
            )
        else startActivity(with(Intent(this, NotePadActivity::class.java)) {
            putParcelableArrayListExtra(NotePadActivity.CHARACTER_MODEL_LIST_EXTRA, characterViewModel?.characterArray)
            this
        })
    }

}