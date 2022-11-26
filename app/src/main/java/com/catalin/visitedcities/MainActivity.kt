package com.catalin.visitedcities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.catalin.visitedcities.ui.theme.VisitedCitiesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitedCitiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val cities = remember { mutableListOf<City>() }
                    val sort = remember { mutableStateOf(false) }
                    val sortedCities = if (sort.value) cities.sortedBy { it.name[0] } else cities

                    CitiesList(
                        sortedCities,
                        onAddCity = { name, country ->
                            cities.add(City(name, country))
                        },
                        onSort = {
                            sort.value = !sort.value
                        }
                    )
                }
            }
        }
    }
}

data class City(
    val name: String,
    val country: String
)

@Composable
fun CitiesList(
    list: List<City>,
    onAddCity: (name: String, country: String) -> Unit,
    onSort: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = { AddCityFab { showDialog.value = true } },
        topBar = { AddTopBar(onSort) }
    ) {
        if (list.isEmpty())
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No cities available")
            }
        else
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)) {
                items(list) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xffeeeeee))
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = it.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = it.country)
                    }
                }
            }
    }

    if (showDialog.value) {
        AddCityDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { name, country ->
                showDialog.value = false
                onAddCity.invoke(name, country)
            }
        )
    }
}

@Composable
fun AddTopBar(onSort: () -> Unit) {
    val icon = painterResource(id = R.drawable.ic_sort)
    TopAppBar(
        title = { Text(text = "Visited cities") },
        actions = {
            IconButton(onClick = onSort) {
                Icon(painter = icon, contentDescription = null)
            }
        }
    )
}

@Composable
fun AddCityFab(onFabClick: () -> Unit) {
    FloatingActionButton(onClick = onFabClick) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun AddCityDialog(onDismiss: () -> Unit, onConfirm: (name: String, country: String) -> Unit) {
    val cityName = remember { mutableStateOf(TextFieldValue("")) }
    val country = remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add a city",
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = cityName.value,
                    onValueChange = { cityName.value = it },
                    label = { Text(text = "City name") },
                    placeholder = { Text(text = "Rome") },
                    modifier = Modifier.padding(5.dp)
                )
                TextField(
                    value = country.value,
                    onValueChange = { country.value = it },
                    label = { Text(text = "Country") },
                    placeholder = { Text(text = "Italy") },
                    modifier = Modifier.padding(5.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val n = cityName.value.text
                val c = country.value.text
                if (n.isNotEmpty() && c.isNotEmpty())
                    onConfirm.invoke(n, c)
            }) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}







