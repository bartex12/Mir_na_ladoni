package com.bartex.statesmvvm.presenter

import com.bartex.statesmvvm.view.fragments.favorite.IFavoriteView
import moxy.MvpPresenter

class FavoritePresenter : MvpPresenter<IFavoriteView>(){
//
//    @Inject
//    lateinit var roomCash: IRoomStateCash
//
//    @Inject
//    lateinit var helper : IPreferenceHelper
//
//    @Inject
//    lateinit var mainThreadScheduler: Scheduler
//
//    @Inject
//    lateinit var statesRepo: IStatesRepo
//
//    @Inject
//    lateinit var router: Router
//
//    @Inject
//    lateinit var stateUtils: IStateUtils
//
//    companion object{
//        const val TAG = "33333"
//    }
//
//    val favoritePresenter = FavoritePresenter()
//
//    //inner чтобы был доступ к переменным внешнего класса
// inner class FavoritePresenter : IFavoriteListPresenter {
//
//        val states = mutableListOf<State>()
//
//        override var itemClickListener: ((FavoritesItemView) -> Unit)? = null
//
//        override fun getCount() = states.size
//
//        override fun bindView(view: FavoritesItemView) {
//            val state = states[view.pos]
//
//            state.name?. let{view.setName(it)}
//            state.flag?. let{view.loadFlag(it)}
//            state.area?. let{view.setArea(stateUtils.getStateArea(state))}
//            state.population?. let{view.setPopulation(stateUtils.getStatePopulation(state))}
//        }
//    }
//
//    override fun onFirstViewAttach() {
//        super.onFirstViewAttach()
//        viewState.init()
//        loadFavorite()
//
//        //здесь присваиваем значение  слушателю щелчка по списку - ранее он был null
//        favoritePresenter.itemClickListener = { itemView ->
//            //переход на экран пользователя
//            val state =  favoritePresenter.states[itemView.pos]
//            helper.savePositionState(itemView.pos) //сохраняем позицию
//            Log.d(TAG, "FavoritePresenter itemClickListener state name =${state.name}")
//            router.replaceScreen(Screens.DetailsScreen(state))
//        }
//    }
//
//    //грузим данные и делаем сортировку в соответствии с настройками
//   fun loadFavorite() {
//        val isSorted = helper.isSorted()
//        val getSortCase = helper.getSortCase()
//        var f_st:List<State>?= null
//        roomCash. loadFavorite()
//            .observeOn(Schedulers.computation())
//            .flatMap {st->
//                if(isSorted){
//                    when (getSortCase) {
//                        1 -> {f_st = st.filter {it.population!=null}.sortedByDescending {it.population}}
//                        2 -> {f_st = st.filter {it.population!=null}.sortedBy {it.population}}
//                        3 -> {f_st = st.filter {it.area!=null}.sortedByDescending {it.area} }
//                        4 -> { f_st = st.filter {it.area!=null}.sortedBy {it.area} }
//                    }
//                    return@flatMap Single.just(f_st)
//                }else{
//                    return@flatMap Single.just(st)
//                }
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(mainThreadScheduler)
//            .subscribe ({states->
//                states?. let{Log.d(TAG, "FavoritePresenter  loadData states.size = ${it.size}")}
//                favoritePresenter.states.clear()
//                states?. let{favoritePresenter.states.addAll(it)}
//                viewState.updateList()
//            }, {error -> Log.d(StatesPresenter.TAG, "FavoritePresenter onError ${error.message}")
//            })
//    }
//
//    fun getPosition(): Int{
//        return helper.getPositionFavorite()
//    }
//
//    fun savePosition(position: Int){
//        helper.savePositionFavorite(position)
//    }
//
//    fun backPressed(): Boolean {
//        router.exit()
//        return true
//    }

}
