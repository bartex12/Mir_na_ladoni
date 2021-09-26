package com.bartex.statesmvvm

import com.bartex.statesmvvm.model.api.ApiService
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.states.StatesRepo
import com.bartex.statesmvvm.model.repositories.states.cash.IRoomStateCash
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class StateRepoTest {

    private lateinit var repository: StatesRepo
    @Mock
    private lateinit var stateApi: ApiService
    @Mock
    private lateinit var roomCash: IRoomStateCash

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = StatesRepo(stateApi, roomCash)
    }

    @Test
    fun statesRepo_TestNetYes() {
        val state = State()

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(stateApi.getStates()).thenReturn(Single.just(listOf(state)))

        //вызываем метод repository, сеть есть
        repository.getStates(true)
        Mockito.verify(stateApi, Mockito.times(1)).getStates()

    }

    @Test
    fun statesRepo_TestNetNo() {
        val state = State()

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(roomCash.getStatesFromCash()).thenReturn(Single.just(listOf(state)))

        //вызываем метод repository, сети нет
        repository.getStates(false)
        Mockito.verify(roomCash, Mockito.times(1)).getStatesFromCash()
    }
}