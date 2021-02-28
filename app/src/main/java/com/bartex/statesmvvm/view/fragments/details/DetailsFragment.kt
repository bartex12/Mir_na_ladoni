package com.bartex.statesmvvm.view.fragments.details

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.presenter.DetailsPresenter
import com.bartex.statesmvvm.view.fragments.BackButtonListener
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_details.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class DetailsFragment : MvpAppCompatFragment(),
    IDetailsView,
    BackButtonListener {

    companion object {
        const val TAG = "33333"
        private const val ARG_STATE = "state"

        @JvmStatic
        fun newInstance(state: State) =
            DetailsFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_STATE, state)
                    }
                }
    }
    private var state: State? = null

    val presenter: DetailsPresenter by moxyPresenter {
        //здесь аргументы нужны - иначе state = null
        arguments?.let {state = it.getParcelable<State>(ARG_STATE )}
        Log.d(TAG, "DetailsFragment onCreate state = $state")
        DetailsPresenter(state).apply {
            App.instance.appComponent.inject(this)
        }
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
        arguments?.let {state = it.getParcelable<State>(ARG_STATE )}

        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        state?. let { presenter.isFavorite(it)}

        btn_addToFavorite.setOnClickListener {
            state?. let { presenter.addToFavorite(it)}
        }
        btn_removeFavorite.setOnClickListener {
            state?. let { presenter.removeFavorite(it)}
        }

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
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
                presenter.toHome()
                true
            }
            R.id.geo -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView page_2")
                state?. let {presenter.sendGeoIntent(it)}
                true
            }
            R.id.weather -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView page_3")
                state?. let {presenter.showWeather(it)}
                true
            }
            else -> {
                Log.d(TAG, "DetailsFragment BottomNavigationView else")
                false
            }
        }
    }

    //реализация метода BackButtonListener
    override fun backPressed(): Boolean = presenter.backPressed()

    override fun setStateName() {
        state?. let { tv_state_name.text = it.name}
    }

    override fun setStateRegion() {
        state?. let { tv_state_region.text =
            String.format("Регион:   %S ", it.region)}
    }

    override fun setStateFlag() {
        state?. let {
            it.flag?. let{
                GlideToVectorYou.justLoadImage(requireActivity(), Uri.parse(it), iv_flag_big)
            }
        }
    }

    override fun setStateArea(area:String) {
        tv_state_area.text = area
    }

    override fun setStatePopulation(population:String) {
        tv_state_population.text = population
    }

    override fun setStateCapital(capital:String) {
        tv_state_capital.text = capital
    }

    override fun sendGeoIntent(geoCoord:String) {

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

    override fun showAddFavoriteToast() {
        //Toast.makeText(requireActivity(), getString(R.string.addFavoriteToast), Toast.LENGTH_SHORT ).show()
        btn_addToFavorite.visibility = View.GONE
        btn_removeFavorite.visibility = View.VISIBLE
    }

    override fun setVisibility(isFavorite: Boolean) {
        if (isFavorite){
            btn_addToFavorite.visibility = View.GONE
            btn_removeFavorite.visibility = View.VISIBLE
        }else{
            btn_addToFavorite.visibility = View.VISIBLE
            btn_removeFavorite.visibility = View.GONE
        }
    }

    override fun showRemoveFavoriteToast() {
       // Toast.makeText(requireActivity(), getString(R.string.removeFavoriteToast), Toast.LENGTH_SHORT ).show()
        btn_addToFavorite.visibility = View.VISIBLE
        btn_removeFavorite.visibility = View.GONE
    }

}