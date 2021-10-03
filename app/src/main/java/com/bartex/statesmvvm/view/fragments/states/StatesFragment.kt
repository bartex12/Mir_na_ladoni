package com.bartex.statesmvvm.view.fragments.states

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.toast
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.network.NoInternetDialogFragment
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import com.bartex.statesmvvm.view.main.MainActivity
import com.bartex.statesmvvm.view.main.MainViewModel
import com.bartex.statesmvvm.view.shared.SharedViewModel
import com.bartex.statesmvvm.view.utils.UtilStates
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.*

class   StatesFragment : Fragment(),
    SearchView.OnQueryTextListener {

    private var position = 0
    private var adapter: StateRVAdapter? = null
    lateinit var navController:NavController
    private lateinit var stateViewModel: StatesViewModel
    private lateinit var rvStates: RecyclerView
    private lateinit  var emptyViewStates: TextView
    private lateinit var chipGroupStates: ChipGroup
    private lateinit var progressBarState: ProgressBar
    private var listOfStates  = mutableListOf<State>() //список стран мира
    private var filtred:List<State> = listOf() // отфильтрованный и отсортированный список (список региона)
    private var region:String = Constants.REGION_ALL // текущий

    private val sharedViewModel: SharedViewModel by activityViewModels() //спец viewModel для обмена данными

    companion object {
        const val TAG = "33333"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        return inflater.inflate(R.layout.fragment_states, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "StatesFragment onViewCreated ")

        navController = Navigation.findNavController(view)

        stateViewModel = ViewModelProvider(this).get(StatesViewModel::class.java)
        stateViewModel.apply { App.instance.appComponent.inject(this)}

        initViews(view)
        initAdapter()
        initChipGroupListener()

        //приводим меню тулбара в соответствии с onPrepareOptionsMenu в MainActivity
        //без этой строки меню в тулбаре ведёт себя неправильно
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()

       val  isNetworkAvailable = (requireActivity() as MainActivity).getNetworkAvailable()

        //если первое включение - смотрим в сети, если сети нет - смотрим в базе - если и там нет - диалог
        //если не первое включение, сначала смотрим в базе если там нет -показываем диалог
        Log.d(TAG, "###StatesFragment onViewCreated: FirstRun = ${sharedViewModel.getFirstRun()}")
        //если первое включение
        if (sharedViewModel.getFirstRun()){
            sharedViewModel.setFirstRun(false) //устанавливаем флаг - не первое включение
            //сначала смотрим в сети
            if (isNetworkAvailable) { //если сеть есть
                //получаем страны из сети
                stateViewModel.getStatesSealed()
                    .observe(viewLifecycleOwner, Observer { stateSealed ->
                        renderData(stateSealed)
                    })
            } else {
                //если данных в сети нет, смотрим в базе
                stateViewModel.getDataFromDatabase()
                    .observe(viewLifecycleOwner, Observer { list ->
                        if (list.size > 200) { //если в базе есть записи берём из базы
                            listOfStates = list //запоминаем
                            chipGroupStates.check(UtilStates. getRegionId(region))
                            renderDataWithRegion(region)  // с учётом текущего региона
                        }else{
                            sharedViewModel.setFirstRun(true) //устанавливаем флаг чтобы при появлении сети повторить
                            //если данных ни  в базе ни в сети нет, показываем диалог
                            showAlertDialog(
                                getString(R.string.dialog_title_device_is_offline),
                                getString(R.string.dialog_message_load_impossible)
                            )
                        }
                    })
            }
        }else{
            //если не первое включение, сначала смотрим в базе
            stateViewModel.getDataFromDatabase()
                .observe(viewLifecycleOwner, Observer { list ->
                    if (list.size > 200) { //если в базе есть записи берём из базы
                        listOfStates = list //запоминаем
                        chipGroupStates.check(UtilStates. getRegionId(region))
                        renderDataWithRegion(region)  // с учётом текущего региона
                    }else{
                        //если в базе нет, это самый первый запуск приложения в отсутствие сети
                        sharedViewModel.setFirstRun(true) //устанавливаем флаг чтобы при появлении сети повторить
                        //показываем диалог
                        showAlertDialog(
                            getString(R.string.dialog_title_device_is_offline),
                            getString(R.string.dialog_message_load_impossible)
                        )
                    }
                })
        }
        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  stateViewModel.getPositionState()

        stateViewModel.getNewRegion()
            .observe(viewLifecycleOwner, Observer {newRegion->
                region = newRegion //текущий регион
                //при смене региона обновляем данные
                chipGroupStates.check(UtilStates. getRegionId(region))
                renderDataWithRegion(region)
            })
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
        val manager = rvStates.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        stateViewModel.savePositionState(firstPosition)
        Log.d(TAG, "StatesFragment onPause firstPosition = $firstPosition")
    }

    private fun initViews(view: View) {
        progressBarState =view.findViewById(R.id.progressBarState)
        rvStates =  view.findViewById(R.id.rv_states)
        emptyViewStates =  view.findViewById(R.id.empty_view)
        chipGroupStates =  view.findViewById(R.id.chip_region_region)
    }

    private fun showAlertDialog(title: String?, message: String?) {
        NoInternetDialogFragment.newInstance(title, message)
            .show(requireActivity().supportFragmentManager, Constants.DIALOG_FRAGMENT)
    }

    private fun initAdapter() {
        rvStates.layoutManager = LinearLayoutManager(requireActivity())

        adapter = StateRVAdapter(
            getOnClickListener(),
            GlideToVectorYouLoader(
                requireActivity()
            )
        )
        rvStates.adapter = adapter
    }

    private fun initChipGroupListener() {
        chipGroupStates.setOnCheckedChangeListener { _, id ->
            when(id){ //выделяем цветом
                -1 -> chipGroupStates.check( R.id.chip_all_region) //если два раза
                else-> chipGroupStates.check(id) //если один раз
            }
            val newRegion:String = UtilStates.getRegionName(id)
            stateViewModel. updateRegion(newRegion)
        }
    }

    private fun renderDataWithRegion(newRegion: String) {
        when (newRegion) {
            Constants.REGION_ALL ->renderStates(listOfStates)
                        else -> {
                val filteredList = listOfStates.filter { state ->
                    state.regionRus == newRegion
                } as MutableList<State>
                renderStates(filteredList)
            }
        }
    }

    private fun renderData(data: StatesSealed) {
        Log.d(TAG, "StatesFragment renderData")
        when(data){
            is StatesSealed.Success -> {
                renderLoadingStop()
                listOfStates = data.state as MutableList<State>
                renderStates(listOfStates)
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

    private fun renderStates(states: List<State>) {
        Log.d(TAG, "StatesFragment renderState: states.size = ${states.size}")
        if(states.isEmpty()){
            rvStates.visibility = View.GONE
            emptyViewStates.visibility = View.VISIBLE
        }else{
            rvStates.visibility =  View.VISIBLE
            emptyViewStates.visibility =View.GONE

            getNumberOnChipName()

            val isSorted = stateViewModel.isSorted()
            val getSortCase = stateViewModel.getSortCase()

            if(isSorted){
                when (getSortCase) {
                    1 -> {filtred = states.filter {it.population!=null}.sortedByDescending {it.population} }
                    2 -> {filtred = states.filter {it.population!=null}.sortedBy {it.population} }
                    3 -> {filtred = states.filter {it.area!=null && it.area!!>0}.sortedByDescending {it.area}}
                    4 -> {filtred = states.filter {it.area!=null && it.area!!>0}.sortedBy {it.area}}
                }
            //Log.d(TAG, "StatesQuizFragment filtred:  $filtred")
            adapter?.listStates = filtred
            adapter?.setRusLang(stateViewModel.getRusLang())
            rvStates.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
            Log.d(TAG, "StatesFragment renderState scrollToPosition = $position")
        }
    }
}

    private fun getNumberOnChipName() {
        for (i in 0 until chipGroupStates.childCount) {
            val chip = chipGroupStates.getChildAt(i) as Chip
            val regionName =
                if (chip.isChecked) {
                getRegionNameAndNumber()
            }else{
                UtilStates. getRegionName(chip.id)
            }
            chip.text = regionName
        }
    }

    private fun getRegionNameAndNumber(): String {
        val regionSize: Int = when (region) {
            Constants.REGION_ALL -> listOfStates.size
            else -> listOfStates.filter { it.regionRus == region }.size
        }
        return  "$region $regionSize"
    }

    private fun getOnClickListener(): StateRVAdapter.OnitemClickListener =
        object : StateRVAdapter.OnitemClickListener{
            override fun onItemclick(state: State) {

                //чтобы сразу исчезала клавиатура а не после перехода в детали
                val inputManager: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    requireActivity().currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)

                val bundle = bundleOf(Constants.STATE to state) //так проще
                navController.navigate(R.id.action_statesFragment_to_detailsFragment, bundle)
            }
        }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val searchItem: MenuItem = menu.findItem(R.id.search_toolbar)
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
        //изменяется список внутри StatesQuizFragment
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        stateViewModel. updateRegion(Constants.REGION_ALL)
        newText?. let {
            if (it.isNotBlank()) {
                val listSearched = mutableListOf<State>()
                if(stateViewModel.getRusLang()){
                    for (state in listOfStates) {
                        state.nameRus?. let{ nameRus->
                            if((nameRus.toUpperCase(Locale.ROOT)
                                    .startsWith(it.toUpperCase(Locale.ROOT)))){
                                listSearched.add(state)
                            }
                        }
                    }
                }else{
                    for (state in listOfStates) {
                        state.name?. let{ name->
                            if((name.toUpperCase(Locale.ROOT)
                                    .startsWith(it.toUpperCase(Locale.ROOT)))){
                                listSearched.add(state)
                            }
                        }
                    }
                }
                //если строка поиска не пуста - выводим список полученный в ходе поиска
                adapter?.listStates = listSearched
            }else{
                //если строка поиска  пуста - выводим то же что было
                adapter?.listStates = filtred
            }
        }
        return false
    }
}

