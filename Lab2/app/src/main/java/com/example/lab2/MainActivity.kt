package com.example.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.lab2.domain.HORSE_ID
import com.example.lab2.domain.Horse
import com.example.lab2.ui.theme.Lab2Theme
import com.example.lab2.repository.repo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App(LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun App(currentContext: Context) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                title = {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your Horses",
                            color = Color.Black
                        )
                    }
                }
            )
        }
    ) {
        Horses(currentContext)
        AddHorse(currentContext)
    }
}

fun <T> SnapshotStateList<T>.refreshList(newList: List<T>){
    clear()
    addAll(newList)
}

@Composable
fun Horses(currentContext: Context, lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    val horsesList = remember { mutableStateListOf<Horse>() }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                horsesList.refreshList(repo.getHorses())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(horsesList, key = { horse -> horse.id }) {
                horse -> HorseCard(currentContext, horse)
        }
    }
}

@Composable
fun HorseCard(currentContext: Context, horse: Horse) {
    Card(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {

                val intent = Intent(currentContext, AddHorse::class.java)
                intent.putExtra(HORSE_ID, horse.id)
                currentContext.startActivity(intent)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = 10.dp
    ) {
        Column() {
            Text(
                text = horse.name,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = horse.breed,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black
            )
        }
    }
}

@Composable
fun AddHorse(currentContext: Context) {
    Box(Modifier.fillMaxSize()) {
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(25.dp)
                .align(Alignment.BottomEnd),
            backgroundColor = MaterialTheme.colors.background,
            text = { Text(text ="Add horse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)},
            onClick = {
                currentContext.startActivity(Intent(currentContext, AddHorse::class.java))
            }
        )
    }
}