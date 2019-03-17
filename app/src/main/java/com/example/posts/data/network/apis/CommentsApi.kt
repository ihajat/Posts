package com.example.posts.data.network.apis

import com.example.posts.data.model.CommentEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentsApi {

    @GET("comments/")
    fun getComments(@Query("postId") postId: String): Single<List<CommentEntity>>
}
