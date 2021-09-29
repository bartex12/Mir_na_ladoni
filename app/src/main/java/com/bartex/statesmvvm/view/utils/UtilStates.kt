package com.bartex.statesmvvm.view.utils

import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object UtilStates {

    fun filterData(st: State) :Boolean{
        return  st.name!=null && st.capital!=null  && st.name.isNotBlank() &&
                st.latlng?.size == 2  &&
                st.capital.isNotBlank() && st.flags?.get(0).toString().isNotBlank() &&
                st.name != "Puerto Rico" && st.name !=  "French Guiana" &&
                st.flags?.get(0)!!.startsWith("https://restcountries.com")
    }

     fun getRegionName(id: Int): String {
        return when (id) {
            R.id.chip_all_region -> Constants.REGION_ALL
            R.id.chip_Europa_region -> Constants.REGION_EUROPE
            R.id.chip_Asia_region -> Constants.REGION_ASIA
            R.id.chip_America_region -> Constants.REGION_AMERICAS
            R.id.chip_Oceania_region -> Constants.REGION_OCEANIA
            R.id.chip_Africa_region -> Constants.REGION_AFRICA
            else -> Constants.REGION_ALL
        }
    }

    fun getRegionId(region: String): Int {
        return when (region) {
            Constants.REGION_ALL -> R.id.chip_all_region
            Constants.REGION_EUROPE -> R.id.chip_Europa_region
            Constants.REGION_ASIA -> R.id.chip_Asia_region
            Constants.REGION_AMERICAS -> R.id.chip_America_region
            Constants.REGION_OCEANIA -> R.id.chip_Oceania_region
            Constants.REGION_AFRICA -> R.id.chip_Africa_region
            else -> R.id.chip_Europa_region
        }
    }

    //показываем количество стран  по регионам
    fun showCountByRegion(chipGroupStates: ChipGroup, listOfStates: MutableList<State>){
        var regionNameAndCount = ""
        for (i in 0 until chipGroupStates.childCount) {
            val chip = chipGroupStates.getChildAt(i) as Chip
            regionNameAndCount =  when(chip.id){
                R.id.chip_all_region -> "${getRegionName(chip.id)} ${listOfStates.size}"
                R.id.chip_Europa_region-> {
                    getChipText(listOfStates, Constants.REGION_EUROPE,  chip.id)
                }
                R.id.chip_Asia_region-> {
                    getChipText(listOfStates, Constants.REGION_ASIA,  chip.id)
                }
                R.id.chip_America_region-> {
                    getChipText(listOfStates, Constants.REGION_AMERICAS,  chip.id)
                }
                R.id.chip_Oceania_region-> {
                    getChipText(listOfStates, Constants.REGION_OCEANIA,  chip.id)
                }
                R.id.chip_Africa_region->{
                    getChipText(listOfStates, Constants.REGION_AFRICA,  chip.id)
                }
                else ->{
                    getChipText(listOfStates, Constants.REGION_EUROPE,  chip.id)
                }
            }
            chip.text = regionNameAndCount
        }
    }

    //текст на чипе региона
    private fun getChipText(listOfStates: MutableList<State>, regionRus:String, chipId:Int): String {
        val filteredList = listOfStates.filter { state ->
            state.regionRus == regionRus
        } as MutableList<State>
        return "${getRegionName(chipId)} ${filteredList.size}"
    }

}