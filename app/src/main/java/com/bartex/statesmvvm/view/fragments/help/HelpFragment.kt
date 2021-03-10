package com.bartex.statesmvvm.view.fragments.help

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment: Fragment(){

    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?)=
        View.inflate(context, R.layout.fragment_help, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpViewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        helpViewModel.apply {App.instance.appComponent.inject(this)}

        val helpText = helpViewModel.getHelpText()
        helpText?. let{
            tv_help.text = Html.fromHtml(it)
        }?:  getString(R.string.noHelp)

        chip4.setOnClickListener{
            helpViewModel.saveTheme()
            requireActivity().recreate()
        }
    }


}