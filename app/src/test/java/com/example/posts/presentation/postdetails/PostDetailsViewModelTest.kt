@file:Suppress("IllegalIdentifier")

package com.example.posts.presentation.postdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.example.posts.combinedUserPost
import com.example.posts.comment
import com.example.posts.domain.usecase.CommentsUseCase
import com.example.posts.domain.usecase.UserPostUseCase
import com.example.posts.post
import com.example.posts.presentation.states.Resource
import com.example.posts.presentation.states.ResourceState
import com.example.posts.presentation.RxSchedulersOverrideRule
import com.example.posts.presentation.model.mapToPresentation
import com.example.posts.user
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PostDetailsViewModelTest {

    private lateinit var viewModel: PostDetailsViewModel

    private val mockUserPostUseCase: UserPostUseCase = mock()
    private val mockCommentsUseCase: CommentsUseCase = mock()

    private val comments = listOf(comment)

    private val userId = user.id
    private val postId = post.id

    private val throwable = Throwable()

    @Rule
    @JvmField
    val rxSchedulersOverrideRule = RxSchedulersOverrideRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = PostDetailsViewModel(mockUserPostUseCase, mockCommentsUseCase)
    }

    @Test
    fun `get post succeeds`() {
        // given
        whenever(mockUserPostUseCase.get(userId, postId, false))
            .thenReturn(Single.just(combinedUserPost))

        // when
        viewModel.getPost(UserIdPostId(userId, postId))

        // then
        verify(mockUserPostUseCase).get(userId, postId, false)
        assertEquals(combinedUserPost.mapToPresentation(), viewModel.post.value)
    }

    @Test
    fun `get comments succeeds`() {
        // given
        whenever(mockCommentsUseCase.get(postId, false)).thenReturn(Single.just(comments))

        // when
        viewModel.getComments(postId, false)

        // then
        verify(mockCommentsUseCase).get(postId, false)
        assertEquals(
            Resource(
                state = ResourceState.SUCCESS,
                data = comments.mapToPresentation(),
                message = null
            ), viewModel.comments.value
        )
    }

    @Test
    fun `get comments fails`() {
        // given
        whenever(mockCommentsUseCase.get(postId, true)).thenReturn(Single.error(throwable))

        // when
        viewModel.getComments(postId, true)

        // then
        verify(mockCommentsUseCase).get(postId, true)
        assertEquals(
            Resource(
                state = ResourceState.ERROR,
                data = null,
                message = throwable.message
            ),
            viewModel.comments.value
        )
    }
}
