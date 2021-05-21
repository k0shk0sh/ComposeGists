package com.fastaccess.gists.ui.details

import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fastaccess.gists.R
import com.fastaccess.gists.ui.main.MainViewModel
import com.fastaccess.gists.ui.views.CenteredBox
import com.fastaccess.gists.ui.views.TextView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GistDetails(viewModel: MainViewModel, id: String) {
    val gist = viewModel.getGist(id) ?: run {
        CenteredBox { TextView(stringResource(R.string.no_gist_label)) }
        return
    }

    Column {
        Card(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Column {
                TextView(text = gist.description)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextView(text = stringResource(R.string.comments_label, gist.comments ?: 0))
                    TextView(text = stringResource(R.string.files_label, gist.files?.size ?: 0))
                    TextView(
                        text = DateUtils.getRelativeTimeSpanString(gist.createdAt?.time ?: 0)
                            .toString()
                    )
                }
            }
        }
        LazyVerticalGrid(cells = GridCells.Fixed(gist.files?.size ?: 0)) {
            items(gist.files?.values?.toList() ?: listOf()) { file ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Column {
                        TextView(text = file.filename, modifier = Modifier)
                        val formattedSize =
                            Formatter.formatFileSize(LocalContext.current, file.size ?: 0L)
                        TextView(text = "${file.type} - $formattedSize")
                    }
                }
            }
        }
    }
}