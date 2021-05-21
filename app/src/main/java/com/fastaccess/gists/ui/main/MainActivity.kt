package com.fastaccess.gists.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
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

@Composable
fun TopBar(navController: NavController) {
    val navState by navController.currentBackStackEntryFlow.collectAsState(initial = "gists")
    val isDetailsRoute = navState is NavBackStackEntry && (navState as NavBackStackEntry)
        .destination.route?.contains("details") == true
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
