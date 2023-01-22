package com.bartex.statesmvvm.view.fragments.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.FavoriteRVAdapter
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {

    private var position = 0
    private var adapter: FavoriteRVAdapter? = null
    lateinit var navController:NavController
    private  val favoriteViewModel: FavoriteViewModel by viewModel()

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_favorite, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FavoriteFragment onViewCreated ")

        navController = Navigation.findNavController(view)

        favoriteViewModel.getFavorite().observe(viewLifecycleOwner,Observer<List<State>>{ favorites->
                Log.d(TAG, "FavoriteFragment onChanged")
                favorites?. let{
                    renderData(it)
                }
        })
        initAdapter()

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  favoriteViewModel.getPositionFavorite()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun initAdapter() {
        rv_favorite.layoutManager = LinearLayoutManager(requireActivity())

        adapter = FavoriteRVAdapter(
            favoriteViewModel,
            getOnClickListener(),
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rv_favorite.adapter = adapter
    }

    private fun renderData(favorites: List<State>) {
        if(favorites.isEmpty()){
            rv_favorite.visibility = View.GONE
            empty_view_favorite.visibility = View.VISIBLE
        }else{
            rv_favorite.visibility =  View.VISIBLE
            empty_view_favorite.visibility =View.GONE

            adapter?.listFavoriteStates = favorites //передаём список в адаптер
            adapter?.setRusLang(favoriteViewModel.getRusLang()) //смотрим какой язык в настройках
            rv_favorite.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
            Log.d(TAG, "FavoriteFragment renderData scrollToPosition = $position")
        }
    }

    private fun getOnClickListener(): FavoriteRVAdapter.OnItemClickListener =
            object : FavoriteRVAdapter.OnItemClickListener{
                override fun onItemClick(state: State) {
                    val bundle = bundleOf(Constants.STATE to state)
                    navController.navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
                }
            }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FavoriteFragment onPause ")
        //определяем первую видимую позицию
        val manager = rv_favorite.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        favoriteViewModel.savePositionFavorite(firstPosition)
    }

}