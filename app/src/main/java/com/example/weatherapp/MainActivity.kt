package com.example.weatherapp

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.network.dataclass.WeatherRepository
import com.example.weatherapp.network.dataclass.WeatherRoomDatabase
import com.example.weatherapp.ui.main.WeatherApiStatus
import com.example.weatherapp.ui.main.WeatherViewModel
import com.example.weatherapp.ui.main.layouts.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.*


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

// UI controller
@Composable
fun WeatherApp(
){
    // Setting up dependencies
    val allScreens = WeatherScreen.values().toList()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = WeatherScreen.fromRoute(backstackEntry.value?.destination?.route)

    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val locationManager = LocalContext.current.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val weatherDb = WeatherRoomDatabase.getDatabase(LocalContext.current)
    val weatherDao = weatherDb.itemDao()
    val weatherRepo = WeatherRepository(weatherDao)
    val factory = WeatherViewModel.Factory(weatherRepo)
    val owner = LocalViewModelStoreOwner.current
    owner?.let {
        //https://mahendranv.github.io/posts/viewmodel-store/
        val weatherViewModel: WeatherViewModel =
            ViewModelProvider(it, factory)[WeatherViewModel::class.java]

        val addressList by weatherViewModel.addressList.collectAsState(listOf())
        val isRefreshing by weatherViewModel.isRefreshing.collectAsState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                if (currentScreen != WeatherScreen.SearchUI) {
                    TopBarUI(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        title = if (currentScreen == WeatherScreen.ForecastUI) weatherViewModel.selectedAddress else ""
                    )
                }
            },
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
                    dataSource = addressList,
                    selectedAddress = weatherViewModel.selectedAddress,
                    onAddressSelection = { exp -> weatherViewModel.setSelected(exp) },
                    onRemoveClick = { exp -> weatherViewModel.removeAddress(exp) }
                )
            },
            drawerGesturesEnabled = currentScreen != WeatherScreen.SearchUI,
            drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.primaryVariant
                        )
                    )
                )
        ) { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
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
                    locationManager = locationManager,
                    geocoder = geocoder
                )
            }
        }
    }
}

// Defining navigation host and navigation graph.
@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel(),
    locationManager: LocationManager,
    geocoder: Geocoder
){
    //https://stackoverflow.com/questions/69002018/why-a-new-viewmodel-is-created-in-each-compose-navigation-route
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val autoCompleteText by weatherViewModel.autoCompleteText.collectAsState()
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
                    searchText = autoCompleteText,
                    onValueChange = { exp -> weatherViewModel.updateAutocompleteList(exp) },
                    onClearClick = { weatherViewModel.resetAutofill() },
                    onClick = { exp ->
                        weatherViewModel.searchAddress(exp)
                        navController.navigate(WeatherScreen.WeatherUI.name)
                    },
                    locationManager = locationManager,
                    onGetLocationClick = { exp -> weatherViewModel.setSelected(exp)
                        navController.navigate(WeatherScreen.WeatherUI.name)},
                    geocoder = geocoder
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
        allScreens.forEach{
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