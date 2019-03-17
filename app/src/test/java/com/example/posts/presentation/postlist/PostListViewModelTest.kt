@file:Suppress("IllegalIdentifier")

package com.example.posts.presentation.postlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.example.posts.combinedUserPost

import com.example.posts.presentation.RxSchedulersOverrideRule
import com.example.posts.domain.usecase.UsersPostsUseCase
import com.example.posts.presentation.states.Resource
import com.example.posts.presentation.states.ResourceState
import com.example.posts.presentation.model.mapToPresentation

import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PostListViewModelTest {

    private lateinit var viewModel: PostListViewModel

    private val mockUseCase: UsersPostsUseCase = mock()

    private val combinedUserPostList = listOf(combinedUserPost)
    private val throwable = Throwable()

    @Rule
    @JvmField
    val rxSchedulersOverrideRule = RxSchedulersOverrideRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = PostListViewModel(mockUseCase)
    }

    @Test
    fun `get post item list succeeds`() {
        // given
        whenever(mockUseCase.get(false)).thenReturn(Single.just(combinedUserPostList))

        // when
        viewModel.get(false)

        // then
        verify(mockUseCase).get(false)
        assertEquals(
            Resource(
                ResourceState.SUCCESS,
                combinedUserPostList.mapToPresentation(),
                null
            ),
            viewModel.posts.value
        )
    }

    @Test
    fun `get post item list fails`() {
        // given
        whenever(mockUseCase.get(false)).thenReturn(Single.error(throwable))

        // when
        viewModel.get(false)

        // then
        verify(mockUseCase).get(false)
        assertEquals(
            Resource(
                ResourceState.ERROR,
                null,
                throwable.message
            ),
            viewModel.posts.value
        )
    }
}
