package com.example.posts.data.source.remote.implementation

import com.example.posts.data.network.apis.CommentsApi
import com.example.posts.data.source.remote.interfaces.CommentRemote
import com.example.posts.data.model.mapToDomain
import com.example.posts.domain.model.Comment
import io.reactivex.Single

class CommentRemoteImpl(
    private val api: CommentsApi
) : CommentRemote {

    override fun get(postId: String): Single<List<Comment>> =
        api.getComments(postId)
            .map { it.mapToDomain() }
}
