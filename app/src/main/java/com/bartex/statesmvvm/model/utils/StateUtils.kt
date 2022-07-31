package com.bartex.statesmvvm.model.utils

import com.bartex.statesmvvm.model.entity.state.State

class StateUtils:IStateUtils {

    override fun getStateArea(area: Float?): String {
      var   textStateArea = "Площадь территории неизвестна"
        area?. let {
            textStateArea = if(it > 0f ){
                when {
                    it>1000000f -> { String.format("Площадь: %.1f млн. кв. км.", (it)/1000000)}
                    it in 1000f..1000000f -> {String.format("Площадь: %.1f тыс. кв. км.", (it)/1000)}
                    else -> {String.format("Площадь: %.1f кв. км.", it)}
                }
            }else {
                "Площадь территории неизвестна"
            }
        }
        return textStateArea
    }

    override fun getStatePopulation(population: Int?): String {
        var   textStatePopulation = "Население: численность неизвестна"
        population?. let {
            textStatePopulation = if (it>0){
                when {
                    it>1000000 -> {String.format("Население: %.1f млн. чел.", (it.toFloat())/1000000)}
                    it in 1000..1000000 -> {String.format("Население: %.1f тыс. чел.", (it.toFloat())/1000)}
                    else -> {String.format("Население: %s чел.", it)}
                }
            }else{
                "Население: численность неизвестна"
            }
        }
        return textStatePopulation
    }

    override fun getStateDensity(area: Float?, population: Int?): String {
        val textStateDensity = "Плотность населения неизвестна"
        var density = 0.0F

        if (area!=null && area>0f && population!=null && population>0){
            density = population.toFloat()/area
            when {
                density > 100f -> return  String.format("Плотность: %.0f человек на 1 кв км", density)
                density in 1f..100f -> return  String.format("Плотность: %.1f человек на 1 кв км", density)
                density < 1 -> return  String.format("Плотность: %.2f человек на 1 кв км", density)
            }
        }
        return textStateDensity
    }

    override fun getStatezoom(state: State?): String {
       val  zoom = getZoom(state?.area)
        return String.format("geo:%s,%s?z=%s",
            state?.latlng?.get(0).toString(), state?.latlng?.get(1).toString(), zoom.toString())
    }


     fun getZoom(area: Float?): Int {
        var zoom1 = 0
        area?.let {
            if (it < 10f) {
                zoom1 = 13
            } else if (it in 10f..1000f) {
                zoom1 = 11
            } else if (it in 1000f..30000f) {
                zoom1 = 9
            } else if (it in 30000f..100000f) {
                zoom1 = 7
            } else if (it in 100000f..1000000f) {
                zoom1 = 5
            } else if (it in 1000000f..5000000f) {
                zoom1 = 3
            } else {
                zoom1 = 1
            }
        }
        return zoom1
    }

    override fun getStateCapital(capital: String?): String {
        var capitalState = "Название столицы неизвестно"
        capital?. let {
            capitalState = if (it.isNotBlank()){
                String.format("Столица:   %S ", it)
            }else{
                "Название столицы неизвестно"
            }
        }
        return capitalState
    }

    override fun getStateRegion(region: String?): String {
        var regionNew = "Название региона неизвестно"
        region?. let {
            regionNew = if(it.isNotBlank()){
                String.format("Регион:   %S ", it)
            }else{
                "Название региона неизвестно"
            }
        }
       return regionNew
    }
}