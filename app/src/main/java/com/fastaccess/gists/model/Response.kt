package com.fastaccess.gists.model

import com.google.gson.annotations.SerializedName

sealed class Response<out T> {
    object Empty : Response<Nothing>()
    data class Error(val throwable: Throwable? = null) : Response<Nothing>()
    data class Success<T>(val response: T) : Response<T>()
}

data class Gist(
    @SerializedName("comments")
    var comments: Int? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("html_url")
    var htmlUrl: String? = null,
    @SerializedName("id")
    var id: String? = null,
    var owner: Owner? = null,
    @SerializedName("public")
    var `public`: Boolean? = null,
    @SerializedName("truncated")
    var truncated: Boolean? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("user")
    var user: Owner? = null,
)

data class Owner(
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    @SerializedName("html_url")
    var htmlUrl: String? = null,
    @SerializedName("login")
    var login: String? = null,
)