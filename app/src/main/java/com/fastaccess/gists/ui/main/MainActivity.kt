package com.fastaccess.gists.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.fastaccess.gists.R
import com.fastaccess.gists.ui.details.GistDetails
import com.fastaccess.gists.ui.list.GistList
import com.fastaccess.gists.ui.theme.ComposeTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                GistApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBar(navController: NavController) {
    val state by navController.currentBackStackEntryAsState()
    val isDetailsRoute = state?.destination?.route?.contains("details") == true
    val title = if (isDetailsRoute) {
        stringResource(R.string.details_label)
    } else {
        stringResource(R.string.app_name)
    }
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.White,
        navigationIcon = if (isDetailsRoute) navIcon(navController) else null
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun navIcon(navController: NavController): @Composable () -> Unit = {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(R.string.back_label))
    }
}

@Composable
fun GistApp() {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar(navController) },
        content = {
            NavHost(navController = navController, startDestination = "gists") {
                composable("gists") {
                    GistList(viewModel, navController)
                }
                composable(
                    "details/{gistId}",
                    arguments = listOf(
                        navArgument("gistId") { type = NavType.StringType }
                    ),
                ) {
                    GistDetails(viewModel, it.arguments?.getString("gistId") ?: "")
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        GistApp()
    }
}
