package com.bartex.statesmvvm.view.fragments.flags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.FavoriteRVAdapter
import com.bartex.statesmvvm.view.adapter.FlagGridAdapter
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_flags.*

class FlagsFragment:Fragment() {
    private var position = 0
    private var adapter: FlagGridAdapter? = null
    lateinit var navController: NavController
    private lateinit var flagViewModel: FlagViewModel

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_flags, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FlagsFragment onViewCreated ")

        navController = Navigation.findNavController(view)
        flagViewModel = ViewModelProvider(this).get(FlagViewModel::class.java)
        flagViewModel.apply {
            App.instance.appComponent.inject(this)
        }

        flagViewModel.getStateFlafs().observe(viewLifecycleOwner, Observer<List<State>>{ states->
            Log.d(TAG, "FlagsFragment onChanged")
            states?. let{
                renderData(it)
            }
        })
        initAdapter()

        //восстанавливаем позицию списка после поворота или возвращения на экран
        //position =  favoriteViewModel.getPositionFavorite()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun initAdapter() {
        rv_flags.layoutManager = GridLayoutManager(requireActivity(), 5)

        adapter = FlagGridAdapter(getOnClickListener(),
            GlideToVectorYouLoader(requireActivity())
        )
        rv_flags.adapter = adapter
    }

    private fun renderData(states: List<State>) {
        if(states.isEmpty()){
            rv_flags.visibility = View.GONE
            empty_view_flags.visibility = View.VISIBLE
        }else{
            rv_flags.visibility =  View.VISIBLE
            empty_view_flags.visibility = View.GONE

            adapter?.listFavoriteStates = states //передаём список в адаптер
        }
    }

    private fun getOnClickListener(): FlagGridAdapter.OnitemClickListener =
        object : FlagGridAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {
                val bundle = bundleOf(Constants.STATE to state)
                Log.d(TAG, "FlagsFragment getOnClickListener")
                navController.navigate(R.id.detailsFragment, bundle)
            }
        }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FlagsFragment onPause ")
        //определяем первую видимую позицию
        val manager = rv_flags.layoutManager as GridLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        //favoriteViewModel.savePositionFavorite(firstPosition)
    }

}