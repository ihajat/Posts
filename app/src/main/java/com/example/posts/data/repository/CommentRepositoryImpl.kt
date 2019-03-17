package com.example.posts.data.repository

import com.example.posts.data.source.local.cache.interfaces.CommentCache
import com.example.posts.data.source.remote.interfaces.CommentRemote
import com.example.posts.domain.model.Comment
import com.example.posts.domain.repository.CommentRepository
import io.reactivex.Single

class CommentRepositoryImpl constructor(
    private val cache: CommentCache,
    private val remoteDataSource: CommentRemote
) : CommentRepository {

    override fun get(postId: String, refresh: Boolean): Single<List<Comment>> =
        when (refresh) {
            true -> remoteDataSource.get(postId)
                .flatMap { cache.set(postId, it) }
            false -> cache.get(postId)
                .onErrorResumeNext { get(postId, true) }
        }
}
