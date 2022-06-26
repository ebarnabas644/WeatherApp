package com.example.weatherapp.ui.main.layouts

import com.example.weatherapp.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.main.WeatherViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.WeatherScreen
import com.example.weatherapp.network.dataclass.PanelData
import com.example.weatherapp.network.dataclass.Weather
import com.example.weatherapp.network.dataclass.WeatherForecastDay
import com.example.weatherapp.network.dataclass.WeatherHour
import com.example.weatherapp.ui.icons.ConvertIconToEnum
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun WeatherUI(
    modifier: Modifier = Modifier,
    weather: Weather,
    isLoading: Boolean = false
){
    WeatherScreen(
        weather = weather,
        modifier = modifier
            .fillMaxHeight(),
        isLoading = isLoading)
}

@Composable
fun WeatherScreen(
    weather: Weather,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
){
    val panelData = listOf<PanelData>(
        PanelData(stringResource(R.string.sunrise_title), weather.current.sunrise, iconId = R.drawable.ic_wi_sunrise),
        PanelData(stringResource(R.string.sunset_title), weather.current.sunset, iconId = R.drawable.ic_wi_sunset),
        PanelData(stringResource(R.string.humidity_title), (LocalContext.current.resources.getString(R.string.humidity_detail, (weather.current.humidity ?: "N/A").toString())), iconId = R.drawable.ic_wi_humidity),
        PanelData(stringResource(R.string.windSpeed_title), (LocalContext.current.resources.getString(R.string.wind_speed_metric, (weather.current.windSpeed ?: "N/A").toString())).toString(), iconId = R.drawable.ic_wi_windy),
        PanelData(stringResource(R.string.windDirection_title), (LocalContext.current.resources.getString(R.string.wind_direction, (weather.current.winDirection ?: "N/A").toString())), iconId = R.drawable.ic_wi_wind_deg, rotation = (weather.current.winDirection ?: 0.0)),
        PanelData(stringResource(R.string.uvIndex_title), weather.current.uvIndex.toString(), iconId = R.drawable.ic_wi_day_sunny)
    )
    val alpha: Float by animateFloatAsState(if (!isLoading) 1f else 0.0f)
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.primaryVariant
                )
            )
        )) {
        TemperatureHeader(
            temperature = weather.current.temp,
            address = weather.address,
            iconId = ConvertIconToEnum(weather.current.imageIcon).iconId,
            modifier = Modifier.alpha(alpha),
            isLoading = isLoading
        )
        InfoSection(title = R.string.daily_forecast) {
            DayForecastRow(
                dataSource = weather.days[0].hours,
                modifier = Modifier.alpha(alpha),
                isLoading = isLoading)
        }
        InfoSection(title = R.string.detail_panel_title) {
            DetailPanel(
                data = panelData,
                modifier = modifier.alpha(alpha),
                isLoading = isLoading)
        }
    }
}

@Composable
fun TemperatureHeader(
    address: String,
    temperature: Double,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
){
    val textStyleStart = MaterialTheme.typography.h4
    var readyToDraw by remember { mutableStateOf(false) }
    var textStyle by remember { mutableStateOf(textStyleStart) }
    /*if (isLoading){
        TemperatureHeaderLoading(modifier)
    }
    else{*/
    val alpha: Float by animateFloatAsState(if (!isLoading) 1f else 0.0f)
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = LocalContext.current.resources.getString(
                    R.string.temperature,
                    temperature.toInt()
                ),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground),
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = address,
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
fun DayForecastCard(
    dateEpoch: Int,
    @DrawableRes iconId: Int,
    temperature: Double,
    modifier: Modifier = Modifier
)
{
    //val formattedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
    /*val weekDay = formattedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val monthAndDay = formattedDate.format(DateTimeFormatter.ofPattern("MM.dd"))*/
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.getDefault());
    val dateTime = Instant.ofEpochSecond(dateEpoch.toLong()).atZone(ZoneId.systemDefault())
    val hour = dateTime.format(DateTimeFormatter.ofPattern("h a").withLocale(Locale.ENGLISH))
    val hour24 = dateTime.format(DateTimeFormatter.ofPattern("H").withLocale(Locale.ENGLISH)).toInt()
    val current = Calendar.getInstance()
    val currentHour = current.get(Calendar.HOUR_OF_DAY)
    //if (dateTime.hour > currentHour) {
        val contentColor = MaterialTheme.colors.onSurface
        val backgroundColor = MaterialTheme.colors.background
        Card(
            elevation = 0.dp,
            modifier = Modifier.padding(5.dp),
            contentColor = contentColor,
            backgroundColor = backgroundColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(6.dp)
            ) {
                Text(
                    text = hour,
                    style = MaterialTheme.typography.h5
                )
                /*Text(
                text = monthAndDay,
                style = MaterialTheme.typography.h5)*/
                Image(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    colorFilter = ColorFilter.tint(color = contentColor)
                )
                Text(
                    text = LocalContext.current.resources.getString(
                        R.string.temperature,
                        temperature.toInt()
                    ),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .paddingFromBaseline(10.dp)
                )
            }
        }
    //}
}

@Composable
fun DayForecastRow(
    dataSource: List<WeatherHour>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
){
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp)
    ){
        items(items = dataSource.filter { i -> (i.datetimeEpoch / 3600) % 3 == 2 }){
            item ->
                DayForecastCard(
                    dateEpoch = item.datetimeEpoch,
                    iconId = ConvertIconToEnum(item.imageIcon.toString()).iconId,
                    temperature = item.temp,
                    modifier = modifier)
        }
    }
}

@Composable
fun DetailPanel(
    data: List<PanelData>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
){
    Card(
        elevation = 0.dp,
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(vertical =  5.dp, horizontal = 15.dp)
    ){
        Column() {
            data.forEach{ item ->
                val isLastItem = data[data.size - 1] == item
                    DetailPanelItem(
                        title = item.title,
                        data = item.data,
                        iconId = item.iconId,
                        rotation = item.rotation,
                        lastItem = isLastItem,
                        modifier = modifier
                    )
            }
        }
    }
}

@Composable
fun DetailPanelItem(
    title: String,
    data: String,
    iconId: Int,
    lastItem: Boolean,
    modifier: Modifier = Modifier,
    rotation: Double = 0.0
){
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .weight(1f)
                    .rotate(rotation.toFloat()),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface)
            )
            Text(
                text = title,
                modifier = Modifier.weight(5f)
            )
            Text(
                text = data,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        if (!lastItem) {
            Divider(
                Modifier.padding(horizontal = 15.dp)
            )
        }
    }
}

@Composable
fun InfoSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Column(modifier.padding(top = 20.dp)) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        content()
    }
}

//PREVIEWS ----------------------------------------------------------------------------

@Preview /*(showBackground = true, backgroundColor = 0xFFFFFFFF)*/
@Composable
fun TemperatureHeaderPreview(){
    WeatherAppTheme {
        TemperatureHeader(
            address = "Bremen",
            temperature = 23.1,
            iconId = R.drawable.ic_wi_day_sunny,
            isLoading = false)
    }
}

@Preview
@Composable
fun DayCardPreview(){
    WeatherAppTheme {
        DayForecastCard(
            dateEpoch = 1654034400,
            iconId = R.drawable.ic_wi_day_sunny,
            temperature = 12.9)
    }
}


@Preview
@Composable
fun DayForecastRowPreview(){
    WeatherAppTheme {
        DayForecastRow(
            dataSource = dayForecastTestDatasource,
            isLoading = false
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun InfoSectionPreview(){
    WeatherAppTheme {
        InfoSection(title = R.string.daily_forecast) {
            DayForecastRow(
                dataSource = dayForecastTestDatasource
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DetailPanelItemPreview(){
    WeatherAppTheme() {
        DetailPanelItem(
            title = detailTestDatasource[0].title,
            data = detailTestDatasource[0].data,
            iconId = detailTestDatasource[0].iconId,
            lastItem = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DetailPanelPreview(){
    WeatherAppTheme() {
        DetailPanel(
            data = detailTestDatasource,
            isLoading = false)
    }
}
