@file:Suppress("IllegalIdentifier")

package com.example.posts.presentation.model

import com.example.posts.domain.usecase.CombinedUserPost
import com.example.posts.post
import com.example.posts.user
import org.junit.Assert.assertTrue
import org.junit.Test

class PostItemTest {

    @Test
    fun `map domain to presentation`() {
        // given
        val combinedUserPost = CombinedUserPost(user, post)

        // when
        val postItem = combinedUserPost.mapToPresentation()

        // then
        assertTrue(postItem.postId == post.id)
        assertTrue(postItem.userId == user.id)
        assertTrue(postItem.title == post.title)
        assertTrue(postItem.body == post.body)
        assertTrue(postItem.name == user.name)
        assertTrue(postItem.username == user.username)
        assertTrue(postItem.email == user.email)
    }
}
