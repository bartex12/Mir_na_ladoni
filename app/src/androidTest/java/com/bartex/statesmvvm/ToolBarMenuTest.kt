package com.bartex.statesmvvm

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ToolBarMenuTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()
    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    companion object {
        private const val TIMEOUT = 5000L
    }

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()
        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)
        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

   // Проверяем открытие экрана избранное после щелчка на иконке тулбара
    // в uiautomatorviewer смотрим resourceId иконки = com.bartex.states_search:id/favorites
    @Test
    fun test_OpenFavoriteScreen() {
        uiDevice.waitForIdle(1000L)
       //Находим иконку тулбара Избранное
        val toFavorite: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/favorites")
        )
        //Кликаем по ней
         toFavorite.click()
        //Ожидаем конкретного события: появления ресайклера rv_favorite
        //Это будет означать, что FavoritesFragment открылся и rv_favorite видно на экране.
        val favorite = uiDevice.wait(
            Until.findObject(By.res(packageName, "rv_favorite")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //Проверяем на null
        Assert.assertNotNull(favorite)
    }

    // Проверяем поиск по списку в StatesQuizFragment
    // в uiautomatorviewer смотрим resourceId иконки = com.bartex.states_search:id/search
    @Test
    fun test_OpenSearchWidget() {
        uiDevice.waitForIdle(1000L)
        //Находим иконку тулбара Поиск
        val search: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/search")
        )
        //Кликаем по ней
        search.click()
        //Ожидаем конкретного события: появления EditText search_src_text
        val searchEditText: UiObject =  uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/search_src_text")
        )
        uiDevice.waitForIdle(1000L)
        //Проверяем на null
        Assert.assertNotNull(searchEditText)

        //Выделяем по тапу наш EditText
        Espresso.onView(withId(R.id.search_src_text)).perform(ViewActions.click())
        //Вставляем в него текст запроса
        Espresso.onView(withId(R.id.search_src_text))
            .perform(ViewActions.replaceText("Канада"), ViewActions.closeSoftKeyboard())

        val nameOfState: UiObject = uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/search_src_text"))
        //Проверяем на null
        Assert.assertEquals("Канада",nameOfState.text )
    }

    // Проверяем открытие экрана Флаги после щелчка на иконке тулбара
    // в uiautomatorviewer смотрим resourceId иконки = com.bartex.states_search:id/favorites
    @Test
    fun test_OpenFlafgScreen() {
        uiDevice.waitForIdle(1000L)
        //Находим иконку
       // прокатило описание для ImageView description - id здесь нет, текст не катит
        val toFlags: UiObject =uiDevice.findObject( UiSelector().description("Ещё"))
        //Кликаем по ней
        toFlags.click()
        //Находим строчку
        val flags: UiObject2 = uiDevice.findObject(By.text( "Флаги" ))
        //Кликаем по ней
        flags.click()
        //Ожидаем конкретного события: появления ресайклера rv_flags
        //Это будет означать, что FlagsQuizFragment открылся и rv_flags видно на экране.
        val rv_flags = uiDevice.wait(
            Until.findObject(By.res(packageName, "rv_flags")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //Проверяем на null
        Assert.assertNotNull(rv_flags)
    }

    // Проверяем открытие экрана Настройки после щелчка на иконке тулбара
    // и выборе строки Настройки
    @Test
    fun test_OpenSettingsScreen() {
        uiDevice.waitForIdle(1000L)
        //Находим иконку
        // прокатило описание для ImageView description - id здесь нет, текст не катит
        val elseItem: UiObject =uiDevice.findObject( UiSelector().description("Ещё"))
        //Кликаем по ней
        elseItem.click()
        //Находим строчку
        val toSettings: UiObject2 = uiDevice.findObject(By.text( "Настройки" ))
        //Кликаем по ней
        toSettings.click()
        //Ожидаем конкретного события: появления переключателя языка в настройках
        //Это будет означать, что фрагмент настроек  открылся
        val switchWidget: UiObject = uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/switchWidget"))
        uiDevice.waitForIdle(1000L)
        //Проверяем на null
        Assert.assertNotNull(switchWidget)
    }

    // Проверяем открытие экрана Справка после щелчка на иконке тулбара
    // и выборе строки Справка
    @Test
    fun test_OpenHelpScreen() {
        uiDevice.waitForIdle(1000L)
        //Находим иконку
        // прокатило описание для ImageView description - id здесь нет, текст не катит
        val elseItem: UiObject =uiDevice.findObject( UiSelector().description("Ещё"))
        //Кликаем по ней
        elseItem.click()
        //Находим строчку
        val toHelp: UiObject2 = uiDevice.findObject(By.text( "Справка" ))
        //Кликаем по ней
        toHelp.click()
        //Ожидаем конкретного события: появления текста справки
        //Это будет означать, что фрагмент справки  открылся
        val textHelp: UiObject = uiDevice.findObject(
            UiSelector().resourceId("com.bartex.states_search:id/tv_help"))
        uiDevice.waitForIdle(1000L)
        //Проверяем на null
        Assert.assertNotNull(textHelp)
    }
}