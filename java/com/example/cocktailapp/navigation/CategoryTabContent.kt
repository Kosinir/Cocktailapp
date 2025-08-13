package com.example.cocktailapp.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cocktailapp.data.local.CocktailEntity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.cocktailapp.data.viewmodel.CocktailListViewModel
import com.example.cocktailapp.data.local.CocktailRepository
import com.example.cocktailapp.data.local.AppDatabase
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@Composable
fun CategoryTabContent(
    cocktails: List<CocktailEntity>,
    onItemClick: (Int) -> Unit,
    context: Context
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cocktails) { cocktail ->
            val imageId = remember(cocktail.imageRes) {
                context.resources.getIdentifier(
                    cocktail.imageRes, "drawable", context.packageName
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(cocktail.id) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(cocktail.name, style = MaterialTheme.typography.titleMedium)
                        Text("Czas: ${cocktail.prepTimeMinutes} min", style = MaterialTheme.typography.bodySmall)
                    }
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = cocktail.name,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        if (cocktails.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Brak koktajli w tej kategorii")
                }
            }
        }
    }
}
