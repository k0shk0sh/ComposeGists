package com.fastaccess.gists.service

import com.fastaccess.gists.model.Gist
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers

interface GistService {

    @GET("/gists/public")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getGists(): List<Gist>
}