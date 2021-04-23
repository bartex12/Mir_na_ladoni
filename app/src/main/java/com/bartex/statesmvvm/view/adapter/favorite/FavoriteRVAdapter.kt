package com.bartex.statesmvvm.view.adapter.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.MapOfState
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.imageloader.IImageLoader
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteViewModel
import kotlinx.android.synthetic.main.item_state.view.*
import kotlinx.android.synthetic.main.item_state_favor.view.*


class FavoriteRVAdapter(private val favoriteViewModel: FavoriteViewModel, private val onitemClickListener: OnitemClickListener, val imageLoader: IImageLoader<ImageView>)
    : RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder> (){

    var isRus = false

    interface OnitemClickListener{
        fun onItemclick(state: State)
    }

    //так сделано чтобы передавать список в адаптер без конструктора
    // - через присвоение полю значения
    var listFavoriteStates: List<State> = listOf()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state_favor, parent, false )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listFavoriteStates.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(listFavoriteStates[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(state: State){
            itemView.tv_name_favor.text =
            if (isRus) state.nameRus else state.name

            itemView.tv_area_favor.text = favoriteViewModel.getArea(state.area)
            itemView.tv_population_favor.text = favoriteViewModel.getPopulation(state.population)
            state.flag?.let { imageLoader.loadInto(it, itemView.iv_flag_favor) }

            itemView.setOnClickListener {
                onitemClickListener.onItemclick(state)
            }
        }
    }

    fun setRusLang(isRus:Boolean){
        this.isRus = isRus
    }
}