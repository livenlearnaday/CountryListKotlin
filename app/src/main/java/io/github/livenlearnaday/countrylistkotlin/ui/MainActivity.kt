package io.github.livenlearnaday.countrylistkotlin.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.livenlearnaday.countrylistkotlin.R
import io.github.livenlearnaday.countrylistkotlin.databinding.ActivityMainBinding
import io.github.livenlearnaday.countrylistkotlin.ui.base.BaseActivity


@AndroidEntryPoint
class MainActivity : BaseActivity() {


    private var binding: ActivityMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar.toolbarTopCustom)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController: NavController = navHostFragment.navController


        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding!!.toolbar.toolbarTopCustom.setupWithNavController(navController, appBarConfiguration)



    }

    fun setToolBarTitle(title: String) {
        binding!!.toolbar.appTitleToolbar.text = title
    }

}