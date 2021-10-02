package com.bartex.statesmvvm.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.common.AlertDialogFragment
import com.bartex.statesmvvm.common.isOnline
import com.bartex.statesmvvm.network.OnlineLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


open class    MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var doubleBackToExitPressedOnce = false
    private lateinit var navController:NavController
    private lateinit var  appBarConfiguration:AppBarConfiguration
    private var oldTheme:Int = 1
    private var oldSort:Int = 1
    private var isOldSort:Boolean = true
    private var isOldLang :Boolean = true

    private var isNetworkAvailable: Boolean = true

    private lateinit var toolbar: Toolbar

    private lateinit var bottomNavigationState : BottomNavigationView

    companion object{
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
        const val TAG = "33333"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity onCreate ")

        //читаем сохранённную в настройках тему
        oldTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListColor", "1")!!.toInt()
        oldSort = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListSort", "1")!!.toInt()
        isOldSort = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("cbSort", true)
        isOldLang =PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("switchLang", true)

        //устанавливаем сохранённную в настройках тему
        when(oldTheme){
            1 -> setTheme(R.style.AppTheme)
            2 -> setTheme(R.style.AppThemeGreen)
            3 -> setTheme(R.style.AppThemePurple)
            4 -> setTheme(R.style.AppThemeRed)
            5 -> setTheme(R.style.AppThemeBlue)
            6 -> setTheme(R.style.AppThemeBlack)
        }
        //устанавливаем макет
        setContentView(R.layout.activity_main)

        bottomNavigationState = findViewById(R.id.bottom_navigation_state)
        bottomNavigationState.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //получаем статус сети при первом запуске приложения не через LiveData
        if (savedInstanceState == null) {
            isNetworkAvailable = isOnline(this)
            if (!isNetworkAvailable) {
                showNoInternetConnectionDialog()
            }
        }
        Log.d(TAG, "MainActivity onCreate  первый запуск isNetworkAvailable = $isNetworkAvailable")

        //следим за сетью через LiveData
        OnlineLiveData(this).observe(
            this@MainActivity,
            Observer<Boolean> {
                isNetworkAvailable = it
                if (!isNetworkAvailable) {
                    showNoInternetConnectionDialog()
                }
            })
        Log.d(TAG, "*** MainActivity onCreate  isNetworkAvailable = $isNetworkAvailable")

        //находим NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle)
        //поддержка экшенбара для создания строки поиска
        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration.Builder(navController.graph)
        //Отображать кнопку навигации как гамбургер , когда она не отображается как кнопка вверх
                .setOpenableLayout(drawer_layout)
                .build()
        //обработка событий экшенбара -гамбургер, кнопка вверх
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        //слушатель меню шторки -для обработки пунктов шторки
        nav_view.setNavigationItemSelectedListener(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomNavigationState.visibility = View.GONE
                R.id.statesFragment -> {
                    bottomNavigationState.visibility = View.VISIBLE
                    bottomNavigationState.selectedItemId = R.id.states
                }
                R.id.weatherFragment -> bottomNavigationState.visibility = View.GONE
                R.id.detailsFragment -> bottomNavigationState.visibility = View.GONE
                R.id.favoriteFragment -> {
                    bottomNavigationState.visibility = View.VISIBLE
                    bottomNavigationState.selectedItemId = R.id.liked
                }
                R.id.helpFragment -> bottomNavigationState.visibility = View.GONE
                R.id.settingsFragment -> bottomNavigationState.visibility = View.GONE
                R.id.flagsFragment -> {
                    bottomNavigationState.visibility = View.VISIBLE
                    bottomNavigationState.selectedItemId = R.id.flags
                }
                R.id.tabsFragment -> {
                    bottomNavigationState.visibility = View.VISIBLE
                    bottomNavigationState.selectedItemId = R.id.quiz
                }
                else -> {}
            }
        }

    }

    // Этот метод вызывается всякий раз, когда пользователь выбирает переход вверх
    // в иерархии действий приложения из панели действий (экшенбара)
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                ||super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        //при изменении темы и возвращении из настроек проверяем - какая тема установлена
       val newTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListColor", "1")!!.toInt()
        Log.d(TAG, "MainActivity onResume newTheme =$newTheme oldTheme = $oldTheme")
        val  newSort = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListSort", "1")!!.toInt()
       val newIsSort = PreferenceManager.getDefaultSharedPreferences(this)
           .getBoolean("cbSort", true)
        val newLang = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("switchLang", true)
        //если настройки изменились, пересоздаём активити, чтобы не париться с изменением данных
        if (newTheme != oldTheme || newSort!= oldSort || newIsSort!= isOldSort || newLang != isOldLang){
            recreate()
        }
    }

    fun getNetworkAvailable(): Boolean = isNetworkAvailable

    private fun showNoInternetConnectionDialog() {
        showAlertDialog(
            getString(R.string.dialog_title_device_is_offline),
            getString(R.string.dialog_message_device_is_offline)
        )
    }

    private fun showAlertDialog(title: String?, message: String?) {
        AlertDialogFragment.newInstance(title, message).show(
            supportFragmentManager,
            DIALOG_FRAGMENT_TAG
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "MainActivity onCreateOptionsMenu ")
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    //заголовки тулбара в зависимости от фрагмента прописаны в навигационном графе в label
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "MainActivity onPrepareOptionsMenu ")
        //нашел способ установить видимость иконок в тулбаре без перебора всех вариантов
        val id = navController.currentDestination?.id
        //видимость иконок в тулбаре
        id?. let {
            menu?.findItem(R.id.search_toolbar)?.isVisible = it == R.id.statesFragment
            menu?.findItem(R.id.help_toolbar)?.isVisible = it!= R.id.helpFragment
                    && it!= R.id.homeFragment && it!= R.id.weatherFragment
            menu?.findItem(R.id.settings_toolbar)?.isVisible = it != R.id.settingsFragment
                    && it!= R.id.homeFragment && it!= R.id.weatherFragment
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //меню тулбара
    //так  как Избранное - детали и справка - настройки могут бесконечно вызываться друг из друга
    //чтобы это предотвратить, в их action было задействовано popUpTo и popUpToInclusive
    //но чтобы здесь использовать action нужно navigate to action, а не фрагмент
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val destinationId = navController.currentDestination?.id
        when (id){
            R.id.settings_toolbar -> {
                when(destinationId){
                 R.id.helpFragment -> navController.navigate(R.id.action_helpFragment_to_settingsFragment)
                    R.id.flagsFragment-> navController.navigate(R.id.action_flagsFragment_to_settingsFragment)
                    R.id.favoriteFragment->navController.navigate(R.id.action_favoriteFragment_to_settingsFragment)
                    R.id.tabsFragment->navController.navigate(R.id.action_tabsFragment_to_settingsFragment)
                    R.id.statesFragment->navController.navigate(R.id.action_statesFragment_to_settingsFragment)
                    R.id.weatherFragment->navController.navigate(R.id.action_weatherFragment_to_settingsFragment)
                    R.id.detailsFragment->navController.navigate(R.id.action_detailsFragment_to_settingsFragment)
                }
                return true
            }
            R.id.help_toolbar -> {
                when(destinationId){
                    R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_helpFragment)
                    R.id.flagsFragment->navController.navigate(R.id.action_flagsFragment_to_helpFragment)
                    R.id.favoriteFragment->navController.navigate(R.id.action_favoriteFragment_to_helpFragment)
                    R.id.tabsFragment->navController.navigate(R.id.action_tabsFragment_to_helpFragment)
                    R.id.statesFragment->navController.navigate(R.id.action_statesFragment_to_helpFragment)
                    R.id.weatherFragment->navController.navigate(R.id.action_weatherFragment_to_helpFragment)
                    R.id.detailsFragment->navController.navigate(R.id.action_detailsFragment_to_helpFragment)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // нижнее меню
    //чтобы при многократном вызове фрагментов друг из друга в стэке не скапливались фрагменты,
    //для всех action сделан popUp to statesFragment и для statesFragment сделан launchSingleTop
    private val  onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val itemId =item.itemId
            val destinationId = navController.currentDestination?.id
            when (itemId){
                R.id.states -> {
                    when(destinationId){
                        R.id.flagsFragment-> navController.navigate(R.id.action_flagsFragment_to_statesFragment)
                        R.id.favoriteFragment-> navController.navigate(R.id.action_favoriteFragment_to_statesFragment)
                        R.id.tabsFragment -> navController.navigate(R.id.action_tabsFragment_to_statesFragment)
                    }
                }
                R.id.flags -> {
                    when(destinationId){
                        R.id.statesFragment-> navController.navigate(R.id.action_statesFragment_to_flagsFragment)
                        R.id.favoriteFragment-> navController.navigate(R.id.action_favoriteFragment_to_flagsFragment)
                        R.id.tabsFragment -> navController.navigate(R.id.action_tabsFragment_to_flagsFragment)
                    }
                }
                R.id.liked -> {
                    when(destinationId){
                        R.id.statesFragment->navController.navigate(R.id.action_statesFragment_to_favoriteFragment)
                        R.id.flagsFragment->navController.navigate(R.id.action_flagsFragment_to_favoriteFragment)
                        R.id.tabsFragment ->navController.navigate(R.id.action_tabsFragment_to_favoriteFragment)
                    }
                }
                R.id.quiz -> {
                    when(destinationId){
                        R.id.statesFragment->navController.navigate(R.id.action_statesFragment_to_tabsFragment)
                        R.id.favoriteFragment->navController.navigate(R.id.action_favoriteFragment_to_tabsFragment)
                        R.id.flagsFragment->navController.navigate(R.id.action_flagsFragment_to_tabsFragment)
                    }
                }
                else -> {}
            }
            true
        }

    //меню шторки
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_states -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_states")
                navController.navigate(R.id.statesFragment)
            }
            R.id.nav_quiz -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_quiz")
                navController.navigate(R.id.tabsFragment)
            }
            R.id.nav_setting -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_setting")
                navController.navigate(R.id.settingsFragment)
            }
            R.id.nav_help -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_help")
                navController.navigate(R.id.helpFragment)
            } 
            R.id.nav_share -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_share")
                //поделиться - передаём ссылку на приложение в маркете
                shareApp()
            }
            R.id.nav_rate -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_send")
                //оценить приложение - попадаем на страницу приложения в маркете
                rateApp()
            }
        }
        // Выделяем выбранный пункт меню в шторке
        item.isChecked = true
        drawer_layout.closeDrawer(GravityCompat.START)
        return false
    }

    private fun shareApp() {
        val sendIntent = Intent()
        with(sendIntent){
            type = "text/plain"
            //чтобы отменить неправильный вариант Запомнить выбор - убрать и вставить эту строку
            action = Intent.ACTION_SEND_MULTIPLE //задаём возможность отправки несколькими способами
            putExtra(
                Intent.EXTRA_TEXT,
                """ 
                    ${getString(R.string.header)}
                    
                    ${getString(R.string.uri_stor)}
                    
                    ${getString(R.string.blok1)}
                    
                    ${getString(R.string.blok2)}
                    
                    ${getString(R.string.blok3)}
                    
                    ${getString(R.string.blok4)}
                    
                    ${getString(R.string.blok5)}
                """.trimIndent()
            )
        }
        startActivity(sendIntent)
    }

    private fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.uri_stor))
        startActivity(intent)
    }

    //при нажатии на кнопку Назад если фрагмент реализует BackButtonListener, вызываем метод backPressed
    //при этом если мы в homeFragment   - выходим из приложения по двойному щелчку,
    // а если в другом экране - делаем то, что там прописано
    override fun onBackPressed() {
        //если мы в homeFragment, то при нажатии Назад показываем Snackbar и при повторном
        //нажати в течении 2 секунд закрываем приложение
        Log.d(
            TAG,
            "MainActivity onBackPressed  Destination = ${navController.currentDestination?.label}"
        )
        if( navController.currentDestination?.id  == R.id.homeFragment){
            Log.d(TAG, "MainActivity onBackPressed  это StatesFragment")
            //если флаг = true - а это при двойном щелчке - закрываем программу
            if (doubleBackToExitPressedOnce) {
                Log.d(TAG, "MainActivity onBackPressed  doubleBackToExitPressedOnce")
                super.onBackPressed()
                return
            }
            doubleBackToExitPressedOnce = true //выставляем флаг = true
            //закрываем шторку, если была открыта
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            //показываем Snackbar: Для выхода нажмите  НАЗАД  ещё раз
            Snackbar.make(
                findViewById(android.R.id.content), this.getString(R.string.forExit),
                Snackbar.LENGTH_SHORT
            ).show()
            //запускаем поток, в котором через 2 секунды меняем флаг
            Handler(Looper.getMainLooper())
                .postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }else{
            Log.d(TAG, "MainActivity onBackPressed  это НЕ StatesFragment ")
            super.onBackPressed()
        }
    }


}