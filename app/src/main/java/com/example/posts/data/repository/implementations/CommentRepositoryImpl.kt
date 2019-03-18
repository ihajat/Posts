package com.example.posts.data.repository.implementations

import com.example.posts.data.source.local.cache.interfaces.CommentCache
import com.example.posts.data.source.remote.interfaces.CommentRemote
import com.example.posts.domain.model.Comment
import com.example.posts.domain.repository.interfaces.CommentRepository
import io.reactivex.Single

class CommentRepositoryImpl constructor(
    private val cache: CommentCache,
    private val remote: CommentRemote
) : CommentRepository {

    override fun get(postId: String, refresh: Boolean): Single<List<Comment>> =
        when (refresh) {
            true -> remote.get(postId)
                .flatMap { cache.set(postId, it) }
            false -> cache.get(postId)
                .onErrorResumeNext { get(postId, true) }
        }
}
