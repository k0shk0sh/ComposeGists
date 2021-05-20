package com.fastaccess.gists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fastaccess.gists.model.Response
import com.fastaccess.gists.ui.theme.ComposeTestTheme
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Gists Compose") },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.White
    )
}

@Composable
fun GistApp(viewModel: MainViewModel = viewModel()) {
    Scaffold(
        topBar = { TopBar() },
        content = { Body(viewModel) },
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Body(viewModel: MainViewModel) {
    val state = viewModel.responseState.collectAsState()
    val isLoading by viewModel.loadingState.collectAsState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading),
        modifier = Modifier.padding(vertical = 16.dp),
        onRefresh = { viewModel.loadGists() }) {
        when (val response = state.value) {
            Response.Empty -> CenteredBox { TextView("No gists") }
            is Response.Error -> CenteredBox {
                TextView(response.throwable?.localizedMessage ?: "N/A")
            }
            is Response.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(response.response.size) { index ->
                    val gist = response.response[index]
                    var expanded by remember { mutableStateOf(false) }
                    val hasDescription = !gist.description.isNullOrEmpty()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { expanded = !expanded && hasDescription },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberCoilPainter(
                                    request = gist.owner?.avatarUrl ?: "",
                                ),
                                contentDescription = gist.owner?.login ?: "",
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(text = gist.owner?.login ?: "", maxLines = 1)
                            Spacer(modifier = Modifier.weight(1f))
                            if (hasDescription) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_down),
                                    contentDescription = "",
                                    modifier = Modifier.rotate(if (expanded) 180F else 0F)
                                )
                            }
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(text = gist.description ?: "", modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextView(
    text: String,
) = Text(
    text = text,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
)

@Composable
fun CenteredBox(content: @Composable() () -> Unit) =
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        content.invoke()
    }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        GistApp()
    }
}