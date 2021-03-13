package com.bartex.statesmvvm.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.repositories.prefs.PreferenceHelper
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class    MainActivity: AppCompatActivity(),
     SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private var doubleBackToExitPressedOnce = false
    lateinit var navController:NavController
    lateinit var  appBarConfiguration:AppBarConfiguration
    private var oldTheme:Int = 1

    companion object{
        const val TAG = "33333"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity onCreate ")

        //читаем сохранённную в настройках тему
        oldTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListColor", "1")!!.toInt()
        //устанавливаем сохранённную в настройках тему
        when(oldTheme){
            1->setTheme(R.style.AppTheme)
            2->setTheme(R.style.AppThemeGreen)
            3->setTheme(R.style.AppThemePurple)
            4->setTheme(R.style.AppThemeRed)
        }
        setContentView(R.layout.activity_main)

        //находим NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
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
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity onResume currentDestination =" +
                " ${navController.currentDestination?.label}")

//            navController.currentDestination?.id?.  let{
//                navController.navigate(it)
//        }

//        //чтобы после возврата из SettingsActivity автоматически происходило обновление
//        //нужно принудительно делать вызов фрагмента который уже есть но у него теперь другие параметры
//        if (navController.currentDestination?.id == R.id.wikiFragment){
//            navController.currentDestination?.id?.  let{
//                navController.navigate(it)
//        }
//        }else{
//            navController.navigate(R.id.statesFragment)
//        }

        //при изменении темы и возвращении из настроек проверяем - какая тема установлена
       val newTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("ListColor", "1")!!.toInt()
        Log.d(TAG, "MainActivity onResume newTheme =$newTheme oldTheme = $oldTheme")
        //если тема изменилась, пересоздаём активити
        if (newTheme != oldTheme) recreate()
    }

    // Этот метод вызывается всякий раз, когда пользователь выбирает переход вверх
    // в иерархии действий приложения из панели действий (экшенбара)
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                ||super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "MainActivity onCreateOptionsMenu ")
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView =searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "MainActivity onPrepareOptionsMenu ")
        //нашел способ установить видимость иконок в тулбаре без перебора всех вариантов
        val id = navController.currentDestination?.id
        //видимость иконок в тулбаре
        id?. let {
            menu?.findItem(R.id.search)?.isVisible = it == R.id.statesFragment
            menu?.findItem(R.id.navigation_help)?.isVisible = it!= R.id.helpFragment
            menu?.findItem(R.id.favorites)?.isVisible =
                it!= R.id.favoriteFragment && it!= R.id.weatherFragment && it!= R.id.helpFragment

            //заголовки тулбара в зависимости от фрагмента
            toolbar.title = when(it){
                R.id.statesFragment -> getString(R.string.app_name)
                R.id.searchFragment -> getString(R.string.search_name)
                R.id.weatherFragment -> getString(R.string.weather_name)
                R.id.detailsFragment -> getString(R.string.details_name)
                R.id.favoriteFragment -> getString(R.string.favorite_name)
                R.id.helpFragment -> getString(R.string.help_name)
                else -> getString(R.string.app_name)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //так  как Избранное - детали и справка - настройки могут бесконечно вызываться друг из друга
    //чтобы это предотвратить, в их action было задействовано popUpTo и popUpToInclusive
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id){
            R.id.favorites ->{
                navController.navigate(R.id.favoriteFragment)
                return true
            }
           R.id.navigation_settings ->{
               navController.navigate(R.id.settingsActivity)
               return true
           }
            R.id.navigation_help->{
                navController.navigate(R.id.helpFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "MainActivity onQueryTextSubmit query = $query ")
        query?. let{
            val bundle = Bundle().apply { putString(Constants.SEARCH, it) }
            navController.navigate(R.id.action_statesFragment_to_searchFragment, bundle)
            }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_favorites -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_favorites")
                navController.navigate(R.id.action_statesFragment_to_favoriteFragment)
            }
            R.id.nav_setting -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_setting")
                navController.navigate(R.id.action_statesFragment_to_settingsActivity)
            }
            R.id.nav_help -> {

            Log.d(TAG, "MainActivity onNavigationItemSelected nav_help")
                    navController.navigate(R.id.action_statesFragment_to_helpFragment)
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
            putExtra(Intent.EXTRA_TEXT,
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
    //при этом если мы в списке стран - выходим из приложения по двойному щелчку,
    // а если в другом экране - делаем то, что там прописано
    override fun onBackPressed() {
        //если мы в StatesFragment, то при нажатии Назад показываем Snackbar и при повторном
        //нажати в течении 2 секунд закрываем приложение
        Log.d(TAG, "MainActivity onBackPressed  Destination = ${navController.currentDestination?.label}")
        if( navController.currentDestination?.id  == R.id.statesFragment){
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
                Snackbar.LENGTH_SHORT).show()
            //запускаем поток, в котором через 2 секунды меняем флаг
            Handler(Looper.getMainLooper())
                .postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }else{
            Log.d(TAG, "MainActivity onBackPressed  это НЕ StatesFragment ")
            super.onBackPressed()
        }
    }
}