package com.example.posts.data.source.local.cache.interfaces

import com.example.posts.domain.model.Post
import io.reactivex.Single

interface PostCache {

    fun get(): Single<List<Post>>

    fun set(list: List<Post>): Single<List<Post>>

    fun get(postId: String): Single<Post>

    fun set(item: Post): Single<Post>
}
