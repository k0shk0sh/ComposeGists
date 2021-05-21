package com.fastaccess.gists.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fastaccess.gists.R
import com.fastaccess.gists.model.Gist
import com.fastaccess.gists.model.Response
import com.fastaccess.gists.ui.main.MainViewModel
import com.fastaccess.gists.ui.main.TopBar
import com.fastaccess.gists.ui.views.CenteredBox
import com.fastaccess.gists.ui.views.TextView
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GistList(viewModel: MainViewModel, navController: NavHostController) {
    val isLoading by viewModel.loadingState.collectAsState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading),
        modifier = Modifier.padding(vertical = 16.dp),
        onRefresh = { viewModel.loadGists() }) {
        val responseState by viewModel.responseState.observeAsState()
        when (@Suppress("UnnecessaryVariable") val state = responseState) {
            Response.Empty -> CenteredBox { TextView(stringResource(R.string.no_gists_label)) }
            is Response.Error -> CenteredBox {
                TextView(state.throwable?.localizedMessage ?: stringResource(R.string.n_a))
            }
            is Response.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.response) { gist ->
                    var expanded by remember { mutableStateOf(false) }
                    val hasDescription = !gist.description.isNullOrEmpty()
                    val rotationState by animateFloatAsState(
                        if (expanded) 180F else 0F,
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable {
                                expanded = !expanded && hasDescription
                            },
                    ) {
                        GistRow(gist, hasDescription, rotationState)
                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(text = gist.description ?: "",
                                    modifier = Modifier.padding(4.dp))
                                TextButton(onClick = { navController.navigate("details/${gist.id}") },
                                    modifier = Modifier.align(
                                        Alignment.End,
                                    )) {
                                    TextView(text = stringResource(R.string.read_more_label))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GistRow(
    gist: Gist,
    hasDescription: Boolean,
    rotationState: Float,
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
                modifier = Modifier.rotate(rotationState)
            )
        }
    }
}
