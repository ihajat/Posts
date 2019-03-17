package com.example.posts.data.source.remote.interfaces

import com.example.posts.domain.model.Post
import io.reactivex.Single

interface PostRemote {

    fun get(): Single<List<Post>>

    fun get(postId: String): Single<Post>
}