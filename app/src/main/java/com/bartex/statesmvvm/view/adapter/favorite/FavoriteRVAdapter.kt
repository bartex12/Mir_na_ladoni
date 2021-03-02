package com.bartex.statesmvvm.view.adapter.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.entity.favorite.Favorite
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.imageloader.IImageLoader
import kotlinx.android.synthetic.main.item_state_favorite.view.*

class FavoriteRVAdapter(private val onitemClickListener: OnitemClickListener, val imageLoader: IImageLoader<ImageView>)
    : RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder> (){

    interface OnitemClickListener{
        fun onItemclick(state: Favorite)
    }

    //так сделано чтобы передавать список в адаптер без конструктора
    // - через присвоение полю значения
    var listFavoriteStates: List<Favorite> = listOf()
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

        fun bind(favoriteState: Favorite){
            itemView.tv_name_favorite.text = favoriteState.name
            itemView.tv_area_favorite.text = favoriteState.area.toString()
            itemView.tv_population_favorite.text = favoriteState.population.toString()
            favoriteState.flag?.let { imageLoader.loadInto(it, itemView.iv_flag_favorite) }

            itemView.setOnClickListener {
                onitemClickListener.onItemclick(favoriteState)
            }
        }
    }
}