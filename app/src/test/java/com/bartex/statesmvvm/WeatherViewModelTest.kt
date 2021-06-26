package com.bartex.statesmvvm

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bartex.statesmvvm.model.entity.state.State
import com.bartex.statesmvvm.model.entity.weather.WeatherInCapital
import com.bartex.statesmvvm.model.entity.weather.WeatherQuery
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.model.repositories.weather.IWeatherRepo
import com.bartex.statesmvvm.view.fragments.scheduler.ScheduleProviderStub
import com.bartex.statesmvvm.view.fragments.states.StatesSealed
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.bartex.statesmvvm.view.fragments.weather.WeatherSealed
import com.bartex.statesmvvm.view.fragments.weather.WeatherViewModel
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
class WeatherViewModelTest {
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

    lateinit var weatherViewModel: WeatherViewModel

    @Mock
    lateinit var weatherRepo: IWeatherRepo
    @Mock
    lateinit var helper : IPreferenceHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        weatherViewModel = WeatherViewModel(helper,
            ScheduleProviderStub(), weatherRepo)
    }

    //протестируем работу WeatherViewModel
    //Проверим вызов метода getWeatherInCapital() у нашей ВьюМодели - должен вызываться 1 раз
    @Test
    @Config(manifest=Config.NONE)
    fun getWeatherInCapital_Test() {

        val state = State()

        Mockito.`when`(weatherRepo.getWeatherInCapital(
            true, state.capital, WeatherQuery.keyApi, WeatherQuery.units))
            .thenReturn( Single.just(WeatherInCapital()))

        //вызываем метод statesViewModel
        weatherViewModel.loadWeatherSealed(state, true)
        //проверяем что вызывается метод репозитория 1 раз
        verify(weatherRepo, times(1)).getWeatherInCapital(
            true, null, WeatherQuery.keyApi, WeatherQuery.units )
    }

    //протестируем работу LiveData
    // сначала Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
    //потом Убеждаемся,  что Репозиторий вернул пустое имя = ""
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnValueIsNotNull() {
        val state = State()
        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<WeatherSealed> {} //переменная вынесена, чтобы удалить в конце
        //Получаем LiveData - переменная вынесена, чтобы удалить observer в конце
        val liveData = weatherViewModel.getWeatherSealed()

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(weatherRepo.getWeatherInCapital(
            true, state.capital, WeatherQuery.keyApi, WeatherQuery.units))
            .thenReturn( Single.just(WeatherInCapital()))

        try {
            //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            //вызываем метод statesViewModel
            weatherViewModel.loadWeatherSealed(state, true)

            //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
            Assert.assertNotNull(liveData.value)

            //Убеждаемся, что Репозиторий вернул имя = ""
            val weatherSealed: WeatherSealed.Success = liveData.value as WeatherSealed.Success
            Assert.assertEquals( "", weatherSealed.weather.name)

        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    //протестируем работу LiveData
    // Убеждаемся, что Репозиторий вернул строку "Moscow"
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnSuccessValue() {
        val state = State(capital = "Moscow", flag = "", name = "Russia", region = "Europe",
            population = 146000000, area = 17000000f, latlng = arrayOf(60f, 40f) ,
            nameRus = "Россия", capitalRus = "Москва", regionRus ="Европа" )

        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<WeatherSealed> {} //переменная вынесена, чтобы удалить в конце
        //Получаем LiveData - переменная вынесена, чтобы удалить observer в конце
        val liveData =  weatherViewModel.getWeatherSealed()

        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(weatherRepo.getWeatherInCapital(
            true, state.capital, WeatherQuery.keyApi, WeatherQuery.units))
            .thenReturn( Single.just(WeatherInCapital(name = "Moscow")))

        try {
            //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            //вызываем метод statesViewModel
            weatherViewModel.loadWeatherSealed(state, true)

            //Убеждаемся, что Репозиторий вернул строку "Moscow"
            val weatherSealed: WeatherSealed.Success = liveData.value as WeatherSealed.Success
            Assert.assertEquals("Moscow", weatherSealed.weather.name)

        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    //протестируем работу LiveData
    // скармливаем ошибку и смотрим, что возвращается ошибка с заданным текстом сообщения
    @Test
    @Config(manifest=Config.NONE)
    fun liveData_TestReturnValueIsError() {

        val state = State()
        val observer = Observer<WeatherSealed> {}
        val liveData =  weatherViewModel.getWeatherSealed()
        val error = Throwable(ERROR_TEXT)

        //При вызове Репозитория возвращаем ошибку
        Mockito.`when`(weatherRepo.getWeatherInCapital(
            true, state.capital, WeatherQuery.keyApi, WeatherQuery.units))
            .thenReturn( Single.error(error))

        try {
            liveData.observeForever(observer)
            //вызываем метод statesViewModel
            weatherViewModel.loadWeatherSealed(state, true)
            //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
            val value: WeatherSealed.Error = liveData.value as WeatherSealed.Error
            Assert.assertEquals(error.message, value.error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }
}