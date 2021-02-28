package com.bartex.statesmvvm.view.fragments.weather

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.presenter.WeatherPresenter
import com.bartex.statesmvvm.view.fragments.BackButtonListener
import kotlinx.android.synthetic.main.fragment_weather.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class WeatherFragment : MvpAppCompatFragment(),
    IWeatherView,
    BackButtonListener {

    private var state: State? = null

    companion object {
        const val TAG = "33333"
        private const val ARG_STATE = "state"

        @JvmStatic
        fun newInstance(state: State) =
            WeatherFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_STATE, state)
                    }
                }
    }

    val presenter: WeatherPresenter by moxyPresenter {
        arguments?.let {state = it.getParcelable<State>(ARG_STATE)}
        Log.d(TAG, "WeatherFragment  capital = ${state?.capital}")
        WeatherPresenter(state).apply {
            App.instance.appComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    override fun setStateName(state: String) {
        tv_state_name.text = state
    }

    override fun setCapitalName(head: String) {
        tv_capital_name.text = head
    }

    override fun setTemp(temp: Float) {
        tv_capital_temp.text = String.format("%.0f \u2103", temp)
    }

    override fun setPressure(pressure: Int) {
        tv_capital_pressure.text = String.format("атм. давление %d мбар", pressure)
    }

    override fun setHumidity(humidity: Int) {
        tv_capital_humidity.text = String.format("отн.влажность %d проц.", humidity)
    }

    override fun setDescription(description: String) {
        tv_capital_description.text = description
    }

    override fun setIconDrawble(icon: String) {
        iv_icon.setImageDrawable(getIconFromIconCod(icon))
    }

    override fun setErrorMessage() {
        tv_capital_description.text = getString(R.string.ErrorCity)
        iv_icon.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.whatcanido))
    }

    override fun backPressed(): Boolean {
        presenter.backPressed()
        return true
    }

    //Drawable это import android.graphics.drawable.Drawable - не буду тащить его в презентер
    private fun getIconFromIconCod(iconCod: String): Drawable? {
        return   when (iconCod) {
            "01d", "01n" -> ContextCompat.getDrawable(requireContext(), R.drawable.sun)
            "02d", "02n" ->ContextCompat.getDrawable(requireContext(),R.drawable.partly_cloudy)
            "03d", "03n" -> ContextCompat.getDrawable(requireContext(),R.drawable.cloudy)
            "04d", "04n" ->ContextCompat.getDrawable(requireContext(),R.drawable.cloudy)
            "09d", "09n" -> ContextCompat.getDrawable(requireContext(),R.drawable.rain)
            "10d", "10n" ->ContextCompat.getDrawable(requireContext(),R.drawable.little_rain)
            "11d", "11n" -> ContextCompat.getDrawable(requireContext(),R.drawable.boom)
            "13d", "13n" ->ContextCompat.getDrawable(requireContext(),R.drawable.snow)
            "50d", "50n" ->ContextCompat.getDrawable(requireContext(),R.drawable.smog)
            else -> ContextCompat.getDrawable(requireContext(),R.drawable.whatcanido)
        }

    }
}