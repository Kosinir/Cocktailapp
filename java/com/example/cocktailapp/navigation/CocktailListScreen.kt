// ui/list/CocktailListScreen.kt
package com.example.cocktailapp.navigation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cocktailapp.data.viewmodel.CocktailListViewModel
import com.example.cocktailapp.data.local.CocktailRepository
import com.example.cocktailapp.data.local.AppDatabase
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
fun CocktailListScreen(
    onItemClick: (Int) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel = remember {
        CocktailListViewModel(
            CocktailRepository(AppDatabase.getInstance(context).cocktailDao())
        )
    }
    val allCocktails by viewModel.cocktails.collectAsState()
    val tabTitles = listOf("Home", "Łatwe", "Trudne")

    val pages = listOf(2) + listOf(0, 1, 2) + listOf(0)
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0) {
            delay(100)
            pagerState.scrollToPage(pages.size - 2)
        } else if (pagerState.currentPage == pages.size - 1) {
            delay(100)
            pagerState.scrollToPage(1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Koktaili") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = when (pagerState.currentPage) {
                    0 -> 2
                    pages.size - 1 -> 0
                    else -> pagerState.currentPage - 1
                },
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[when (pagerState.currentPage) {
                                0 -> 2
                                pages.size - 1 -> 0
                                else -> pagerState.currentPage - 1
                            }]
                        )
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = when (pagerState.currentPage) {
                            0 -> index == 2
                            pages.size - 1 -> index == 0
                            else -> index == pagerState.currentPage - 1
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index + 1)
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                when (pages[pageIndex]) {
                    0 -> HomeTabContent(isDarkTheme, onToggleTheme)
                    1 -> CategoryTabContent(
                        cocktails = allCocktails.filter { it.prepTimeMinutes <= 3 },
                        onItemClick = onItemClick,
                        context = context
                    )
                    2 -> CategoryTabContent(
                        cocktails = allCocktails.filter { it.prepTimeMinutes > 3 },
                        onItemClick = onItemClick,
                        context = context
                    )
                }
            }
        }
    }
}
