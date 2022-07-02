package com.example.weatherapp.ui.main.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBarUI(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    title: String = ""
){
    TopBarScreen(
        scope = scope,
        scaffoldState = scaffoldState,
        modifier = modifier,
        title = title
    )
}

@Composable
fun TopBarScreen(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
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
        Spacer(modifier = Modifier.size(35.dp))
        /*AddFavouriteButton(
            onClick = onSearchClick)*/
    }
}

@Composable
fun SideBarButton(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
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
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(.7f)
                .offset(x = (-20).dp)
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