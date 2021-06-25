package com.bartex.statesmvvm

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bartex.statesmvvm.dagger.DaggerAppComponent
import com.bartex.statesmvvm.dagger.PrefModule
import com.bartex.statesmvvm.model.repositories.prefs.IPreferenceHelper
import com.bartex.statesmvvm.model.repositories.states.IStatesRepo
import com.bartex.statesmvvm.view.fragments.states.StatesViewModel
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import dagger.internal.DaggerCollections
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import javax.inject.Inject


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

    @Inject
    lateinit var statesRepo: IStatesRepo
    @Inject
    lateinit var helper : IPreferenceHelper
    @Inject
    lateinit var mainThreadScheduler: Scheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        statesViewModel = StatesViewModel()

        helper = mock(IPreferenceHelper::class.java)
        statesRepo = mock(IStatesRepo::class.java)
        statesViewModel = StatesViewModel()

    }

    @Test //Проверим вызов метода getStates() у нашей ВьюМодели
    @Config(manifest=Config.NONE)
    fun getStates_Test() {

        Mockito.`when`(helper.isSorted()).thenReturn(true)
        Mockito.`when`(helper.getSortCase()).thenReturn(3)

        Mockito.`when`(statesRepo.getStates(true)).thenReturn( Single.just(listOf()))

        //вызываем метод statesViewModel
        statesViewModel.loadDataSealed(true)
        //проверяем что вызывается метод репозитория 1 раз
        verify(statesRepo, times(1)).getStates(true)
    }

//    //протестируем работу LiveData
//    @Test
//    fun liveData_TestReturnValueIsNotNull() {
//        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
//        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
//        val observer = Observer<StatesSealed> {} //переменная вынесена, чтобы удалить в конце
//        //Получаем LiveData - переменная вынесена, чтобы удалить observer в конце
//        val liveData = statesViewModel.subscribeToLiveData()
//
//        //При вызове Репозитория возвращаем шаблонные данные
//        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
//            Observable.just(SearchResponse(1,listOf() ) )
//        )
//
//        try {
//            //этот метод позволяет подписаться на уведомления и не отписываться от них никогда
//            //Подписываемся на LiveData без учета жизненного цикла
//            liveData.observeForever(observer)
//            searchViewModel.searchGitHub(SEARCH_QUERY)
//
//            //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
//            Assert.assertNotNull(liveData.value)
//
//            //Убеждаемся, что Репозиторий вернул totalCount = 1
//            val sus = liveData.value as ScreenState.Working
//            Assert.assertEquals(1, sus.searchResponse.totalCount)
//        } finally {
//            //Тест закончен, снимаем Наблюдателя
//            liveData.removeObserver(observer)
//        }
//    }

}