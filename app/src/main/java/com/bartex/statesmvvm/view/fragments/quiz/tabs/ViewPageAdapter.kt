package com.bartex.statesmvvm.view.fragments.quiz.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.FlagsQuizFragment
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.MistakesQuizFragment
import com.bartex.statesmvvm.view.fragments.quiz.flagstatemistake.StatesQuizFragment

class ViewPageAdapter(val context: Context, fragmentManager : FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragments = arrayOf(
        FlagsQuizFragment(),
        StatesQuizFragment(),
        MistakesQuizFragment()
    )

    private val titles = arrayOf(
        context.getString(R.string.flags),
        context.getString(R.string.states),
        context.getString(R.string.mistakes)
    )

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
       return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}