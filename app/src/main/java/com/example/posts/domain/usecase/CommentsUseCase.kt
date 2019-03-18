package com.example.posts.domain.usecase

import com.example.posts.domain.model.Comment
import com.example.posts.domain.repository.interfaces.CommentRepository
import io.reactivex.Single

class CommentsUseCase constructor(private val commentRepository: CommentRepository) {

    fun get(postId: String, refresh: Boolean): Single<List<Comment>> =
        commentRepository.get(postId, refresh)
}
