package com.bartex.statesmvvm.model.utils

import com.bartex.statesmvvm.model.entity.state.State

class StateUtils:IStateUtils {

    override fun getStateArea(area: Float?): String {
      var   textStateArea = ""
        area?. let {
            textStateArea = if(it!= 0f ){
                if (it>1000000f){
                    String.format("Площадь: %.1f млн. кв. км.", (it)/1000000)
                }else if (it in 1000f..1000000f){
                    String.format("Площадь: %.1f тыс. кв. км.", (it)/1000)
                }else{
                    String.format("Площадь: %.1f  кв. км.", it)
                }
            }else {
                "Площадь территории неизвестна"
            }
        }
        return textStateArea
    }

    override fun getStatePopulation(population: Int?): String {
        var   textStatePopulation = ""
        population?. let {
            textStatePopulation = if (it!=0){
                if(it>1000000){
                    String.format("Население: %.1f млн. чел.", (it.toFloat())/1000000)
                }else if (it in 1000..1000000){
                    String.format("Население: %.1f тыс. чел.", (it.toFloat())/1000)
                }else{
                    String.format("Население: %s чел.", it)
                }
            }else{
                "Население: численность неизвестна"
            }
        }
        return textStatePopulation
    }

    override fun getStatezoom(state: State?): String {
        var zoom = 0
        state?.area?. let{
            if (it<10f){
                zoom = 13
            }else if (it in 10f..1000f){
                zoom = 11
            }else if (it in 1000f..30000f){
                zoom = 9
            }else if (it in 1000f..100000f) {
                zoom = 7
            }else if (it in 100000f..1000000f){
                zoom = 5
            }else if (it in 1000000f..5000000f){
                zoom = 3
            }else{
                zoom = 1
            }
        }
        return String.format("geo:%s,%s?z=%s",
            state?.latlng?.get(0).toString(), state?.latlng?.get(1).toString(), zoom.toString())
    }

    override fun getStateCapital(capital: String?): String {
        var capitalState = ""
        capital?. let {
            capitalState = if (it != ""){
                String.format("Столица:   %S ", it)
            }else{
                "Название столицы неизвестно"
            }
        }
        return capitalState
    }

    override fun getStateRegion(region: String?): String {
        var regionNew = ""
        region?. let {
            regionNew = if(it!= ""){
                String.format("Регион:   %S ", it)
            }else{
                "Название региона неизвестно"
            }
        }
       return regionNew
    }
}