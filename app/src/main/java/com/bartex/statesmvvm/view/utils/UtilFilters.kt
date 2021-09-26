package com.bartex.statesmvvm.view.utils

import com.bartex.statesmvvm.model.entity.state.State

object UtilFilters {

    fun filterData(st: State) :Boolean{
        return  st.name!=null && st.capital!=null  && st.name.isNotBlank() &&
                st.latlng?.size == 2  &&
                st.capital.isNotBlank() && st.flags?.get(0).toString().isNotBlank() &&
                st.name != "Puerto Rico" && st.name !=  "French Guiana" &&
                st.flags?.get(0)!!.startsWith("https://restcountries.com")
    }

}