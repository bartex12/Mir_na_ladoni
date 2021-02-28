package com.bartex.statesmvvm.view.adapter.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.presenter.list.IFavoriteListPresenter
import com.bartex.statesmvvm.view.adapter.imageloader.IImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_state_favorite.view.*

class FavoriteRVAdapter(val presenter: IFavoriteListPresenter, val imageLoader: IImageLoader<ImageView>)
    : RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state_favor, parent, false )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = presenter.getCount()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.pos =position
        presenter.bindView(holder)

        holder.containerView.setOnClickListener{
            //нельзя иначе осуществить вызов nullable-значения функционального типа.
            //вызовет itemClickListener, если он не равен null
            presenter.itemClickListener?.invoke(holder)
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer,
        FavoritesItemView {

        override var pos = -1

        override fun setName(name: String) {
            containerView.tv_name_favorite.text = name
        }

        override fun loadFlag(url: String) {
            imageLoader.loadInto(url, containerView.iv_flag_favorite)
        }

        override fun setArea(area: String) {
            containerView.tv_area_favorite.text = area
        }

        override fun setPopulation(population: String) {
            containerView.tv_population_favorite.text = population
        }
    }

}