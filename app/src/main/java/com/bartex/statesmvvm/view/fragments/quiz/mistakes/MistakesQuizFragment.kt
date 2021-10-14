package com.bartex.statesmvvm.view.fragments.quiz.mistakes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.GlideToVectorYouLoader
import com.bartex.statesmvvm.view.adapter.MistakesAdapter
import com.bartex.statesmvvm.view.shared.SharedViewModel
import com.bartex.statesmvvm.view.utils.UtilFilters
import com.bartex.statesmvvm.view.utils.UtilMistakes
import com.google.android.material.chip.ChipGroup
import java.util.*

class MistakesQuizFragment: Fragment(),
        SearchView.OnQueryTextListener {

    private var position = 0
    private var adapter:  MistakesAdapter? = null
    private lateinit var navController: NavController

    private val mistakesViewModel by lazy{
        ViewModelProvider(requireActivity()).get(MistakesQuizModel::class.java)
    }

    private val model: SharedViewModel by activityViewModels()

    private var listOfMistakeStates  = mutableListOf<State>() //список стран региона с ошибками
    private lateinit var rvStatesMistake: RecyclerView
    private lateinit  var emptyViewMistake: TextView
    private lateinit var chipGroupMistake: ChipGroup
    private var region:String = Constants.REGION_ALL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mistakes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        //восстанавливаем позицию списка после поворота или возвращения на экран
        position =  mistakesViewModel.getPositionState()

        initViews(view)
        initAdapter()
        initChipGroupListener()
        initMenu()

        //чтобы получить текущий регион - сделал обмен данными через SharedViewModel
        // во FlagsQuizFragment и StateQuizFragment в initChipGroupListener() кладём значение, а здесь принимаем
        model.newRegion.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ newRegion->
            region = newRegion
            chipGroupMistake.check(UtilMistakes.getRegionId(region)) //отметка на чипе
            //не убирать эту строку иначе при повороте данные пропадают!
            renderDataWithRegion(region)
        })

        //получаем все ошибки автоматически при любом изменении в базе данных
        mistakesViewModel.getAllMistakesLive()
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer{
                    listOfMistakeStates =   it.map {room->
//                        State(capital =room.capital, flag = room.flag, name =room.name,
//                            region = room.region, nameRus = room.nameRus,
//                            capitalRus = room.capitalRus, regionRus = room.regionRus
//                        )
                        State(room.capital, room.flag, room.name, room.region,
                            room.population, room.area, arrayOf(room.lat, room.lng),
                            room.nameRus, room.capitalRus, room.regionRus
                        )
                    }.filter {st-> //отбираем только те, где полные данные
                        UtilFilters.filterData(st)
                    }  as MutableList<State>

                    UtilMistakes.showCountByRegion(chipGroupMistake, listOfMistakeStates)
                    chipGroupMistake.check(UtilMistakes.getRegionId(region))//отметка на чипе
                    renderDataWithRegion(region)
                })
    }

    //запоминаем  позицию списка, на которой сделан клик - на случай поворота экрана
    override fun onPause() {
        super.onPause()
        //определяем первую видимую позицию
        val manager = rvStatesMistake.layoutManager as LinearLayoutManager
        val firstPosition = manager.findFirstVisibleItemPosition()
        mistakesViewModel.savePositionState(firstPosition)
    }

    private fun initMenu() {
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()
    }

    private fun initViews(view: View) {
        rvStatesMistake = view.findViewById(R.id.rv_states_mistakes)
        emptyViewMistake = view.findViewById(R.id.empty_view_mistakes)
        chipGroupMistake = view.findViewById(R.id.chip_region_mistakes)
    }

    private fun initAdapter() {
        rvStatesMistake.layoutManager = LinearLayoutManager(requireActivity())

        adapter = MistakesAdapter(
            getOnMistakeClickListener(),
                getOnRemoveListener(),
            GlideToVectorYouLoader(requireActivity())
        )
        rvStatesMistake.adapter = adapter
    }

    private fun renderData(listOfMistakeStates:MutableList<State>) {
        if(listOfMistakeStates.isEmpty()){
            rvStatesMistake.visibility = View.GONE
            emptyViewMistake.visibility = View.VISIBLE
            if (region == Constants.REGION_ALL){
                emptyViewMistake.text = getString(R.string.no_data_state_test_all)
            }else{
                emptyViewMistake.text = getString(R.string.no_data_state_test_region)
            }
        }else{
            rvStatesMistake.visibility =  View.VISIBLE
            emptyViewMistake.visibility = View.GONE

            listOfMistakeStates.sortBy {it.nameRus}
            adapter?.listOfMistakes = listOfMistakeStates

            rvStatesMistake.layoutManager?.scrollToPosition(position) //крутим в запомненную позицию списка
        }
    }

    private fun renderDataWithRegion(newRegion: String) {
        when (newRegion) {
            Constants.REGION_ALL -> {
                renderData(listOfMistakeStates)
            }
            else -> {
                val filteredList = listOfMistakeStates.filter { state ->
                    state.regionRus == newRegion
                } as MutableList<State>
                renderData(filteredList)
            }
        }
    }

    private fun initChipGroupListener() {
        chipGroupMistake.setOnCheckedChangeListener { _, id ->
            when(id){ //выделяем цветом
                -1 -> chipGroupMistake.check( R.id.chip_all_mistakes) //если два раза на одном чипе
                else-> chipGroupMistake.check(id) //если один раз
            }
            region = UtilMistakes.getRegionName(id)
            renderDataWithRegion(region)
        }
    }

    private fun getOnMistakeClickListener(): MistakesAdapter.OnMistakeClickListener =
        object : MistakesAdapter.OnMistakeClickListener{
            override fun onMistakeClick(mistakeState: State) {
                val bundle = bundleOf(Constants.STATE to mistakeState)
                val destinationId = navController.currentDestination?.id
                if(destinationId == R.id.tabsFragment) {
                   navController.navigate(R.id.action_tabsFragment_to_detailsFragment, bundle)
               }else{
                    navController.navigate(R.id.detailsFragment, bundle)
                }

            }
        }

    private fun getOnRemoveListener(): MistakesAdapter.OnRemoveListener =
            object: MistakesAdapter.OnRemoveListener{
                override fun onRemove(nameRus: String) {
                    mistakesViewModel.removeMistakeFromDatabase(nameRus)
                }
            }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.search_toolbar)?.isVisible = true

        val searchItem: MenuItem = menu.findItem(R.id.search_toolbar)
        val searchView =searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_country) //пишем подсказку в строке поиска
        searchView.setOnQueryTextListener(this)//устанавливаем слушатель
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //ничего не делаем - не будет фрагмента поиска, так как при вводе символов
        //изменяется список внутри RegionFragment
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        model.updateRegion(Constants.REGION_ALL) // для поиска ставим Все регионы на чипы
        newText?. let {
            if (it.isNotBlank()) {
                val listSearched = mutableListOf<State>()
                for (state in listOfMistakeStates ) {
                    state.nameRus?. let{ nameRus->
                        if((nameRus.toUpperCase(Locale.ROOT)
                                        .startsWith(it.toUpperCase(Locale.ROOT)))){
                            listSearched.add(state)
                        }
                    }
                }
                adapter?.listOfMistakes = listSearched
            }else{
                adapter?.listOfMistakes = listOfMistakeStates
            }
        }
        return false
    }
}