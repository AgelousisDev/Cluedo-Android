package com.agelousis.cluedonotepad.main.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.main.fragments.SuspectWhatFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhereFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhoFragment

class SuspectFragmentAdapter(private val context: Context, supportFragmentManager: FragmentManager): FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 3

    override fun getItem(position: Int) =
        when(position) {
            0 -> SuspectWhoFragment()
            1 -> SuspectWhatFragment()
            2 -> SuspectWhereFragment()
            else -> SuspectWhoFragment()
        }

    override fun getPageTitle(position: Int) =
        when(position) {
            0 -> context.resources.getString(R.string.key_who_label)
            1 -> context.resources.getString(R.string.key_what_label)
            2 -> context.resources.getString(R.string.key_where_label)
            else -> context.resources.getString(R.string.key_who_label)
        }


}