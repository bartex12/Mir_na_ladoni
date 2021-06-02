package com.bartex.statesmvvm


import com.bartex.statesmvvm.common.MapOfCapital
import com.bartex.statesmvvm.common.MapOfRegion
import com.bartex.statesmvvm.common.MapOfState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StateRusValidatorTest {

    @Test
    fun nameRus_CorrectName_ReturnTrue(){
        assertTrue(MapOfState.mapStates["Andorra"]== "Андорра" )
    }

    @Test
    fun nameRus_UnknownName_ReturnTrue(){
        assertTrue(MapOfState.mapStates["Andorr"]== null )
    }

    @Test
    fun nameRus_CorrectName_ReturnFalse(){
        assertFalse(MapOfState.mapStates["Andorr"]== "Андорра" )
    }

    /*------------------------------------------------------*/

    @Test
    fun capitalRus_CorrectCapital_ReturnTrue(){
        assertTrue(MapOfCapital.mapCapital["Vienna"] == "Вена")
    }

    @Test
    fun capitalRus_UnknownCapital_ReturnTrue(){
        assertTrue(MapOfCapital.mapCapital[""] == null)
    }

    @Test
    fun nameRus_UnknownCapital_ReturnFalse(){
        assertFalse(MapOfState.mapStates["Vien"]== "Вена" )
    }

    /*-------------------------------------------------*/

    @Test
    fun capitalRus_CorrectRegion_ReturnTrue(){
        assertTrue(MapOfRegion.mapRegion["Africa"] == "Африка")
    }

    @Test
    fun capitalRus_UnknownRegion_ReturnTrue(){
        assertTrue(MapOfRegion.mapRegion[""] == null)
    }

    @Test
    fun nameRus_UnknownRegion_ReturnFalse(){
        assertFalse(MapOfRegion.mapRegion["Afric"]== "Африка" )
    }
}