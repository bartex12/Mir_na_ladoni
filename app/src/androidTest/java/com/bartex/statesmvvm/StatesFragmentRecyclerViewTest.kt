package com.bartex.statesmvvm

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.bartex.statesmvvm.view.adapter.StateRVAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class StatesFragmentRecyclerViewTest {

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
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Ожидаем конкретного события: появления ресайклера rv_states
        //Это будет означать, что StatesFragment открылся и это поле видно на экране.
        val rv = uiDevice.wait(
            Until.findObject( By.res(packageName, "rv_states")),TIMEOUT
        )
        //Проверяем на null
        Assert.assertNotNull(rv)
    }

    //прокрутка до строки с определённым текстом
    @Test
    fun activitySearch_ScrollTo() {
        //Ожидаем конкретного события: появления ресайклера rv_states
        uiDevice.wait(
            Until.findObject(By.res(packageName, "rv_states")), TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //находим наш RecyclerView по id и вызываем у него Action scrollTo, который стал
        // нам доступен благодаря новой зависимости RecyclerViewActions.
        // Метод hasDescendant ищет вью с надписью “Гондурас”
        // И если находит, то проматывает список до этого элемента.
        // То же самое можно сделать с помощью метода scrollToPosition,
        // передавая индекс нужного элемента в качестве аргумента.
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.scrollTo<StateRVAdapter.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Гондурас"))
                ))

    }

    //прокрутка до строки с определённым номером и клик на ней
    @Test
    fun statesFragment_PerformClickAtPosition() {
        //Ожидаем конкретного события: появления строки с текстом Канада
        uiDevice.wait(
            Until.findObject(By.text("Канада")),
            TIMEOUT
        )
        //метод, который нажимает на элемент списка:
        //Тут мы вызываем метод actionOnItemAtPosition, который в качестве аргументов принимает
        // позицию элемента, с которым мы хотим взаимодействовать, и Action. Если запустить тест,
        // то вы увидите всплывающее уведомление (Mali - при определённой сортировке), позволяющее вам самостоятельно наблюдать
        // процесс исполнения теста.
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<StateRVAdapter.ViewHolder>(
                    23 , click()
                )
            )
    }
    //усложним задачу и будем нажимать на элемент списка, который не виден на экране.
    // Для этого мы объединим функционал предыдущих методов: прокрутим список до нужного
    // элемента и нажмем на него
    // тест работает нестабильно !!!  иногда при прокрутке к Norway
    // возникает сбой -> добавил uiDevice.waitForIdle()
    @Test
    fun  statesFragment_PerformClickOnItem() {
        //Ожидаем конкретного события: появления строки с текстом Канада
        uiDevice.wait(
            Until.findObject(By.text("Канада")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //Обратите внимание, что мы проматываем чуть ниже - до Norway. Это сделано для того,
        // чтобы искомый элемент был примерно посередине экрана.
        // Так работает промотка списка через метод scrollTo: .
        // Промотка останавливается, как только элемент появляется на экране.
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.scrollTo<StateRVAdapter.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Норвегия"))
                ))
        uiDevice.waitForIdle(1000L)
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItem<StateRVAdapter.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Италия")),
                    click()
                )
            )
        uiDevice.waitForIdle(1000L)  //чтобы контролировать на экране
    }

    //Убеждаемся, что DetailsFragment открывается
    @Test
    fun  statesFragment_OpenDetailsFragment() {
        //Ожидаем конкретного события: появления строки с текстом Канада
        uiDevice.wait(
            Until.findObject(By.text("Канада")),
            TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //Обратите внимание, что мы проматываем чуть ниже - до Norway. Это сделано для того,
        // чтобы искомый элемент был примерно посередине экрана.
        // Так работает промотка списка через метод scrollTo: .
        // Промотка останавливается, как только элемент появляется на экране.
        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.scrollTo<StateRVAdapter.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Норвегия"))
                ))

        Espresso.onView(withId(R.id.rv_states))
            .perform(
                RecyclerViewActions.actionOnItem<StateRVAdapter.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Италия")),
                    click()
                )
            )
        //Ожидаем конкретного события: появления появления текстового поля tv_state_name.
        //Это будет означать, что DetailsFragment открылся и это поле видно на экране
        val stateName = uiDevice.wait(
            Until.findObject(By.res(packageName, "tv_state_name")), TIMEOUT
        )
        uiDevice.waitForIdle(1000L)
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        Assert.assertEquals(stateName.text, "Италия")
    }

}