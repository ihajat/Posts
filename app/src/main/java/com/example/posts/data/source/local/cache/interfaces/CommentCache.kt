package com.example.posts.data.source.local.cache.interfaces

import com.example.posts.domain.model.Comment
import io.reactivex.Single

interface CommentCache {

    fun get(postId: String): Single<List<Comment>>

    fun set(postId: String, list: List<Comment>): Single<List<Comment>>
}
