package com.fastaccess.gists.ui.main

import androidx.lifecycle.*
import com.fastaccess.gists.model.Gist
import com.fastaccess.gists.model.Response
import com.fastaccess.gists.repos.GistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: GistRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _loadingState = MutableStateFlow(true)
    private val _responseState =
        savedStateHandle.getLiveData<Response<List<Gist>>>(DATA_KEY, Response.Empty)

    val responseState: LiveData<Response<List<Gist>>> = _responseState
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

    fun getGist(id: String) = (_responseState.value as? Response.Success<List<Gist>>)
        ?.response?.firstOrNull { it.id == id }

    companion object {
        private const val DATA_KEY = "gist_data"
    }
}