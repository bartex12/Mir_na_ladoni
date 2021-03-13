package com.bartex.statesmvvm.view.fragments.weather

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment()  {

    private var state: State? = null
    private lateinit var weatherViewModel:WeatherViewModel

    companion object {
        const val TAG = "33333"
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

        arguments?.let {state = it.getParcelable<State>(Constants.DETAILS)}

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.apply { App.instance.appComponent.inject(this) }

        weatherViewModel.getWeatherSealed(state)
            .observe(viewLifecycleOwner, Observer {renderData(it)})

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderData(data:WeatherSealed) {
        when(data){
            is WeatherSealed.Success ->{
                renderLoadingStop()
                renderWeather(data.weather)
            }
            is WeatherSealed.Error -> {
                renderLoadingStop()
                renderError(data.error)
            }
            is WeatherSealed.Loading -> {
                renderLoadingStart()
            }
        }
    }

    private fun renderError(error: Throwable) {
        tv_capital_description.text = error.message
        iv_icon.setImageDrawable( ContextCompat.getDrawable(requireContext(),R.drawable.whatcanido))
    }

    private fun renderWeather(weather: WeatherInCapital?) {
        state?.name?. let{ tv_state_name.text = it}
        tv_capital_name.text = weather?.name
        tv_capital_pressure.text = String.format("атм. давление %d мбар", weather?.main?.pressure)
        tv_capital_humidity.text = String.format("отн.влажность %d проц.", weather?.main?.humidity)
        tv_capital_description.text = weather?.weather?.get(0)?.description
        tv_capital_temp.text = String.format("%.0f \u2103", weather?.main?.temp)
        weather?.weather?.get(0)?.icon?. let{ iv_icon.setImageDrawable(getIconFromIconCod(it))}
    }

    private fun renderLoadingStart(){
        progressBar.visibility = View.VISIBLE
    }

    private fun renderLoadingStop(){
        progressBar.visibility = View.GONE
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