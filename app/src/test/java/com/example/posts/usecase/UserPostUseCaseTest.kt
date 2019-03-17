package com.example.posts.usecase

import com.example.posts.domain.repository.PostRepository
import com.example.posts.domain.repository.UserRepository
import com.example.posts.domain.usecase.UserPostUseCase
import com.example.posts.domain.usecase.map
import com.example.posts.post
import com.example.posts.user
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UserPostUseCaseTest {

    private lateinit var userPostUseCase: UserPostUseCase

    private val mockUserRepository: UserRepository = mock {}
    private val mockPostRepository: PostRepository = mock {}

    private val userId = user.id
    private val postId = post.id

    @Before
    fun setUp() {
        userPostUseCase = UserPostUseCase(mockUserRepository, mockPostRepository)
    }

    @Test
    fun `repository get success`() {
        // given
        whenever(mockUserRepository.get(userId, false)).thenReturn(Single.just(user))
        whenever(mockPostRepository.get(postId, false)).thenReturn(Single.just(post))

        // when
        val test = userPostUseCase.get(userId, postId, false).test()

        // then
        verify(mockUserRepository).get(userId, false)
        verify(mockPostRepository).get(postId, false)

        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(1)
        test.assertValue(map(user, post))
    }

    @Test
    fun `repository get fail`() {
        // given
        val throwable = Throwable()
        whenever(mockUserRepository.get(userId, false)).thenReturn(Single.error(throwable))
        whenever(mockPostRepository.get(postId, false)).thenReturn(Single.error(throwable))

        // when
        val test = userPostUseCase.get(userId, postId, false).test()

        // then
        verify(mockUserRepository).get(userId, false)
        verify(mockPostRepository).get(postId, false)

        test.assertNoValues()
        test.assertNotComplete()
        test.assertError(throwable)
        test.assertValueCount(0)
    }
}
