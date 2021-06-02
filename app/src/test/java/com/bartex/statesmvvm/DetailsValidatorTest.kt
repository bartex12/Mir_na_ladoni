package com.bartex.statesmvvm

import com.bartex.statesmvvm.model.utils.StateUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsValidatorTest {

    /*--------------------Area-------------------*/
    @Test
fun stateArea_million(){
   assertEquals("Площадь: 5,1 млн. кв. км.", StateUtils().getStateArea(5123000f))
}

    @Test
    fun stateArea_thousand(){
        assertEquals("Площадь: 5,1 тыс. кв. км.", StateUtils().getStateArea(5123f))
    }

    @Test
    fun stateArea_km(){
        assertEquals("Площадь: 5,1 кв. км.", StateUtils().getStateArea(5.123f))
    }

    @Test
    fun stateArea_unknownIfZero(){
        assertEquals("Площадь территории неизвестна", StateUtils().getStateArea(0f))
    }

    @Test
    fun stateArea_unknownIfLessZero(){
        assertEquals("Площадь территории неизвестна", StateUtils().getStateArea(-32f))
    }

    @Test
    fun stateArea_unknownIfNull(){
        assertEquals("Площадь территории неизвестна", StateUtils().getStateArea(null))
    }

/*-------------------------Population--------------------------*/
    @Test
    fun statePopulation_million(){
        assertEquals("Население: 5,1 млн. чел.",
            StateUtils().getStatePopulation(5123000))
    }

    @Test
    fun statePopulation_thousand(){
        assertEquals("Население: 5,1 тыс. чел.",
            StateUtils().getStatePopulation(5123))
    }

    @Test
    fun statePopulation_person(){
        assertEquals("Население: 123 чел.",
            StateUtils().getStatePopulation(123))
    }

    @Test
    fun statePopulation_unknownIfZero(){
        assertEquals("Население: численность неизвестна",
            StateUtils().getStatePopulation(0))
    }

    @Test
    fun statePopulation_unknownIfLessZero(){
        assertEquals("Население: численность неизвестна",
            StateUtils().getStatePopulation(-32))
    }

    @Test
    fun statePopulation_unknownIfNull(){
        assertEquals("Население: численность неизвестна",
            StateUtils().getStatePopulation(null))
    }

/*-------------------------Region------------------------*/
@Test
fun nameRegion_isCorrect(){
    assertEquals("Регион:   АФРИКА ",
        StateUtils().getStateRegion("Африка"))
}

    @Test
    fun nameRegion_unknownIfNull(){
        assertEquals("Название региона неизвестно",
            StateUtils().getStateRegion(null))
    }

    @Test
    fun nameRegion_unknownIfEmpty(){
        assertEquals("Название региона неизвестно",
            StateUtils().getStateRegion(""))
    }

    @Test
    fun nameRegion_unknownIfBlank(){
        assertEquals("Название региона неизвестно",
            StateUtils().getStateRegion("   "))
    }

    /*------------------------Capital----------------------*/
    @Test
    fun nameCapital_isCorrect(){
        assertEquals("Столица:   МОСКВА ",
            StateUtils().getStateCapital("Москва"))
    }

    @Test
    fun nameCapital_unknownIfNull(){
        assertEquals("Название столицы неизвестно",
            StateUtils().getStateCapital(null))
    }

    @Test
    fun nameCapital_unknownIfEmpty(){
        assertEquals("Название столицы неизвестно",
            StateUtils().getStateCapital(""))
    }

    @Test
    fun nameCapital_unknownIfBlank(){
        assertEquals("Название столицы неизвестно",
            StateUtils().getStateCapital("   "))
    }

    /*-------------------------Zoom---------------------*/
    @Test
    fun zoom_isCorrect_areaLess10(){
        assertEquals(13,StateUtils().getZoom(5f))
    }

    @Test
    fun zoom_isCorrect_area10_1000(){
        assertEquals(11,StateUtils().getZoom(500f))
    }

    @Test
    fun zoom_isCorrect_area1000_30000(){
        assertEquals(9,StateUtils().getZoom(10000f))
    }

    @Test
    fun zoom_isCorrect_area30000_100000(){
        assertEquals(7,StateUtils().getZoom(50000f))
    }

    @Test
    fun zoom_isCorrect_area100000_1000000(){
        assertEquals(5,StateUtils().getZoom(500000f))
    }

    @Test
    fun zoom_isCorrect_area1000000_5000000(){
        assertEquals(3,StateUtils().getZoom(4000000f))
    }

    @Test
    fun zoom_isCorrect_areaMore5000000(){
        assertEquals(1,StateUtils().getZoom(10000000f))
    }
}