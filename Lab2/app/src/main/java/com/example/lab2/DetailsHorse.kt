package com.example.lab2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lab2.domain.HORSE_ID
import com.example.lab2.domain.Horse
import com.example.lab2.ui.theme.Lab2Theme
import com.example.lab2.repository.repo

class AddHorse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val horseId: Int = intent.getIntExtra(HORSE_ID, -1)

            Lab2Theme() {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AddApp(horseId)
                }
            }
        }
    }
}

@Composable
fun AddApp(horseId: Int) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Horse details")
                    }
                }
            )
        }
    ) {
        HorseDetailsView(horseId)
    }
}

@Composable
fun HorseDetailsView(horseId: Int) {
    Column(
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 35.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Form(horseId)
    }
}

@Composable
fun Form(horseId: Int) {
    val currentContext = LocalContext.current

    var horse = repo.searchById(horseId)
    if (horse == null) {
        horse = Horse()
    }

    var horseName by remember { mutableStateOf(horse.name) }
    OutlinedTextField (
        value = horseName,
        onValueChange = { horseName = it },
        label = { Text("Horse name") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.secondary)
    )

    var horseBreed by remember { mutableStateOf(horse.breed) }
    OutlinedTextField (
        value = horseBreed,
        onValueChange = { horseBreed = it },
        label = { Text("Horse breed") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.secondary)
    )

    var horseGender by remember { mutableStateOf(horse.gender) }
    OutlinedTextField (
        value = horseGender,
        onValueChange = { horseGender = it },
        label = { Text("Horse gender") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.secondary)
    )

    var horseColour by remember { mutableStateOf(horse.colour) }
    OutlinedTextField (
        value = horseColour,
        onValueChange = { horseColour = it },
        label = { Text("Horse colour") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.secondary)
    )

    var horseAge by remember { mutableStateOf(horse.age) }
    OutlinedTextField (
        value = horseAge.toString(),
        onValueChange = { horseAge = it.toInt() },
        label = { Text("Horse age") },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.secondary)
    )

    Row (horizontalArrangement = Arrangement.spacedBy(15.dp)){
        Button (onClick = {
            horse.name = horseName
            horse.breed = horseBreed
            horse.gender = horseGender
            horse.colour = horseColour
            horse.age = horseAge

            repo.insert(horse)

            if (horseId == -1)
                finishActivity(currentContext, "Horse successfully added!")
            else
                finishActivity(currentContext, "Horse successfully updated!")

        }) {
            if (horseId == -1)
                Text("Add horse")
            else
                Text("Update horse")
        }

        if (horseId != -1) {
            DeleteHorse(horseId)
        }
    }
}

@Composable
fun DeleteHorse(activityId: Int) {
    var dialogControl by remember { mutableStateOf(false) }

    Button(onClick = {
        dialogControl = true
    }) {
        Text("Delete horse")
    }

    if(dialogControl)
        DisplayDeleteDialog(activityId) {
            dialogControl = false
        }
}

@Composable
fun DisplayDeleteDialog(horseId: Int, onDismiss: () -> Unit) {
    val currentContext = LocalContext.current
    val horseName = repo.searchById(horseId)?.name

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text ="Are you sure you want to delete the horse $horseName?",
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(25.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                    Button(onClick = {
                        repo.delete(horseId)

                        finishActivity(currentContext, "Horse deleted!")
                    }) {
                        Text("Ok")
                    }
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

fun finishActivity(currentContext: Context, toastMessage: String) {
    val activity = currentContext as android.app.Activity
    activity.finish()

//    Toast.makeText(currentContext, toastMessage, Toast.LENGTH_LONG).show()
}