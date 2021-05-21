package com.fastaccess.gists.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fastaccess.gists.ui.main.MainViewModel
import com.fastaccess.gists.ui.views.CenteredBox
import com.fastaccess.gists.ui.views.TextView

@Composable
fun GistDetails(viewModel: MainViewModel, id: String) {
    val gist = viewModel.getGist(id) ?: run {
        CenteredBox { TextView("Gist is null") }
        return
    }
    Column {
        if (!gist.description.isNullOrEmpty()) {
            TextView(text = gist.description ?: "")
            Spacer(modifier = Modifier.padding(16.dp))
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {

        }
    }
}