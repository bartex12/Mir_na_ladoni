package com.bartex.statesmvvm.view.fragments.states

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
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.imageloader.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.state.StateRVAdapter
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.fragment_states.*


class StatesFragment : Fragment() {

    private var position = 0
    var adapter: StateRVAdapter? = null
    lateinit var navController:NavController
    lateinit var stateViewModel: StatesViewModel

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_states, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "StatesFragment onViewCreated ")

        navController = Navigation.findNavController(view)

        stateViewModel = ViewModelProvider(this).get(StatesViewModel::class.java)
        stateViewModel.apply { App.instance.appComponent.inject(this)}

        stateViewModel.loadData()

        stateViewModel.getStates()
            .observe(viewLifecycleOwner, Observer<List<State>> {
            renderData(it)
            })

        initAdapter()

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  stateViewModel.getPositionState()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        //без этой строки меню в тулбаре ведёт себя неправильно
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "StatesFragment onResume ")
        //два раза всё отрабатывает - это плохо
        //todo
        //presenter.loadData() // обновляем данные при изменении настроек
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "StatesFragment onPause ")
        //определяем первую видимую позицию
        val manager = rv_states.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        stateViewModel.savePositionState(firstPosition)
    }

    private fun initAdapter() {
        rv_states.layoutManager = LinearLayoutManager(requireActivity())

        adapter = StateRVAdapter(
            getOnClickListener(),
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rv_states.adapter = adapter
        rv_states.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
        Log.d(FavoriteFragment.TAG, "FavoriteFragment init scrollToPosition = $position")
    }

    private fun renderData(states: List<State>) {
        if(states.isEmpty()){
            rv_states.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }else{
            rv_states.visibility =  View.VISIBLE
            empty_view.visibility =View.GONE

            adapter?.listStates = states
        }
    }

    private fun getOnClickListener(): StateRVAdapter.OnitemClickListener =
        object : StateRVAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {
                val bundle = Bundle().apply { putParcelable(Constants.STATE, state) }
                navController.navigate(R.id.action_statesFragment_to_detailsFragment, bundle)
            }
        }
}

