package com.bartex.statesmvvm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.entity.state.State
import kotlinx.android.synthetic.main.flag_item.view.*

class FlagGridAdapter(private val onitemClickListener: OnitemClickListener,
                      val imageLoader: IImageLoader<ImageView>)
    : RecyclerView.Adapter<FlagGridAdapter.ViewHolder>() {

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagGridAdapter.ViewHolder {
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.flag_item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listFavoriteStates.size
    }

    override fun onBindViewHolder(holder: FlagGridAdapter.ViewHolder, position: Int) {
        holder.bind(listFavoriteStates[position])
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(state: State){
            state.flags?.get(0). let{
                imageLoader.loadInto(it.toString(), itemView.image_view_flag )
            }
            //щелчок на элементе списка флагов
            itemView.setOnClickListener {
                onitemClickListener.onItemclick(state)
            }
        }
    }

}