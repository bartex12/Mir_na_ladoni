package com.bartex.statesmvvm.view.fragments.search

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.imageloader.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.state.StateRVAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: Fragment() {

    private var position = 0
    private var adapter: StateRVAdapter? = null
    lateinit var navController: NavController
    private lateinit var searchViewModel: SearchViewModel

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_search, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "SearchFragment onViewCreated ")

        val search = arguments?.getString(Constants.SEARCH)

        navController = Navigation.findNavController(view)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.apply {App.instance.appComponent.inject(this)}

        searchViewModel.loadDataSearch(search)
        searchViewModel.getStatesSearch()
            .observe(viewLifecycleOwner, Observer {
            renderDataSearched(it)
        })

        initAdapter()

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position = searchViewModel.getPositionSearch()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderDataSearched(listSearched: List<State>) {
        if(listSearched.isEmpty()){
            rv_search.visibility = View.GONE
            empty_view_Search.visibility = View.VISIBLE
        }else{
            rv_search.visibility =  View.VISIBLE
            empty_view_Search.visibility =View.GONE

            adapter?.listStates = listSearched
        }
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        //определяем первую видимую позицию
        val manager = rv_search.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        searchViewModel.savePositionSearch(firstPosition)
    }

    fun initAdapter() {
        Log.d(TAG, "SearchFragment initAdapter ")
        rv_search.layoutManager = LinearLayoutManager(context)
        adapter = StateRVAdapter(
            getOnClickListener(),
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rv_search.adapter = adapter
        rv_search.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
    }

    private fun getOnClickListener(): StateRVAdapter.OnitemClickListener =
        object : StateRVAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {
                val bundle = bundleOf(Constants.STATE to state)
                navController.navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
            }
        }
}