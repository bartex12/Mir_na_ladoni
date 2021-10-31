package com.bartex.statesmvvm.view.utils

import com.bartex.statesmvvm.model.entity.state.State


object UtilFilters {

    fun filterData(st: State) :Boolean{
        return  st.name!=null && st.capital!=null  && st.name.isNotBlank() &&
                st.capital.isNotBlank() && st.flag !=null && st.flag.isNotBlank() &&
                st.name != "Falkland Islands (Malvinas)" && st.name !=  "Malta" &&
                st.flag.startsWith("https://upload.wikimedia.org") ||
                st.flag!!.startsWith("https://flagcdn.com")
    }

    fun filterDataStates(st: State) :Boolean{
        return  st.name!=null && st.capital!=null  && st.name.isNotBlank() &&
                st.latlng?.size == 2  &&
                st.capital.isNotBlank() && st.flag !=null && st.flag.isNotBlank() &&
                st.capitalRus!="Unknown" && st.nameRus!="Unknown" &&
                /* st.name != "Falkland Islands (Malvinas)" && st.name !=  "Malta" &&*/
                (st.flag.startsWith("https://upload.wikimedia.org") ||
                        st.flag.startsWith("https://flagcdn.com"))
    }
}