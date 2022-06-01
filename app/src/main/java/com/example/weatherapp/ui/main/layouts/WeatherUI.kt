package com.example.weatherapp.ui.main.layouts

import com.example.weatherapp.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.main.WeatherViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.network.dataclass.Weather
import com.example.weatherapp.network.dataclass.WeatherForecastDay
import com.example.weatherapp.network.dataclass.WeatherHour
import com.example.weatherapp.ui.theme.WeatherAppTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun WeatherUI(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel()
){
    val weatherState by weatherViewModel.weather.observeAsState()
    weatherState?.let {
        WeatherScreen(
            weather = it,
            modifier = Modifier
        )
    }
}

@Composable
fun WeatherScreen(
    weather: Weather,
    modifier: Modifier = Modifier
){
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
            iconId = R.drawable.ic_wi_day_sunny,
            modifier = modifier
        )
        InfoSection(title = R.string.daily_forecast) {
            DayForecastRow(
                dataSource = weather.days[0].hours,
                modifier = modifier)
        }
        InfoSection(title = R.string.weekly_forecast) {
            WeekForecastColumn(
                dataSource = weather.days,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TemperatureHeader(
    address: String,
    temperature: Double,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()) {
        Text(
            text = LocalContext.current.resources.getString(R.string.temperature, temperature.toInt()),
            style = MaterialTheme.typography.h1)
        Image(
            painter = painterResource(iconId),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            colorFilter = ColorFilter.tint(color = Color.Yellow))
        Text(
            text = address,
            style = MaterialTheme.typography.h4)
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
    Card(
        elevation = 10.dp,
        modifier = modifier.padding(5.dp),
        contentColor = MaterialTheme.colors.onSurface){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(6.dp)
                .background(Color.Transparent)
            ) {
            Text(
                text = hour,
                style = MaterialTheme.typography.h5)
            /*Text(
                text = monthAndDay,
                style = MaterialTheme.typography.h5)*/
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.size(80.dp))
            Text(
                text = LocalContext.current.resources.getString(R.string.temperature, temperature.toInt()),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .paddingFromBaseline(10.dp)
            )
        }
    }
}

@Composable
fun WeekForecastCard(
    dateEpoch: Int,
    @DrawableRes iconId: Int,
    minTemp: Double,
    maxTemp: Double,
    humidity: Double,
    modifier: Modifier = Modifier)
{
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.getDefault());
    val dateTime = Instant.ofEpochSecond(dateEpoch.toLong()).atZone(ZoneId.systemDefault())
    val day = dateTime.format(DateTimeFormatter.ofPattern("EE").withLocale(Locale.ENGLISH))

    Card(
        elevation = 10.dp,
        modifier =  modifier.fillMaxWidth().padding(6.dp),
        contentColor = MaterialTheme.colors.onSurface
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(6.dp)
        ) {
            Column(
            ){
                Text(
                    text = day,
                    style = MaterialTheme.typography.h4
                )
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_wi_humidity),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp))
                    Text(
                        text = humidity.toInt().toString(),
                        style = MaterialTheme.typography.h4
                    )
                }
            }
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.size(100.dp))
            Text(
                text = LocalContext.current.resources.getString(R.string.min_max_temperature_display, maxTemp.toInt(), minTemp.toInt()),
                style = MaterialTheme.typography.h3
            )
        }
    }
}

@Composable
fun DayForecastRow(
    dataSource: List<WeatherHour>,
    modifier: Modifier = Modifier
){
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = modifier
    ){
        items(items = dataSource){
            item ->  DayForecastCard(
                dateEpoch = item.datetimeEpoch,
                iconId = R.drawable.ic_wi_day_sunny,
                temperature = item.temp
            )
        }
    }
}

@Composable
fun WeekForecastColumn(
    dataSource: List<WeatherForecastDay>,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .height(300.dp)
    ){
        items(items = dataSource){
            item -> WeekForecastCard(
            dateEpoch = item.datetimeEpoch,
            iconId = R.drawable.ic_wi_day_sunny,
            minTemp = item.tempMin,
            maxTemp = item.tempMax,
            humidity = item.avgHumidity
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

@Preview
@Composable
fun TemperatureHeaderPreview(){
    WeatherAppTheme {
        TemperatureHeader(
            address = "Bremen",
            temperature = 23.1,
            iconId = R.drawable.ic_wi_day_sunny)
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

@Preview(widthDp = 400)
@Composable
fun WeekForecastCardPreview(){
    WeatherAppTheme {
        WeekForecastCard(
            dateEpoch = 1654034400,
            iconId = R.drawable.ic_wi_day_sunny,
            minTemp = 12.3,
            maxTemp = 26.5,
            humidity = 6.1)
    }
}

@Preview
@Composable
fun DayForecastRowPreview(){
    WeatherAppTheme {
        DayForecastRow(
            dataSource = dayForecastTestDatasource
        )
    }
}

@Preview
@Composable
fun WeekForecastColumnPreview(){
    WeatherAppTheme {
        WeekForecastColumn(
            dataSource = weekForecastTestDatasource
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