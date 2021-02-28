package com.bartex.statesmvvm.view.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.presenter.base.IBaseView
import com.bartex.statesmvvm.presenter.SearchPresenter
import com.bartex.statesmvvm.view.adapter.state.StatesRVAdapter
import com.bartex.statesmvvm.view.adapter.imageloader.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.fragments.BackButtonListener
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class SearchFragment: MvpAppCompatFragment(),
    IBaseView,
    BackButtonListener {

    private var position = 0
    var adapter: StatesRVAdapter? = null

    companion object {
        const val TAG = "33333"
        private const val ARG_SEARCH = "search"

        @JvmStatic
        fun newInstance(search: String) =
            SearchFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_SEARCH, search)
                    }
                }
    }

    val presenter: SearchPresenter by moxyPresenter {
        val search = arguments?.getString(ARG_SEARCH)
        Log.d(TAG, "SearchFragment SearchPresenter by moxyPresenter search = $search ")
        SearchPresenter(search).apply {
            App.instance.appComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_search, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "SearchFragment onViewCreated ")
        //восстанавливаем позицию списка после поворота или возвращения на экран
        position = presenter.getPositionSearch()
        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "SearchFragment onResume ")
        presenter.loadData() // обновляем данные при изменении настроек
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        //определяем первую видимую позицию
        val manager = rv_search.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        presenter.savePositionSearch(firstPosition)
    }

    override fun init() {
        Log.d(TAG, "SearchFragment init ")
        rv_search.layoutManager = LinearLayoutManager(context)
        adapter = StatesRVAdapter(
            presenter.listPresenter,
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rv_search.adapter = adapter
        rv_search.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
    }

    override fun updateList() {
        Log.d(TAG, "SearchFragment updateList ")
        if(presenter.listPresenter.states.isEmpty()){
            rv_search.visibility = View.GONE
            empty_view_Search.visibility = View.VISIBLE
            Log.d(TAG, "SearchFragment updateList  list = Empty")
        }else{
            rv_search.visibility =  View.VISIBLE
            empty_view_Search.visibility =View.GONE

            Log.d(TAG, "SearchFragment updateList  list size = " +
                    "${presenter.listPresenter.states.size}")
            adapter?.notifyDataSetChanged()
        }
    }

    override fun backPressed(): Boolean = presenter.backPressed()

}