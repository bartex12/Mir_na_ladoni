package com.bartex.statesmvvm

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.view.fragments.states.StatesSealed
import com.bartex.statesmvvm.view.fragments.scheduler.ScheduleProviderStub
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StatesViewModelTest {

    //это правило работает с Архитектурными компонентами (которым является LiveData),
    // запуская выполнение фоновых задач синхронно и по порядку, что важно для выполнения тестов.
    // В случае с LiveData рекомендуется использовать это Правило для получения
    // консистентных результатов (иначе порядок выполнения кода не будет гарантироваться).
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }

    lateinit var statesViewModel: StatesViewModel

    @Mock
    lateinit var statesRepo: IStatesRepo
    @Mock
    lateinit var helper : IPreferenceHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        statesViewModel = StatesViewModel(helper,
            ScheduleProviderStub(), statesRepo)
    }

    @Test //Проверим вызов метода getStates() у нашей ВьюМодели - должен вызываться 1 раз
    @Config(manifest=Config.NONE)
    fun getStates_Test() {

        Mockito.`when`(helper.isSorted()).thenReturn(true)
        Mockito.`when`(helper.getSortCase()).thenReturn(3)

        Mockito.`when`(statesRepo.getStates(true))
            .thenReturn( Single.just(listOf()))

        //вызываем метод statesViewModel
        statesViewModel.loadDataSealed(true)
        //проверяем что вызывается метод репозитория 1 раз
        verify(statesRepo, times(1)).getStates(true)
    }

    //протестируем работу LiveData
    // сначала Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
    //потом Убеждаемся, что Репозиторий вернул список размером = 0
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnValueIsNotNull() {
        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<StatesSealed> {} //переменная вынесена, чтобы удалить в конце
        //Получаем LiveData - переменная вынесена, чтобы удалить observer в конце
        val liveData = statesViewModel.getStatesSealed()

        //проверяем все варианты
       for(i in 0..1){
           for (sortType in 1..4) {
               val isSorted = i != 0
               Mockito.`when`(helper.isSorted()).thenReturn(isSorted)
               Mockito.`when`(helper.getSortCase()).thenReturn(sortType)

               //При вызове Репозитория возвращаем шаблонные данные
               Mockito.`when`(statesRepo.getStates(true))
                   .thenReturn(Single.just(listOf()))

               try {
                   //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
                   //Подписываемся на LiveData без учета жизненного цикла
                   liveData.observeForever(observer)
                   statesViewModel.loadDataSealed(true)

                   //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
                   Assert.assertNotNull(liveData.value)

                   //Убеждаемся, что Репозиторий вернул список размером = 0
                   val statesSealed: StatesSealed.Success = liveData.value as StatesSealed.Success
                   Assert.assertEquals(statesSealed.state.size, 0)

               } finally {
                   //Тест закончен, снимаем Наблюдателя
                   liveData.removeObserver(observer)
               }
           }
       }
    }

    // скармливаем ошибку и смотрим, что возвращается ошибка с заданным текстом сообщения
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<StatesSealed> {}
        val liveData = statesViewModel.getStatesSealed()
        val error = Throwable(ERROR_TEXT)

        //проверяем все варианты
        for(i in 0..1) {
            for (sortType in 1..4) {
                val isSorted = i != 0
                Mockito.`when`(helper.isSorted()).thenReturn(isSorted)
                Mockito.`when`(helper.getSortCase()).thenReturn(sortType)
                //При вызове Репозитория возвращаем ошибку
                Mockito.`when`(statesRepo.getStates(true))
                    .thenReturn(Single.error(error))

                try {
                    liveData.observeForever(observer)
                    statesViewModel.loadDataSealed(true)
                    //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
                    val value: StatesSealed.Error = liveData.value as StatesSealed.Error
                    Assert.assertEquals(error.message, value.error.message)
                } finally {
                    liveData.removeObserver(observer)
                }
            }
        }
    }


    //протестируем работу LiveData
    // Убеждаемся, что Репозиторий вернул строку "Россия"
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnSuccessValue() {
        val state = State(
            capital = "Moscow", flag = "", name = "Russia", region = "Europe",
            population = 146000000, area = 17000000f, latlng = arrayOf(60f, 40f),
            nameRus = "Россия", capitalRus = "Москва", regionRus = "Европа"
        )

        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<StatesSealed> {} //переменная вынесена, чтобы удалить в конце
        //Получаем LiveData - переменная вынесена, чтобы удалить observer в конце
        val liveData = statesViewModel.getStatesSealed()

        //проверяем все варианты
        for (i in 0..1) {
            for (sortType in 1..4) {
                val isSorted = i != 0
                Mockito.`when`(helper.isSorted()).thenReturn(isSorted)
                Mockito.`when`(helper.getSortCase()).thenReturn(sortType)

                //При вызове Репозитория возвращаем шаблонные данные
                Mockito.`when`(statesRepo.getStates(true))
                    .thenReturn(Single.just(listOf(state)))

                try {
                    //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
                    //Подписываемся на LiveData без учета жизненного цикла
                    liveData.observeForever(observer)
                    statesViewModel.loadDataSealed(true)

                    //Убеждаемся, что Репозиторий вернул строку "Россия"
                    val statesSealed: StatesSealed.Success = liveData.value as StatesSealed.Success
                    Assert.assertEquals("Россия", statesSealed.state[0].nameRus)

                } finally {
                    //Тест закончен, снимаем Наблюдателя
                    liveData.removeObserver(observer)
                }
            }
        }
    }
}