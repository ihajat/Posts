package com.example.posts

import com.example.posts.data.model.CommentEntity
import com.example.posts.data.model.PostEntity
import com.example.posts.data.model.UserEntity
import com.example.posts.domain.model.Comment
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.User
import com.example.posts.domain.usecase.CombinedUserPost

val user = User("userId", "name", "username", "email")
val post = Post("userId", "postId", "title", "body")
val comment = Comment("postId", "commentId", "name", "email", "body")

val combinedUserPost = CombinedUserPost(user, post)

val userEntity = UserEntity("userId", "name", "username", "email")
val postEntity = PostEntity("userId", "postId", "title", "body")
val commentEntity = CommentEntity("postId", "commentId", "name", "email", "body")
