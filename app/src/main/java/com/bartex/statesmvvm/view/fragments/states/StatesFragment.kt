package com.bartex.statesmvvm.view.fragments.states

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.OnlineLiveData
import com.bartex.statesmvvm.common.toast
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import kotlinx.android.synthetic.main.fragment_states.*


class StatesFragment : Fragment() {

    private var position = 0
    private var adapter: StateRVAdapter? = null
    lateinit var navController:NavController
    private lateinit var stateViewModel: StatesViewModel

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        View.inflate(context, R.layout.fragment_states, null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "StatesFragment onViewCreated ")

        navController = Navigation.findNavController(view)

        stateViewModel = ViewModelProvider(this).get(StatesViewModel::class.java)
        stateViewModel.apply { App.instance.appComponent.inject(this)}

        OnlineLiveData(requireActivity()).observe(
            viewLifecycleOwner,
            Observer<Boolean> {
                if (it){
                    Log.d(TAG, "StatesFragment OnlineLiveData Online")
                    stateViewModel.getStatesFromNet()
                }else{
                    Log.d(TAG, "StatesFragment OnlineLiveData Offline")
//                    Toast.makeText(
//                        requireActivity(),
//                        R.string.dialog_message_device_is_offline,
//                        Toast.LENGTH_LONG
//                    ).show()
                    stateViewModel.getStatesFromRoom()
                }
                    .observe(viewLifecycleOwner, Observer<StatesSealed> {renderData(it)})
            })

//
//        stateViewModel.getStatesFromNet()
//            .observe(viewLifecycleOwner, Observer<StatesSealed> {renderData(it)})

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  stateViewModel.getPositionState()

        initAdapter()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        //без этой строки меню в тулбаре ведёт себя неправильно
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        //определяем первую видимую позицию
        val manager = rv_states.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        stateViewModel.savePositionState(firstPosition)
        Log.d(TAG, "StatesFragment onPause firstPosition = $firstPosition")
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
    }

    private fun renderData(data: StatesSealed) {
        Log.d(TAG, "StatesFragment renderData")
        when(data){
            is StatesSealed.Success -> {
                renderLoadingStop()
                renderState(data.state)
            }
            is StatesSealed.Error ->{
                renderLoadingStop()
                renderError(data.error)
            }
            is StatesSealed.Loading ->{
                renderLoadingStart()
            }
        }
    }

    private fun renderLoadingStart(){
        progressBarState.visibility = View.VISIBLE
    }

    private fun renderLoadingStop(){
        progressBarState.visibility = View.GONE
    }

    private fun renderError(error: Throwable) {
        toast(error.message)
    }

    private fun renderState(states: List<State>) {
        if(states.isEmpty()){
            rv_states.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }else{
            rv_states.visibility =  View.VISIBLE
            empty_view.visibility =View.GONE

            adapter?.listStates = states
            adapter?.setRusLang(stateViewModel.getRusLang())
            rv_states.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
            Log.d(TAG, "StatesFragment renderState scrollToPosition = $position")
        }
    }


    private fun getOnClickListener(): StateRVAdapter.OnitemClickListener =
        object : StateRVAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {
                //val bundle = Bundle().apply { putParcelable(Constants.STATE, state) }
                val bundle = bundleOf(Constants.STATE to state) //так проще
                navController.navigate(R.id.action_statesFragment_to_detailsFragment, bundle)
            }
        }
}

