package com.example.cocktailapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment


@Composable
fun HomeTabContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Witaj w aplikacji CocktailApp!", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text(
                    "Tu znajdziesz przepisy na koktajle. " +
                    "Wybierz zakładkę „Łatwe” lub „Trudne”, aby zobaczyć listę " +
                    "koktajli posegregowanych według stopnia trudności.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(32.dp))

        Text("Ustawienia", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tryb ciemny", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onToggleTheme() }
            )
        }

        Spacer(Modifier.height(32.dp))

        Text("Autor:", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Szymon Warguła", style = MaterialTheme.typography.bodyMedium)
    }
}
