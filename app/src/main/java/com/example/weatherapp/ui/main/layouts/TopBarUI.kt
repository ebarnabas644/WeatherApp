package com.example.weatherapp.ui.main.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.network.dataclass.Weather
import com.example.weatherapp.ui.main.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarUI(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel(),
    onAddClick: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    title: String = ""
){
    TopBarScreen(
        weather = weatherViewModel.weather,
        scope = scope,
        scaffoldState = scaffoldState,
        onSearchClick = onAddClick,
        modifier = modifier,
        title = title
    )
}

@Composable
fun TopBarScreen(
    weather: Weather,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = ""
)
{
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
    ) {
        SideBarButton(
            scope = scope,
            scaffoldState = scaffoldState)
        TitleBar(title = title)
        AddFavouriteButton(
            onClick = onSearchClick)
    }
}

@Composable
fun SideBarButton(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
){
    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
        Icon(Icons.Filled.Menu,
            "",
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier
)
{
    if(title != "") {
        val textStyleStart = MaterialTheme.typography.h4
        var readyToDraw by remember { mutableStateOf(false) }
        var textStyle by remember { mutableStateOf(textStyleStart) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .widthIn(0.dp, 200.dp)
        ) {
            Icon(
                Icons.Filled.LocationOn,
                "",
                modifier = Modifier
                    .size(40.dp)
                    .padding(horizontal = 5.dp)
            )
            //https://stackoverflow.com/questions/63971569/androidautosizetexttype-in-jetpack-compose
            Text(
                text = title,
                style = textStyle,
                maxLines = 1,
                softWrap = false,
                modifier = modifier.drawWithContent {
                    if (readyToDraw) drawContent()
                },
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.didOverflowWidth) {
                        textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                    } else {
                        readyToDraw = true
                    }
                }
            )
        }
    }
}

@Composable
fun AddFavouriteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    IconButton(onClick = onClick) {
        Icon(
            Icons.Filled.Add,
            "",
            modifier = Modifier
                .size(35.dp)
                .requiredWidth(35.dp)
        )
    }
}
