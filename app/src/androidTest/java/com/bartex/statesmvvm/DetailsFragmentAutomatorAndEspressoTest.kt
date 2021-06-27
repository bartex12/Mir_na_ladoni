package com.bartex.statesmvvm

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


//по непонятным причинам первый запуск теста может вызывать ошибку-
// тогда нужно просто перезапустить
@RunWith(AndroidJUnit4ClassRunner::class)
class DetailsFragmentAutomatorAndEspressoTest {

    companion object {
        private const val TIMEOUT = 10000L
    }
    //Класс UiDevice предоставляет доступ к  устройству.
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()
    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup(){
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()
        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)
        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    //Открываем StatesFragment, крутим список до 22 элемента , кликаем по нему
    //ждём переключения на DetailsFragment
    //находим на экране вьюху  с id = btn_removeFavorite - Удалить из избранного
    //если она не null, кликаем на ней
    //и проверяем, что становится видна кнопка Добавить в избранное
    //в противном случае кликаем на кнопке Добавить в избранное
    //и проверяем, что становится видна кнопка Удалить из избранного
    @Test
    fun  detailsFragment_ChangeFavoriteButtonState() {
        //Ожидаем конкретного события: появления ресайклера rv_states
        uiDevice.wait(
            Until.findObject(By.res(packageName, "rv_states")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //нажимаем на элемент списка:
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<StateRVAdapter.ViewHolder>(
                    22 , ViewActions.click()
                )
            )
        //Ожидаем конкретного события: появления появления текстового поля tv_state_name.
        //Это будет означать, что DetailsFragment открылся и это поле видно на экране
        uiDevice.wait(
            Until.findObject(By.res(packageName, "tv_state_name")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //находим на экране вьюху  с id = btn_removeFavorite - Удалить из избранного
        val btnRemoveFavorite =  uiDevice.findObject(By.res(packageName, "btn_removeFavorite"))
        //если она не null, кликаем на ней
        //и проверяем, что становится видна кнопка Добавить в избранное
        //если это так, то сликаем на ней и смотрим, что кнопка изменилась
        if (btnRemoveFavorite != null){
            Espresso.onView(withId(R.id.btn_removeFavorite)).perform(ViewActions.click())
            //на вью с id btn_addToFavorite проконтролировать что кнопка полностью отображается на экране
            Espresso.onView(withId(R.id.btn_addToFavorite))
                .check(ViewAssertions
                    .matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }else{
            //в противном случае кликаем на кнопке Добавить в избранное
            //и проверяем, что становится видна кнопка Удалить из избранного
            Espresso.onView(withId(R.id.btn_addToFavorite)).perform(ViewActions.click())
            //на вью с id btn_removeFavorite проконтролировать что кнопка полностью отображается на экране
            Espresso.onView(withId(R.id.btn_removeFavorite))
                .check(ViewAssertions
                    .matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }
    }

    //проверяем надписи на текстовых полях при условии щелчка на определённой строке при
    // русском языке в настройках и сортировке по умолчанию !!!
    @Test
    fun  detailsFragment_CheckDetailFragmentTextFields() {
        //Ожидаем конкретного события: появления ресайклера rv_states
        uiDevice.wait(
            Until.findObject(By.res(packageName, "rv_states")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //нажимаем на элемент списка:
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<StateRVAdapter.ViewHolder>(
                    1 , ViewActions.click()
                )
            )
        //Ожидаем конкретного события: появления появления текстового поля tv_state_name.
        //Это будет означать, что DetailsFragment открылся и это поле видно на экране
        uiDevice.wait(
            Until.findObject(By.res(packageName, "tv_state_name")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //на вью  с id = tv_state_name проверить соответствие тексту "Канада" и тд
        Espresso.onView(withId(R.id.tv_state_name))
            .check(ViewAssertions.matches(ViewMatchers.withText("Канада")))
        Espresso.onView(withId(R.id.tv_state_region))
            .check(ViewAssertions.matches(ViewMatchers.withText("Регион:   АМЕРИКА ")))
        Espresso.onView(withId(R.id.tv_state_area))
            .check(ViewAssertions.matches(ViewMatchers.withText("Площадь: 10,0 млн. кв. км.")))
        Espresso.onView(withId(R.id.tv_state_population))
            .check(ViewAssertions.matches(ViewMatchers.withText("Население: 36,2 млн. чел.")))
        Espresso.onView(withId(R.id.tv_state_capital))
            .check(ViewAssertions.matches(ViewMatchers.withText("Столица:   ОТТАВА ")))
    }
}