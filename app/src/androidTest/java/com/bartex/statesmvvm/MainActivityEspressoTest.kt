package com.bartex.statesmvvm

import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.bartex.statesmvvm.view.main.MainActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    // MainActivity NotNull
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    // MainActivity в RESUMED
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    // элементы меню, которые при запуске  - NotNull  - открыт фрагмент StatesFragment
    fun activityToolbarMenuitem_NotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it.findViewById<ActionMenuItemView>(R.id.search))
            TestCase.assertNotNull(it.findViewById<ActionMenuItemView>(R.id.favorites))

            TestCase.assertNull(it.findViewById<ActionMenuItemView>(R.id.navigation_flags))
            TestCase.assertNull(it.findViewById<ActionMenuItemView>(R.id.navigation_settings))
            TestCase.assertNull(it.findViewById<ActionMenuItemView>(R.id.navigation_help))
        }
    }
    @Test
    //проверяем видимость элементов на экране при запуске - открыт фрагмент StatesFragment
    //Метод withEffectiveVisibility() возвращает VISIBLE только если все вью в иерархии видны
    fun activityToolbarMenuitem_AreEffectiveVisible() {
       onView(withId(R.id.search))
           .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.favorites))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }


    @After
    fun close() {
        scenario.close()
    }
}