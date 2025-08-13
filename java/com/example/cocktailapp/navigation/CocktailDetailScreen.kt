package com.example.cocktailapp.navigation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cocktailapp.data.local.CocktailRepository
import com.example.cocktailapp.data.local.AppDatabase
import com.example.cocktailapp.data.viewmodel.CocktailDetailViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocktailapp.data.viewmodel.TimerViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailScreen(cocktailId: Int) {
    val context = LocalContext.current
    val repository = remember { CocktailRepository(AppDatabase.getInstance(context).cocktailDao()) }
    val detailViewModel = remember { CocktailDetailViewModel(repository) }
    val timerViewModel: TimerViewModel = viewModel()

    val cocktail by detailViewModel.cocktail.collectAsState()
    val secondsLeft by timerViewModel.remainingSeconds.collectAsState()
    val isRunning by timerViewModel.isRunning.collectAsState()
    var inputSeconds by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(cocktailId) {
        detailViewModel.loadCocktail(cocktailId)
    }

    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(cocktail) {
        if (!initialized && cocktail != null) {
            val seconds = cocktail!!.prepTimeMinutes * 60
            timerViewModel.setTime(seconds)
            initialized = true
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
          cocktail?.let { c ->
              TopAppBar(
                  title = { Text(c.name) },
                  scrollBehavior = scrollBehavior,
                  colors = TopAppBarDefaults.topAppBarColors(
                      containerColor = MaterialTheme.colorScheme.primary,
                      titleContentColor = MaterialTheme.colorScheme.onPrimary,
                  )
              )
          }
        },
        floatingActionButton = {
            cocktail?.let { c ->
                FloatingActionButton(onClick = {
                    val body = buildString {
                        append("Lista składników do „${c.name}”:\n")
                        c.ingredients.forEach {
                            ing -> append("• $ing\n")
                        }
                        append("\nPrzepis: \n" + c.instructions)
                    }
                    val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")).apply {
                        putExtra("sms_body", body)
                    }
                    context.startActivity(smsIntent)
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Wyślij SMS")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            cocktail?.let { c ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Zdjęcie
                    val imageId = remember(c.imageRes) {
                        context.resources.getIdentifier(c.imageRes, "drawable", context.packageName)
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = c.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                    }


                    Spacer(Modifier.height(24.dp))

                   /* Text(
                        text = c.name,
                        style = MaterialTheme.typography.headlineMedium
                    )*/
                    Text(
                        text = "Czas przygotowania: ${c.prepTimeMinutes} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(24.dp))

                    // Składniki
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Składniki:", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            c.ingredients.forEach {
                                Text("• $it", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Instrukcje
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Sposób przygotowania:", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(c.instructions, style = MaterialTheme.typography.bodyMedium)
                        }
                    }


                    //Minutnik
                    Spacer(Modifier.height(32.dp))
                    Divider()
                    Spacer(Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Minutnik",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Minutnik",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = inputSeconds,
                            onValueChange = { new -> inputSeconds = new.filter { it.isDigit() } },
                            label = { Text("Sekundy") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                timerViewModel.setTime(inputSeconds.toIntOrNull() ?: 0)
                            },
                            modifier = Modifier.height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text("Ustaw")
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    val m = (secondsLeft % 3600) / 60
                    val s = secondsLeft % 60
                    val timeString = String.format("%02d:%02d", m, s)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            timeString,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (isRunning) timerViewModel.pause() else timerViewModel.start()
                                },
                                modifier = Modifier
                                    .weight(1f),
                                shape = MaterialTheme.shapes.large
                            ) {
                                Text(if (isRunning) "Pauza" else "Start")
                            }

                            Button(
                                onClick = { timerViewModel.reset() },
                                modifier = Modifier
                                    .weight(1f),
                                shape = MaterialTheme.shapes.large
                            ) {
                                Text("Reset")
                            }
                        }
                    }


                    Spacer(Modifier.height(32.dp))
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
