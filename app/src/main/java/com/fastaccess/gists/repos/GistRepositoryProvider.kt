package com.fastaccess.gists.repos

import android.util.Log
import com.fastaccess.gists.model.Gist
import com.fastaccess.gists.model.Response
import com.fastaccess.gists.service.GistService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GistRepositoryProvider @Inject constructor(
    private val service: GistService,
    private val dispatcher: CoroutineDispatcher,
) : GistRepository {
    override suspend fun getGists(): Response<List<Gist>> = withContext(dispatcher) {
        try {
            val response = service.getGists()
            if (!response.isNullOrEmpty()) {
                Log.e("Response", "${response.map { it.files }}")
                return@withContext Response.Success(response)
            }
            return@withContext Response.Empty
        } catch (e: Throwable) {
            e.printStackTrace()
            return@withContext Response.Error(e)
        }
    }
}

interface GistRepository {
    suspend fun getGists(): Response<List<Gist>>
}