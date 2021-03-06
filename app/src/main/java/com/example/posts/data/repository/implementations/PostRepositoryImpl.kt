package com.example.posts.data.repository.implementations

import com.example.posts.data.source.local.cache.interfaces.PostCache
import com.example.posts.data.source.remote.interfaces.PostRemote
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.interfaces.PostRepository
import io.reactivex.Single

class PostRepositoryImpl constructor(
    private val cache: PostCache,
    private val remote: PostRemote
) : PostRepository {

    override fun get(refresh: Boolean): Single<List<Post>> =
        when (refresh) {
            true -> remote.get()
                .flatMap { cache.set(it) }
            false -> cache.get()
                .onErrorResumeNext { get(true) }
        }

    override fun get(postId: String, refresh: Boolean): Single<Post> =
        when (refresh) {
            true -> remote.get(postId)
                .flatMap { cache.set(it) }
            false -> cache.get(postId)
                .onErrorResumeNext { get(postId, true) }
        }
}
