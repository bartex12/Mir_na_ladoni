package com.bartex.statesmvvm.view.fragments.help

import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment: Fragment() {

    companion object {
        const val TAG = "33333"

        const val STATES: String ="Для каждой из более чем двухсот стран"
        const val FLAGS: String ="изображениями флагов"
        const val MAP: String = "Карта"
        const val WEATHER: String = "Погода"
        const val WIKI: String = "Википедии"
        const val SEARCH: String = "Функция поиска"
        const val SELECTED: String = "Избранное"
        const val SETTINGS: String = "настройки приложения"

    }

    private lateinit var helpViewModel: HelpViewModel
    lateinit var navController: NavController

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        helpViewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        helpViewModel.apply { App.instance.appComponent.inject(this) }

        val helpText = helpViewModel.getHelpText()
        val spanHelp = SpannableString(helpText)
        val color = requireActivity().resources.getColor(R.color.colorPrimaryDarkPurple)

        helpText.let {
            setStyle(it, "Мир на ладони.", spanHelp)
            makeLinks(it, STATES, color, spanHelp)
            makeLinks(it, FLAGS, color, spanHelp)
            makeLinks(it, MAP, color, spanHelp)
            makeLinks(it, WEATHER, color, spanHelp)
            makeLinks(it, WIKI, color, spanHelp)
            makeLinks(it, SEARCH, color, spanHelp)
            makeLinks(it, SELECTED, color, spanHelp)
            makeLinks(it, SETTINGS, color, spanHelp)

            //Чтобы TextView корректно обрабатывал клик на подстроке, нужно настроить параметр
            // movementMethod. Он указывает, кому делегировать touch event. В нашем случае
            // мы просим TextView делегировать клик в LinkMovementMethod, который ищет
            // ClickableSpan и вызывает на них onClick.
            tv_help.movementMethod = LinkMovementMethod.getInstance()
            tv_help.setText(spanHelp, TextView.BufferType.SPANNABLE)

            //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
            //без этой строки меню в тулбаре ведёт себя неправильно
            setHasOptionsMenu(true)
            requireActivity().invalidateOptionsMenu()
        }
    }
    private fun setStyle(text:String, phrase:String, spanHelp: Spannable){
        val start = text.indexOf(phrase)
        val end = start + phrase.length

        spanHelp.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun makeLinks(text: String, phrase: String, phraseColor:Int, spanHelp:Spannable ){

        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                ds.color = phraseColor // устанавливаем наш цвет
                ds.isUnderlineText = true // нижнее подчеркивание
            }

            override fun onClick(view: View) {
                Log.d(TAG, "HelpFragment makeLinks onClick")

               val stateDefault =  State(capital="Moscow", flag= "https://upload.wikimedia.org/wikipedia/en/f/f3/Flag_of_Russia.svg",
                   name="Russian Federation", region="Europe", population=146599183,
                   area=1.7124442E7f, latlng= arrayOf(60.0f, 100.0f), nameRus="Россия",
                   capitalRus="Москва", regionRus="Европа")

                when(phrase){
                    MAP,WEATHER,WIKI   ->{
                        val bundle = bundleOf(Constants.STATE to stateDefault)
                        navController.navigate(R.id.detailsFragment, bundle)
                    }
                    STATES,SEARCH -> navController.navigate(R.id.statesFragment)
                    FLAGS-> navController.navigate(R.id.flagsFragment)
                    SELECTED-> navController.navigate(R.id.favoriteFragment)
                    SETTINGS-> navController.navigate(R.id.settingsFragment)
                    else -> navController.navigate(R.id.statesFragment)
                }
            }
        }

        val start = text.indexOf(phrase)
        val end = start + phrase.length
        spanHelp.setSpan(
            clickableSpan,
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
    }

}