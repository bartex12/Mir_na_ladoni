package com.bartex.statesmvvm

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BottomMenuTest {

    companion object {
        private const val TIMEOUT = 10000L
    }

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
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

        //переходим во фрагмент детализации
        goToDetails()

    }

    //кликаем на 4 позиции списка, переходим в Детали, кликаем на иконке нижнего меню Карта
    //переходим в карты Google проверяем что находимся в карте
    @Test
    fun  detailsFragment_OpenMap() {

        //Находим иконку BottomMenu Карта
        val toMap: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/geo")
        )
        //Кликаем по ней
        toMap.click()
        //находим вью с id = action_bar_root, что значит, что карта открылась
        val action_bar_root: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.google.android.apps.maps:id/action_bar_root")
        )
        //Проверяем на null
        Assert.assertNotNull(action_bar_root)
    }

    //кликаем на 4 позиции списка, переходим в Детали, кликаем на иконке нижнего меню Вики
    //переходим в Википедию проверяем это
    @Test
    fun  detailsFragment_OpenWiki() {

        //Находим иконку BottomMenu Вики
        val toMap: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/wiki")
        )
        //Кликаем по ней
        toMap.click()
        //находим вью с id = action_bar_root, что значит, что карта открылась
        val browser: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.android.browser:id/webview_wrapper")
        )
        //Проверяем на null
        Assert.assertNotNull(browser)
    }

    //кликаем на 4 позиции списка, переходим в Детали, кликаем на иконке нижнего меню Погода
    //переходим во фрагмент с погодой - при наличии интернета - проверяем это
    @Test
    fun  detailsFragment_OpenWeather() {

        //Находим иконку BottomMenu Погода
        val toMap: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/weather")
        )
        //Кликаем по ней
        toMap.click()
        //находим вью с id = action_bar_root, что значит, что карта открылась
        val capital_name: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/tv_capital_name")
        )
        //Проверяем на null
        Assert.assertNotNull(capital_name)
    }

    private fun goToDetails() {
        //Ожидаем конкретного события: появления строки с текстом Канада
        uiDevice.wait(
            Until.findObject(By.text("Канада")),
            TIMEOUT
        )
        //нажимаем на элемент списка:
        Espresso.onView(ViewMatchers.withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<StateRVAdapter.ViewHolder>(
                    4, ViewActions.click()
                )
            )
        //Ожидаем конкретного события: появления текстового поля tv_state_name.
        //Это будет означать, что DetailsFragment открылся и это поле видно на экране
        uiDevice.wait(
            Until.findObject(By.res(packageName, "tv_state_name")),
            TIMEOUT
        )
    }
}