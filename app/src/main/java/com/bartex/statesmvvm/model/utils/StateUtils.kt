package com.bartex.statesmvvm.model.utils

import com.bartex.statesmvvm.model.entity.state.State

class StateUtils:IStateUtils {

    override fun getStateArea(state: State?): String {
      var   text_state_area = ""
        state?. let {
            if(it.area != 0f ){
                it.area?. let{
                    if (it>1000000f){
                        val area = (it)/1000000
                        text_state_area = String.format("Площадь: %.1f млн. кв. км.", area)
                    }else if (it in 1000f..1000000f){
                        val area = (it)/1000
                        text_state_area =String.format("Площадь: %.1f тыс. кв. км.", area)
                    }else{text_state_area =String.format("Площадь: %.1f  кв. км.", it)
                    }
                }?: let{text_state_area = "Площадь территории неизвестна"}
            }
        }
        return text_state_area
    }

    override fun getStatePopulation(state: State?): String {
        var   text_state_population = ""
        state?. let {
            if (it.population!=0){
                it.population?. let{
                    if(it>1000000){
                        val population = (it.toFloat())/1000000
                        text_state_population = String.format("Население: %.1f млн. чел.", population)
                    }else if (it in 1000..1000000){
                        val population = (it.toFloat())/1000
                        text_state_population =String.format("Население: %.1f тыс. чел.", population)
                    }else{
                        text_state_population =String.format("Население: %s чел.", it)
                    }
                }?: let{text_state_population ="Население: численность неизвестна"}
            }
        }
        return text_state_population
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

    override fun getStateCapital(state: State?): String {
        var capital = ""
        state?. let {
            if (it.capital != ""){capital =  String.format("Столица:   %S ", it.capital)
            }else{capital =  "Название столицы неизвестно"}
        }
        return capital
    }

    override fun getStateRegion(state: State?): String {
        var region = ""
        state?. let {
            if(it.region!= ""){
                region = String.format("Регион:   %S ", it.region)
            }else{ region = "Название региона неизвестно" }
        }
       return region
    }
}