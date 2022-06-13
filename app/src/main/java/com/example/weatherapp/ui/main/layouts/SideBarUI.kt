package com.example.weatherapp.ui.main.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.main.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme


@Composable
fun SideBarUI(
    modifier: Modifier = Modifier,
    dataSource: List<String>,
    selectedAddress: String,
    onAddressSelection: (String) -> Unit,
    onRemoveClick: (String) -> Unit
){
    SideBarScreen(
        dataSource = dataSource,
        selectedAddress = selectedAddress,
        onAddressSelection = onAddressSelection,
        onRemoveClick = onRemoveClick,
        modifier = modifier)
}

@Composable
fun SideBarScreen(
    dataSource: List<String>,
    selectedAddress: String,
    onAddressSelection: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
        Text(
            text = stringResource(R.string.sidebar_title),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        AddressSelectionPanel(
            dataSource = dataSource,
            selectedAddress = selectedAddress,
            onAddressSelection = onAddressSelection,
            onRemoveClick = onRemoveClick,
            modifier = modifier)
    }
}

@Composable
fun AddressSelectionPanel(
    dataSource: List<String>,
    selectedAddress: String,
    onAddressSelection: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = modifier
    ){
        items(items = dataSource){
                item -> AddressSelectionRow(
            address = item,
            isSelected = item == selectedAddress,
            modifier = modifier.clickable { onAddressSelection(item) },
            onRemoveClick = onRemoveClick)
        }
    }
}

@Composable

fun AddressSelectionRow(
    address: String,
    isSelected: Boolean,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
{
    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentColor = if(isSelected) MaterialTheme.colors.background else MaterialTheme.colors.onSurface,
        backgroundColor = if(isSelected) MaterialTheme.colors.onSurface  else MaterialTheme.colors.background
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 15.dp, vertical = 6.dp)
        ) {
            Text(text = address)
            IconButton(
                onClick = { onRemoveClick(address) },) {
                Icon(Icons.Filled.Close, contentDescription = "")
            }
        }
    }
}

@Preview
@Composable
fun AddressSelectionRowPreview(){
    WeatherAppTheme {
        AddressSelectionRow(address = "Bremen", isSelected = true, onRemoveClick = {})
    }
}

@Preview
@Composable
fun AddressSelectionPanelPreview(){
    WeatherAppTheme() {
        AddressSelectionPanel(
            dataSource = addressSelectionTestDatasource,
            selectedAddress = addressSelectionTestDatasource[1],
            onAddressSelection = {},
            onRemoveClick = {}
        )
    }
}

@Preview()
@Composable
fun SideBarScreenPreview(){
    WeatherAppTheme() {
        SideBarScreen(
            dataSource = addressSelectionTestDatasource,
            selectedAddress = addressSelectionTestDatasource[2],
            onAddressSelection = {},
            onRemoveClick = {}
        )
    }
}