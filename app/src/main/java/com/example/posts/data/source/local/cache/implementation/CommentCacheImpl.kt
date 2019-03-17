package com.example.posts.data.source.local.cache.implementation

import com.example.posts.data.source.local.cache.interfaces.CommentCache
import com.example.posts.data.cache.ReactiveCache
import com.example.posts.domain.model.Comment
import io.reactivex.Single

class CommentCacheImpl (
    private val cache: ReactiveCache<List<Comment>>
) : CommentCache {

    val key = "Comment List"

    override fun get(postId: String): Single<List<Comment>> =
        cache.load(key + postId)

    override fun set(postId: String, list: List<Comment>): Single<List<Comment>> =
        cache.save(key + postId, list)
}
