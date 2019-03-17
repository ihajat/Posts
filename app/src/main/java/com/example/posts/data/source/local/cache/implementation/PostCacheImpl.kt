package com.example.posts.data.source.local.cache.implementation

import com.example.posts.data.source.local.cache.interfaces.PostCache
import com.example.posts.data.cache.ReactiveCache
import com.example.posts.domain.model.Post
import io.reactivex.Single

class PostCacheImpl (
    private val cache: ReactiveCache<List<Post>>
) : PostCache {
    val key = "Post List"

    override fun get(): Single<List<Post>> =
        cache.load(key)

    override fun set(list: List<Post>): Single<List<Post>> =
        cache.save(key, list)

    override fun get(postId: String): Single<Post> =
        cache.load(key)
            .map { it.first { it.id == postId } }

    override fun set(item: Post): Single<Post> =
        cache.load(key)
            .map { it.filter { it.id != item.id }.plus(item) }
            .flatMap { set(it) }
            .map { item }
}
