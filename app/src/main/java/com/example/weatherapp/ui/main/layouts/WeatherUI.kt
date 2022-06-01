package com.example.weatherapp.ui.main.layouts

import com.example.weatherapp.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.network.dataclass.Weather
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun WeatherUI(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel()
){
    val weatherState by weatherViewModel.weather.observeAsState()
    weatherState?.let {
        WeatherScreen(
            weather = it,
            modifier = modifier
        )
    }
}

@Composable
fun WeatherScreen(
    weather: Weather,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        TemperatureHeader(
            temperature = weather.current.temp,
            address = weather.address,
            iconId = R.drawable.ic_wi_day_sunny
        )
    }
}

@Composable
fun TemperatureHeader(
    address: String,
    temperature: Double,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier
){
    Card(
        elevation = 10.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = temperature.toString(),
                style = MaterialTheme.typography.h1)
            Image(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.size(100.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.h4)
        }
    }
}


@Composable
fun InfoSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Column(modifier) {
        Text(
            text = stringResource(title)
        )
        content()
    }
}

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