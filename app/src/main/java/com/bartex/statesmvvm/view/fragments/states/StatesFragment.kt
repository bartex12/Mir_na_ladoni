package com.bartex.statesmvvm.view.fragments.states

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.toast
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.network.NoInternetDialogFragment
import com.bartex.statesmvvm.network.OnlineLiveData
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import com.bartex.statesmvvm.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_states.*
import java.util.*

class   StatesFragment : Fragment(),
    SearchView.OnQueryTextListener {

    private var position = 0
    private var adapter: StateRVAdapter? = null
    lateinit var navController:NavController
    private lateinit var stateViewModel: StatesViewModel
    private var searchStates = listOf<State>()

    //для доступа к полю MainActivity isNetworkAvailable, где проверяется доступ к интернету
    var main:MainActivity? = null

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
     val  view:View =inflater.inflate(R.layout.fragment_states, container, false)
        main = requireActivity() as MainActivity
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "StatesFragment onViewCreated ")

        navController = Navigation.findNavController(view)

        stateViewModel = ViewModelProvider(this).get(StatesViewModel::class.java)
        stateViewModel.apply { App.instance.appComponent.inject(this)}

       val  isNetworkAvailable = main?.getNetworkAvailable()
        Log.d(TAG, "StatesFragment onViewCreated isNetworkAvailable =$isNetworkAvailable")

        isNetworkAvailable?. let{isNet->
            stateViewModel. loadDataSealed(isNet) //запускаем загрузку данных
            stateViewModel.getStatesSealed() //наблюдаем за изменением данных
                .observe(viewLifecycleOwner, Observer<StatesSealed> {
                    renderData(it)
                })
        }

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  stateViewModel.getPositionState()

        initAdapter()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        //без этой строки меню в тулбаре ведёт себя неправильно
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "StatesFragment onResume")
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "StatesFragment onPause")
        //определяем первую видимую позицию
        val manager = rv_states.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        stateViewModel.savePositionState(firstPosition)
        Log.d(TAG, "StatesFragment onPause firstPosition = $firstPosition")
    }

    private fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    private fun showAlertDialog(title: String?, message: String?) {
        NoInternetDialogFragment.newInstance(title, message)
            .show(requireActivity().supportFragmentManager, Constants.DIALOG_FRAGMENT)
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
        Log.d(TAG, "StatesFragment renderState: states.size = ${states.size}")
        if(states.isEmpty()){
            rv_states.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }else{
            rv_states.visibility =  View.VISIBLE
            empty_view.visibility =View.GONE

            adapter?.listStates = states
            adapter?.setRusLang(stateViewModel.getRusLang())
            searchStates = states
            rv_states.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
            Log.d(TAG, "StatesFragment renderState scrollToPosition = $position")
        }
    }

    private fun getOnClickListener(): StateRVAdapter.OnitemClickListener =
        object : StateRVAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {

                //чтобы сразу исчезала клавиатура а не после перехода в детали
                val inputManager: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    requireActivity().currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)

                //val bundle = Bundle().apply { putParcelable(Constants.STATE, state) }
                val bundle = bundleOf(Constants.STATE to state) //так проще
                navController.navigate(R.id.action_statesFragment_to_detailsFragment, bundle)
            }
        }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView =searchItem.actionView as SearchView
        //значок лупы слева в развёрнутом сост и сворачиваем строку поиска (true)
        //searchView.setIconifiedByDefault(true)
        //пишем подсказку в строке поиска
        searchView.queryHint = getString(R.string.search_country)
        //устанавливаем в панели действий кнопку ( > )для отправки поискового запроса
       // searchView.isSubmitButtonEnabled = true
        //устанавливаем слушатель
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //ничего не делаем - не будет фрагмента поиска, так как при вводе символов
        //изменяется список внутри StatesFragment
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?. let {
            if (it.isNotBlank()) {
                val listSearched = mutableListOf<State>()
                if(stateViewModel.getRusLang()){
                    for (state in searchStates) {
                        state.nameRus?. let{ nameRus->
                            if((nameRus.toUpperCase(Locale.ROOT)
                                    .startsWith(it.toUpperCase(Locale.ROOT)))){
                                listSearched.add(state)
                            }
                        }
                    }
                }else{
                    for (state in searchStates) {
                        state.name?. let{ name->
                            if((name.toUpperCase(Locale.ROOT)
                                    .startsWith(it.toUpperCase(Locale.ROOT)))){
                                listSearched.add(state)
                            }
                        }
                    }
                }
                adapter?.listStates = listSearched
            }else{
                adapter?.listStates = searchStates
            }
        }
        return false
    }
}

