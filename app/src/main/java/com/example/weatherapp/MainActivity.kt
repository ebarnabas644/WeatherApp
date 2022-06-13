package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.main.WeatherApiStatus
import com.example.weatherapp.ui.main.WeatherViewModel
import com.example.weatherapp.ui.main.layouts.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                this.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp(){
    val allScreens = WeatherScreen.values().toList()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val weatherViewModel: WeatherViewModel = viewModel()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = WeatherScreen.fromRoute(backstackEntry.value?.destination?.route)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if(currentScreen != WeatherScreen.SearchUI){
                TopBarUI(
                    scope = scope,
                    scaffoldState = scaffoldState,
                    onAddClick = { navController.navigate(WeatherScreen.SearchUI.name) },
                    title = if(currentScreen == WeatherScreen.ForecastUI) weatherViewModel.selectedAddress else ""
            ) }},
        bottomBar = {
            BottomNavigationBar(
                allScreens = allScreens,
                onSelected = { screen -> navController.navigate(screen.name) },
                currentScreen = currentScreen,
            )
        },
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            SideBarUI(
                dataSource = weatherViewModel.addressList,
                selectedAddress = weatherViewModel.selectedAddress,
                onAddressSelection = { exp -> weatherViewModel.setSelected(exp) },
                onRemoveClick = { exp -> weatherViewModel.removeAddress(exp) }
            )
        },
        drawerGesturesEnabled = currentScreen != WeatherScreen.SearchUI,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) { innerPadding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(weatherViewModel.isRefreshing),
            onRefresh = { weatherViewModel.refresh() },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    backgroundColor = MaterialTheme.colors.background.copy(1f),
                    shape = MaterialTheme.shapes.small,
                    contentColor = MaterialTheme.colors.onSurface
                )
            }) {
            WeatherNavHost(
                navController = navController,
                weatherViewModel = weatherViewModel,
                modifier = Modifier.padding(innerPadding),
                scaffoldState = scaffoldState,
                scope = scope)
        }
    }
}

@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel(),
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
){
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    NavHost(
        navController = navController,
        startDestination = WeatherScreen.WeatherUI.name,
        modifier = modifier){
        composable(WeatherScreen.WeatherUI.name){
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                WeatherUI(
                    weather = weatherViewModel.weather,
                    isLoading = weatherViewModel.weatherApiStatus == WeatherApiStatus.LOADING
                )
            }
        }
        composable(WeatherScreen.SearchUI.name){
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                SearchUI(
                    dataSource = weatherViewModel.autocomplete.predictions,
                    searchText = weatherViewModel.autocompleteText,
                    onValueChange = { exp -> weatherViewModel.updateAutocompleteList(exp) },
                    onClearClick = { weatherViewModel.resetAutofill() },
                    onClick = { exp ->
                        weatherViewModel.searchAddress(exp)
                        navController.navigate(WeatherScreen.WeatherUI.name)
                    }
                )
            }
        }
        composable(WeatherScreen.ForecastUI.name){
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                ForecastUI(
                    weather = weatherViewModel.weather,
                    isLoading = weatherViewModel.weatherApiStatus == WeatherApiStatus.LOADING
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    allScreens: List<WeatherScreen>,
    onSelected: (WeatherScreen) -> Unit,
    currentScreen: WeatherScreen
){
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.shadow(10.dp)
    ) {
        allScreens.filter { tab -> tab != WeatherScreen.SearchUI }.forEach{
            BottomNavigationItem(
                selected = it.name == currentScreen.name,
                selectedContentColor = MaterialTheme.colors.onSurface,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.4f),
                onClick = { onSelected(it) },
                icon = {
                    Icon(
                        WeatherScreen.convertScreenToIcon(it),
                        contentDescription = ""
                    )
                       },
                label = {
                    Text(
                        text = stringResource(WeatherScreen.convertScreenToResourceId(it))
                    )
                })
        }
    }
}