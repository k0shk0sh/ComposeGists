package com.fastaccess.gists.service

import com.fastaccess.gists.model.Gist
import retrofit2.http.GET

interface GistService {

    @GET("/gists/public")
    suspend fun getGists(): List<Gist>
}