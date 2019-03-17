@file:Suppress("IllegalIdentifier")

package com.example.posts.data.repository

import com.example.posts.user
import com.example.posts.data.source.local.cache.interfaces.UserCache
import com.example.posts.data.source.remote.interfaces.UserRemote
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl

    private val mockCache: UserCache = mock()
    private val mockRemote: UserRemote = mock()

    private val userId = user.id

    private val cacheItem = user.copy(name = "cache")
    private val remoteItem = user.copy(name = "remote")

    private val cacheList = listOf(cacheItem)
    private val remoteList = listOf(remoteItem)

    private val cacheThrowable = Throwable()
    private val remoteThrowable = Throwable()

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(mockCache, mockRemote)
    }

    @Test
    fun `get users cache success`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.just(cacheList))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        test.assertValue(cacheList)
    }

    @Test
    fun `get users cache fail fallback remote succeeds`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemote.get()).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        verify(mockRemote).get()
        verify(mockCache).set(remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get users cache fail fallback remote fails`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemote.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        verify(mockRemote).get()
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get users remote success`() {
        // given
        whenever(mockRemote.get()).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemote).get()
        verify(mockCache).set(remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get users remote fail`() {
        // given
        whenever(mockRemote.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemote).get()
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get user cache success`() {
        // given
        whenever(mockCache.get(userId)).thenReturn(Single.just(cacheItem))

        // when
        val test = repository.get(userId, false).test()

        // then
        verify(mockCache).get(userId)
        test.assertValue(cacheItem)
    }

    @Test
    fun `get user cache fail fallback remote succeeds`() {
        // given
        whenever(mockCache.get(userId)).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemote.get(userId)).thenReturn(Single.just(remoteItem))
        whenever(mockCache.set(remoteItem)).thenReturn(Single.just(remoteItem))

        // when
        val test = repository.get(userId, false).test()

        // then
        verify(mockCache).get(userId)
        verify(mockRemote).get(userId)
        verify(mockCache).set(remoteItem)
        test.assertValue(remoteItem)
    }

    @Test
    fun `get user cache fail fallback remote fails`() {
        // given
        whenever(mockCache.get(userId)).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemote.get(userId)).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(userId, false).test()

        // then
        verify(mockCache).get(userId)
        verify(mockRemote).get(userId)
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get user remote success`() {
        // given
        whenever(mockRemote.get(userId)).thenReturn(Single.just(remoteItem))
        whenever(mockCache.set(remoteItem)).thenReturn(Single.just(remoteItem))

        // when
        val test = repository.get(userId, true).test()

        // then
        verify(mockRemote).get(userId)
        verify(mockCache).set(remoteItem)
        test.assertValue(remoteItem)
    }

    @Test
    fun `get user remote fail`() {
        // given
        whenever(mockRemote.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemote).get()
        test.assertError(remoteThrowable)
    }
}
