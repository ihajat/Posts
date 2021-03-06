package com.example.posts.domain.repository.interfaces

import com.example.posts.domain.model.Comment
import io.reactivex.Single

interface CommentRepository {

    fun get(postId: String, refresh: Boolean): Single<List<Comment>>
}
