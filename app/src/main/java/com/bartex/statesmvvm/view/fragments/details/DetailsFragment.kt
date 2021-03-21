package com.bartex.statesmvvm.view.fragments.details

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.os.trace
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfRegion
import com.bartex.statesmvvm.common.MapOfState
import com.bartex.statesmvvm.common.toast
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel :DetailsViewModel
    lateinit var navController: NavController
    private var state: State? = null

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "DetailsFragment onViewCreated")

        //здесь аргументы нужны для корректной обработки поворота экрана
        arguments?.let {state = it.getParcelable<State>(Constants.STATE )}

        navController = Navigation.findNavController(view)

        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        detailsViewModel.apply { App.instance.appComponent.inject(this) }

        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //заполняем поля экрана
        state?. let {
            detailsViewModel.isFavoriteState(it)
                .observe(viewLifecycleOwner, Observer<DetailsSealed> {renderData(it)})
            //заполняем поля экрана
            if (detailsViewModel.getRusLang()){
                tv_state_name.text =  MapOfState.mapStates[it.name]
                tv_state_capital.text = detailsViewModel.getStateCapital(MapOfCapital.mapCapital[it.capital])
                tv_state_region.text = detailsViewModel.getStateRegion(MapOfRegion.mapRegion[it.region])
            }else{
                tv_state_name.text = it.name
                tv_state_capital.text =  detailsViewModel.getStateCapital(it.capital)
                tv_state_region.text = detailsViewModel.getStateRegion(it.region)
            }

            tv_state_area.text =  detailsViewModel.getStateArea(it.area)
            tv_state_population.text =  detailsViewModel.getStatePopulation(it.population)

            it.flag?. let{flag->
                GlideToVectorYou.justLoadImage(requireActivity(), Uri.parse(flag), iv_flag_big)
            }
        }

        btn_addToFavorite.setOnClickListener {
            state?. let {
                detailsViewModel.addToFavorite(it)
            }
        }
        btn_removeFavorite.setOnClickListener {
            state?. let {
                detailsViewModel.removeFavorite(it)
            }
        }
        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderData(data: DetailsSealed) {
        when (data) {
            is DetailsSealed.Success -> {
                if (data.isFavorite){
                    Log.d(TAG, "DetailsFragment renderData Success isFavorite")
                    //toast(getString(R.string.addFavoriteToast))
                    btn_addToFavorite.visibility = View.GONE
                    btn_removeFavorite.visibility = View.VISIBLE
                }else{
                    Log.d(TAG, "DetailsFragment renderData Success not Favorite")
                    //toast(getString(R.string.removeFavoriteToast))
                    btn_addToFavorite.visibility = View.VISIBLE
                    btn_removeFavorite.visibility = View.GONE
                }
            }
            is DetailsSealed.Error -> {
                toast(data.error.message)
                Log.d(TAG, "DetailsFragment renderData Error")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "DetailsFragment onPause")
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.home -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView page_1")
                navController.navigate(R.id.action_detailsFragment_to_statesFragment)
                true
            }
            R.id.geo -> {
                //c GoogleNavigation карта не включается в стэк вызовов и поэтому если вызвать карту
                //а потом погоду и нажать кнопку назад, карта не вызовется  - это хорошо
                Log.d(TAG, "DetailsFragment BottomNavigationView page_2")
                state?. let {sendGeoIntent(detailsViewModel.getStateZoom(it))}
                true
            }
            R.id.weather -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView page_3")
                state?. let {
                    val bundle = bundleOf(Constants.DETAILS to state)
                    navController.navigate(R.id.action_detailsFragment_to_weatherFragment, bundle)
                }
                true
            }
            R.id.wiki -> {
                state?. let {
                    //ручной запуск анонимной активити со страницей из википедии
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data =  Uri.parse("https://en.wikipedia.org/wiki/${it.name}")
                    startActivity(intent)

                }
                true
            }
            else -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView else")
                false
            }
        }
    }

     private fun sendGeoIntent(geoCoord:String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoCoord))
        // пакет для использования Гугл карты
        val packageManager: PackageManager = requireActivity().packageManager
        if (isPackageInstalled("com.google.android.apps.maps", packageManager)) {
            intent.setPackage("com.google.android.apps.maps")
        }
        requireActivity().startActivity(intent)
    }

    //если ошибка - возвращаем false
    private fun isPackageInstalled(packageName: String,packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}