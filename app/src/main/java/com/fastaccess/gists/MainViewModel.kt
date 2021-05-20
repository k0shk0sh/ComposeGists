package com.fastaccess.gists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastaccess.gists.model.Gist
import com.fastaccess.gists.model.Response
import com.fastaccess.gists.repos.GistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: GistRepository,
) : ViewModel() {

    private val _responseState = MutableStateFlow<Response<List<Gist>>>(Response.Empty)
    private val _loadingState = MutableStateFlow(true)

    val responseState: StateFlow<Response<List<Gist>>> = _responseState
    val loadingState: StateFlow<Boolean> = _loadingState

    init {
        loadGists()
    }

    fun loadGists() {
        _loadingState.value = true
        viewModelScope.launch {
            _responseState.value = repo.getGists()
            delay(300) // delay so the pullToRefresh doesn't disappear immediately.
            _loadingState.value = false
        }
    }
}