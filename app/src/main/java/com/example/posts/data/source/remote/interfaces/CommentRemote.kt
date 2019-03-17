package com.example.posts.data.source.remote.interfaces

import com.example.posts.domain.model.Comment
import io.reactivex.Single

interface CommentRemote {

    fun get(postId: String): Single<List<Comment>>
}
