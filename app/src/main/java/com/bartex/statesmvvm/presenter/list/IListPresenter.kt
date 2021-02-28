package com.bartex.statesmvvm.presenter.list

import com.bartex.statesmvvm.view.adapter.IItemView

//По аналогии с интерфейсом IItemView мы создали интерфейс IListPresenter,
// в который вынесли общие для всех списков вещи:слушатель клика,
// функция получения количества элементов и функция наполнения View.
//Здесь V представляет собой тип View для строки списка, а itemClickListener – функция,
// принимающая на вход эту самую View. Таким образом при обработке клика мы сможем получить
// от View позицию и найти нужный элемент
interface IListPresenter<V: IItemView> {
    var itemClickListener: ((V)->Unit)?
    fun bindView(view:V)
    fun getCount():Int
}