@file:Suppress("IllegalIdentifier")

package com.example.posts.domain.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.example.posts.domain.repository.interfaces.PostRepository
import com.example.posts.domain.repository.interfaces.UserRepository
import com.example.posts.post
import com.example.posts.user
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UsersPostsUseCaseTest {

    private lateinit var usersPostsUseCase: UsersPostsUseCase

    private val mockUserRepository: UserRepository = mock()
    private val mockPostRepository: PostRepository = mock()

    private val userList = listOf(user)
    private val postList = listOf(post)

    @Before
    fun setUp() {
        usersPostsUseCase = UsersPostsUseCase(mockUserRepository, mockPostRepository)
    }

    @Test
    fun `repository get success`() {
        // given
        whenever(mockUserRepository.get(false)).thenReturn(Single.just(userList))
        whenever(mockPostRepository.get(false)).thenReturn(Single.just(postList))

        // when
        val test = usersPostsUseCase.get(false).test()

        // then
        verify(mockUserRepository).get(false)
        verify(mockPostRepository).get(false)

        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(1)
        test.assertValue(map(userList, postList))
    }

    @Test
    fun `repository get fail`() {
        // given
        val throwable = Throwable()
        whenever(mockUserRepository.get(false)).thenReturn(Single.error(throwable))
        whenever(mockPostRepository.get(false)).thenReturn(Single.error(throwable))

        // when
        val test = usersPostsUseCase.get(false).test()

        // then
        verify(mockUserRepository).get(false)
        verify(mockPostRepository).get(false)

        test.assertNoValues()
        test.assertNotComplete()
        test.assertError(throwable)
        test.assertValueCount(0)
    }
}
