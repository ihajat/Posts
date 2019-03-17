package com.example.posts.data.network.apis

import com.example.posts.data.model.PostEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApi {

    @GET("posts/")
    fun getPosts(): Single<List<PostEntity>>

    @GET("posts/{id}")
    fun getPost(@Path("id") postId: String): Single<PostEntity>
}