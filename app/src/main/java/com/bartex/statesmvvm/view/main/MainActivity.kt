package com.bartex.statesmvvm.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bartex.statesmvvm.App
import com.bartex.statesmvvm.R
import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.view.fragments.details.DetailsFragment
import com.bartex.statesmvvm.view.fragments.favorite.FavoriteFragment
import com.bartex.statesmvvm.view.fragments.help.HelpFragment
import com.bartex.statesmvvm.view.fragments.search.SearchFragment
import com.bartex.statesmvvm.view.fragments.states.StatesFragment
import com.bartex.statesmvvm.view.fragments.weather.WeatherFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject


class    MainActivity: AppCompatActivity(),
     SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private var doubleBackToExitPressedOnce = false
    private var toggle:ActionBarDrawerToggle? = null
    lateinit var navController:NavController

    companion object{
        const val TAG = "33333"
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = SupportAppNavigator(this, supportFragmentManager,
        R.id.container
    )

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity onCreate ")
        setContentView(R.layout.activity_main)

        //находим NavController
        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)

        setSupportActionBar(toolbar) //поддержка экшенбара для создания строки поиска
        toggle = ActionBarDrawerToggle(this,drawer_layout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close )//гамбургер
        toggle?. let{ drawer_layout.addDrawerListener(it)}  //слушатель гамбургера
        toggle?.syncState() //синхронизация гамбургера

        //https://stackoverflow.com/questions/28531503/toolbar-switching-from-drawer-to-back-
        // button-with-only-one-activity/29292130#29292130
        //если в BackStack больше одного фрагмента (там почему то всегда есть 1 фрагмент)
        //то отображаем стрелку назад и устанавливаем слушатель на щелчок по ней с действием
        //onBackPressed(), иначе отображаем гамбургер и по щелчку открываем шторку
        supportFragmentManager.addOnBackStackChangedListener {  //слушатель BackStack
            if(supportFragmentManager.backStackEntryCount > 1){
                supportActionBar?.setDisplayHomeAsUpEnabled(true) //показать стрелку
                toolbar.setNavigationOnClickListener { // слушатель кнопки навигации- стрелка
                    onBackPressed()
                }
            }else{
                supportActionBar?.setDisplayHomeAsUpEnabled(false) //не показывать стрелку
                toggle?.syncState()
                toolbar.setNavigationOnClickListener {// слушатель кнопки навигации- гамбургер
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }

        nav_view.setNavigationItemSelectedListener(this) //слушатель меню шторки
        App.instance.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity onResume ")
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
        //нашел способ установить видимость иконок в тулбаре без перебора всех вариантов
        val id = navController.currentDestination?.id

        //видимость иконок в тулбаре
        id?. let {
            menu?.findItem(R.id.search)?.isVisible = it == R.id.statesFragment
            menu?.findItem(R.id.navigation_help)?.isVisible = it!= R.id.helpFragment
            menu?.findItem(R.id.navigation_settings)?.isVisible = it!= R.id.prefFragment
            menu?.findItem(R.id.favorites)?.isVisible =
                it!= R.id.favoriteFragment && it!= R.id.weatherFragment
                        && it!= R.id.prefFragment && it!= R.id.helpFragment

            //заголовки тулбара в зависимости от фрагмента
            toolbar.title = when(it){
                R.id.statesFragment -> getString(R.string.app_name)
                R.id.searchFragment -> getString(R.string.search_name)
                R.id.weatherFragment -> getString(R.string.weather_name)
                R.id.detailsFragment -> getString(R.string.details_name)
                R.id.favoriteFragment -> getString(R.string.favorite_name)
                R.id.helpFragment -> getString(R.string.help_name)
                R.id.prefFragment -> getString(R.string.pref_name)
                else -> getString(R.string.app_name)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val destinationId = navController.currentDestination?.id
        when (id){
            R.id.favorites ->{
                destinationId?. let{
                    when(it){
                        R.id.statesFragment -> navController.navigate(R.id.action_statesFragment_to_favoriteFragment)
                        R.id.searchFragment->navController.navigate(R.id.action_searchFragment_to_favoriteFragment)
                        R.id.detailsFragment->navController.navigate(R.id.action_detailsFragment_to_favoriteFragment)
                    }
                }
                return true
            }
           R.id.navigation_settings ->{
               destinationId?. let{
                   when(it){
                       R.id.statesFragment -> navController.navigate(R.id.action_statesFragment_to_prefFragment)
                       R.id.searchFragment ->navController.navigate(R.id.action_searchFragment_to_prefFragment)
                       R.id.weatherFragment ->navController.navigate(R.id.action_weatherFragment_to_prefFragment)
                       R.id.detailsFragment ->navController.navigate(R.id.action_detailsFragment_to_prefFragment)
                       R.id.favoriteFragment -> navController.navigate(R.id. action_favoriteFragment_to_prefFragment)
                       R.id.helpFragment ->navController.navigate(R.id. action_helpFragment_to_prefFragment)
                   }
               }
               return true
           }
            R.id.navigation_help->{
                destinationId?. let{
                    when(it){
                        R.id.statesFragment ->navController.navigate(R.id.action_statesFragment_to_helpFragment)
                        R.id.searchFragment ->navController.navigate(R.id.action_searchFragment_to_helpFragment)
                        R.id.weatherFragment ->navController.navigate(R.id.action_weatherFragment_to_helpFragment)
                        R.id.detailsFragment ->navController.navigate(R.id.action_detailsFragment_to_helpFragment)
                        R.id.favoriteFragment ->navController.navigate(R.id.action_favoriteFragment_to_helpFragment)
                        R.id.prefFragment ->navController.navigate(R.id.action_prefFragment_to_helpFragment)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity onPause ")
        navigatorHolder.removeNavigator()
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
                //presenter.showFavorites()
                navController.navigate(R.id.action_statesFragment_to_favoriteFragment)
            }
            R.id.nav_setting -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_setting")
                //presenter. showSettingsActivity()
                navController.navigate(R.id.action_statesFragment_to_prefFragment)
            }
            R.id.nav_help -> {
                Log.d(TAG, "MainActivity onNavigationItemSelected nav_help")
                //presenter.showHelp()
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
        if( navController.currentDestination?.id  == R.id.statesFragment){
            Log.d(TAG, "MainActivity onBackPressed  это StatesFragment")
            //если флаг = true - а это при двойном щелчке - закрываем программу
            if (doubleBackToExitPressedOnce) {
                Log.d(TAG, "MainActivity onBackPressed  doubleBackToExitPressedOnce")
                //presenter.backClicked()
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