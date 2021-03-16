package com.bartex.statesmvvm.view.adapter.state

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.view.adapter.imageloader.IImageLoader
import kotlinx.android.synthetic.main.item_state.view.*

class StateRVAdapter(private val onitemClickListener: OnitemClickListener, val imageLoader: IImageLoader<ImageView>)
    : RecyclerView.Adapter<StateRVAdapter.ViewHolder> () {

    companion object{
        const val TAG = "33333"
    }

    interface OnitemClickListener{
        fun onItemclick(state: State)
    }

    //так сделано чтобы передавать список в адаптер без конструктора
    // - через присвоение полю значения
    var listStates: List<State> = listOf()
        set(value){
            field = value
           // Log.d(TAG, "StateRVAdapter set =  ${listStates.map {/* it.name+" * "+*/it.capital+" * " }}")
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listStates.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStates[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(state: State){
            itemView.tv_name.text = state.name
            state.flag?.let { imageLoader.loadInto(it, itemView.iv_flag) }

            itemView.setOnClickListener {
                onitemClickListener.onItemclick(state)
            }
        }
    }
}