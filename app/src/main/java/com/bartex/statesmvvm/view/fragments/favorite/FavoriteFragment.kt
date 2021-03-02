package com.bartex.statesmvvm.view.fragments.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.favorite.Favorite
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.favorite.FavoriteRVAdapter
import com.bartex.statesmvvm.view.adapter.imageloader.GlideToVectorYouLoader
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment: Fragment() {

    private var position = 0
    var adapter: FavoriteRVAdapter? = null
    lateinit var navController:NavController
    lateinit var favoriteViewModel: FavoriteViewModel

    companion object {
        const val TAG = "33333"
//
//        fun newInstance() = FavoriteFragment()
    }

    //todo сохр позицию, строки в норм виде
//    val presenter: FavoritePresenter by moxyPresenter {
//        FavoritePresenter().apply {
//            App.instance.appComponent.inject(this)
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_favorite, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FavoriteFragment onViewCreated ")

        navController = Navigation.findNavController(view)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteViewModel.apply {
            App.instance.appComponent.inject(this)
        }

        favoriteViewModel.loadFavorite()

        favoriteViewModel.getFavorite().observe(viewLifecycleOwner,object :Observer<List<State>>{
            override fun onChanged(favorites: List<State>?) {
                Log.d(TAG, "FavoriteFragment onChanged")
                favorites?. let{
                    renderData(it)
                }?: return
            }
        })

        initAdapter()

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  favoriteViewModel.getPositionFavorite()
        //position = presenter.getPosition()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun initAdapter() {
        rv_favorite.layoutManager = LinearLayoutManager(requireActivity())

        adapter = FavoriteRVAdapter(
            getOnClickListener(),
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rv_favorite.adapter = adapter
        rv_favorite.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
        Log.d(TAG, "FavoriteFragment init scrollToPosition = $position")
    }

    private fun renderData(favorites: List<State>) {
        if(favorites.isEmpty()){
            rv_favorite.visibility = View.GONE
            empty_view_favorite.visibility = View.VISIBLE
        }else{
            rv_favorite.visibility =  View.VISIBLE
            empty_view_favorite.visibility =View.GONE

            val favStr = favorites.map {
                Favorite(name = it.name, area = favoriteViewModel.getArea(it),
                population = favoriteViewModel.getPopulation(it), flag = it.flag)
            }

            adapter?.listFavoriteStates = favStr
        }
    }

    private fun getOnClickListener(): FavoriteRVAdapter.OnitemClickListener =
            object : FavoriteRVAdapter.OnitemClickListener{
                override fun onItemclick(favorite: Favorite) {
                    val bundle = Bundle().apply { putParcelable(Constants.FAVORITE, favorite) }
                    navController.navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
                }
            }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FavoriteFragment onResume ")
        //todo
        //presenter.loadFavorite() // обновляем данные при изменении настроек
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FavoriteFragment onPause ")
        //определяем первую видимую позицию
        val manager = rv_favorite.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        favoriteViewModel.savePositionFavorite(firstPosition)
        //presenter.savePosition(firstPosition)
    }
    //todo
    //override fun backPressed(): Boolean = presenter.backPressed()

}