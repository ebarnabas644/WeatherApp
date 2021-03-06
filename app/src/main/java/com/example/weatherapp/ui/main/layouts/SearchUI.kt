package com.example.weatherapp.ui.main.layouts

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.R
import com.example.weatherapp.network.dataclass.Prediction
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted

@Composable
fun SearchUI(
    dataSource: List<Prediction>,
    searchText: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onClick: (Prediction) -> Unit,
    locationManager: LocationManager,
    geocoder: Geocoder,
    onGetLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    SearchScreen(
        dataSource = dataSource,
        searchText = searchText,
        onValueChange = onValueChange,
        onClearClick = onClearClick,
        onClick = onClick,
        locationManager = locationManager,
        geocoder = geocoder,
        onGetLocationClick = onGetLocationClick,
        modifier = modifier
    )
}

@Composable
fun SearchScreen(
    dataSource: List<Prediction>,
    searchText: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onClick: (Prediction) -> Unit,
    locationManager: LocationManager,
    geocoder: Geocoder,
    onGetLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
    ) {
        SearchBar(
            searchText = searchText,
            onValueChange = onValueChange,
            onClearClick = onClearClick
        )
        if(dataSource.isEmpty()){
            EmptyListMessage(
                locationManager = locationManager,
                geocoder = geocoder,
                onGetLocationClick = onGetLocationClick,
                modifier = Modifier
            )
        }
        else {
            AutocompleteList(
                dataSource = dataSource,
                onClick = onClick
            )
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
){
    TextField(
        value = searchText,
        shape = MaterialTheme.shapes.small,
        leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "")
        },
        trailingIcon = {
                        IconButton(onClick = { onClearClick() }) {
                            Icon(Icons.Filled.Close, contentDescription = "")
                        }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background.copy(.6f),
            textColor = MaterialTheme.colors.onSurface
        ),
        onValueChange = { onValueChange(it) },
        modifier =
        Modifier.fillMaxWidth())
}

@Composable
fun AutocompleteList(
    dataSource: List<Prediction>,
    onClick: (Prediction) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 10.dp)
    )
    {
        items(items = dataSource){
            item -> AutocompleteItem(
                label = item,
                onClick = onClick
        )
        }
    }
}

@Composable
fun AutocompleteItem(
    label: Prediction,
    onClick: (Prediction) -> Unit,
    modifier: Modifier = Modifier

){
    Card(elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp, horizontal = 15.dp)
            .clickable { onClick(label) },
        contentColor = MaterialTheme.colors.onSurface,
        backgroundColor = MaterialTheme.colors.background) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(vertical = 15.dp, horizontal = 20.dp)
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = "",
                modifier = modifier.size(30.dp))
            Spacer(modifier = modifier.padding(6.dp))
            Text(
                text = label.description,
                style = MaterialTheme.typography.h5)
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EmptyListMessage(
    locationManager: LocationManager,
    geocoder: Geocoder,
    onGetLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.search_address_text),
            color = MaterialTheme.colors.onSurface
        )

        val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (locationPermissionState.status.isGranted) {
            if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                val addresses: List<Address> =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName: String = addresses[0].locality
                Log.i("searchloc", cityName)
                Column {
                    Button(onClick = { onGetLocationClick(cityName) }) {
                        Text(
                            "Add current location",
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
            else{
                Column {
                    Text("Location not avaiable on this device")
                }
            }
        } else {
            Column {
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Request permission",
                        color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}



@Preview
@Composable
fun AutocompleteItemPreview(){
    WeatherAppTheme {
        /*AutocompleteItem(label = "Budapest", onClick = {})*/
    }
}

@Preview
@Composable
fun SearchBarPreview(){
    WeatherAppTheme {
        SearchBar(searchText = "Buda", onValueChange = {}, onClearClick = {})
    }
}

@Preview
@Composable
fun AutocompleteListPreview(){
    MaterialTheme{
        AutocompleteList(dataSource = searchTestDatasource, onClick = {})
    }
}