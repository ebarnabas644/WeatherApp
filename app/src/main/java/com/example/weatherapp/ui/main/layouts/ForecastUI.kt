package com.example.weatherapp.ui.main.layouts

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.network.dataclass.Weather
import com.example.weatherapp.network.dataclass.WeatherForecastDay
import com.example.weatherapp.ui.icons.ConvertIconToEnum
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun ForecastUI(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    weather: Weather
) {
    ForecastScreen(
        weather = weather,
        modifier = modifier,
        isLoading = isLoading
    )
}

@Composable
fun ForecastScreen(
    weather: Weather,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
)
{
    Column(
        modifier = Modifier
            .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.primaryVariant
                )
            )
            )
    ) {

        WeekForecastColumn(
            dataSource = weather.days,
            description = weather.forecastDescription,
            modifier = Modifier,
            isLoading = isLoading)
    }
}

@Composable
fun DescriptionPanel(
    description: String,
    modifier: Modifier = Modifier
){
    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background
    ){
        Column(
            modifier = modifier.padding(20.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun DescriptionPanelLoading(
    modifier: Modifier = Modifier
)
{
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background
    ){
        Column(
            modifier = modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = alpha))
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
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
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
                        modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface))
                    Text(
                        text = humidity.toInt().toString(),
                        style = MaterialTheme.typography.h4
                    )
                }
            }
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface))
            Text(
                text = LocalContext.current.resources.getString(R.string.min_max_temperature_display, maxTemp.toInt(), minTemp.toInt()),
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@Composable
fun WeekForecastCardLoading(
    modifier: Modifier = Modifier
){
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(MaterialTheme.colors.onSurface.copy(alpha = alpha))
            )
        }
    }
}

@Composable
fun WeekForecastColumn(
    dataSource: List<WeatherForecastDay>,
    description: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 10.dp)
    ){
        item{
            if(isLoading){
                DescriptionPanelLoading()
            }
            else {
                DescriptionPanel(description = description)
            }
        }
        items(items = dataSource){
                item ->
            if(isLoading){
                WeekForecastCardLoading()
            }
            else {
                WeekForecastCard(
                    dateEpoch = item.datetimeEpoch,
                    iconId = ConvertIconToEnum(item.imageIcon).iconId,
                    minTemp = item.tempMin,
                    maxTemp = item.tempMax,
                    humidity = item.avgHumidity
                )
            }
        }
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
fun WeekForecastColumnPreview(){
    WeatherAppTheme(darkTheme = true) {
        WeekForecastColumn(
            dataSource = weekForecastTestDatasource,
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis ipsum odio. Aliquam ultricies, leo et blandit pulvinar, ex dui ornare nunc, ut auctor enim nibh vel velit. In finibus mi quis ipsum suscipit aliquet. Vestibulum hendrerit dui nec diam pulvinar, ut sodales mi mollis. Sed fermentum accumsan nisi eget cursus. Quisque at lacinia metus, luctus gravida risus. In suscipit condimentum nisl. ",
            isLoading = false
        )
    }
}

@Preview
@Composable
fun DescriptionPanelPreview(){
    WeatherAppTheme {
        DescriptionPanel("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis ipsum odio. Aliquam ultricies, leo et blandit pulvinar, ex dui ornare nunc, ut auctor enim nibh vel velit. In finibus mi quis ipsum suscipit aliquet. Vestibulum hendrerit dui nec diam pulvinar, ut sodales mi mollis. Sed fermentum accumsan nisi eget cursus. Quisque at lacinia metus, luctus gravida risus. In suscipit condimentum nisl. ")
    }
}