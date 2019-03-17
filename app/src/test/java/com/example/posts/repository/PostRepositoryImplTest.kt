@file:Suppress("IllegalIdentifier")

package com.example.posts.data.repository

import com.example.posts.post
import com.example.posts.data.source.local.cache.interfaces.PostCache
import com.example.posts.data.source.remote.interfaces.PostRemote
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    private lateinit var repository: PostRepositoryImpl

    private val mockCache: PostCache = mock()
    private val mockRemoteDataSource: PostRemote = mock()

    private val postId = post.id

    private val cacheItem = post.copy(title = "cache")
    private val remoteItem = post.copy(title = "remote")

    private val cacheList = listOf(cacheItem)
    private val remoteList = listOf(remoteItem)

    private val cacheThrowable = Throwable()
    private val remoteThrowable = Throwable()

    @Before
    fun setUp() {
        repository = PostRepositoryImpl(mockCache, mockRemoteDataSource)
    }

    @Test
    fun `get posts cache success`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.just(cacheList))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        test.assertValue(cacheList)
    }

    @Test
    fun `get posts cache fail fallback remote succeeds`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemoteDataSource.get()).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        verify(mockRemoteDataSource).get()
        verify(mockCache).set(remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get posts cache fail fallback remote fails`() {
        // given
        whenever(mockCache.get()).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemoteDataSource.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(false).test()

        // then
        verify(mockCache).get()
        verify(mockRemoteDataSource).get()
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get posts remote success`() {
        // given
        whenever(mockRemoteDataSource.get()).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemoteDataSource).get()
        verify(mockCache).set(remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get posts remote fail`() {
        // given
        whenever(mockRemoteDataSource.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemoteDataSource).get()
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get post cache success`() {
        // given
        whenever(mockCache.get(postId)).thenReturn(Single.just(cacheItem))

        // when
        val test = repository.get(postId, false).test()

        // then
        verify(mockCache).get(postId)
        test.assertValue(cacheItem)
    }

    @Test
    fun `get post cache fail fallback remote succeeds`() {
        // given
        whenever(mockCache.get(postId)).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.just(remoteItem))
        whenever(mockCache.set(remoteItem)).thenReturn(Single.just(remoteItem))

        // when
        val test = repository.get(postId, false).test()

        // then
        verify(mockCache).get(postId)
        verify(mockRemoteDataSource).get(postId)
        verify(mockCache).set(remoteItem)
        test.assertValue(remoteItem)
    }

    @Test
    fun `get post cache fail fallback remote fails`() {
        // given
        whenever(mockCache.get(postId)).thenReturn(Single.error(cacheThrowable))
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(postId, false).test()

        // then
        verify(mockCache).get(postId)
        verify(mockRemoteDataSource).get(postId)
        test.assertError(remoteThrowable)
    }

    @Test
    fun `get post remote success`() {
        // given
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.just(remoteItem))
        whenever(mockCache.set(remoteItem)).thenReturn(Single.just(remoteItem))

        // when
        val test = repository.get(postId, true).test()

        // then
        verify(mockRemoteDataSource).get(postId)
        verify(mockCache).set(remoteItem)
        test.assertValue(remoteItem)
    }

    @Test
    fun `get post remote fail`() {
        // given
        whenever(mockRemoteDataSource.get()).thenReturn(Single.error(remoteThrowable))

        // when
        val test = repository.get(true).test()

        // then
        verify(mockRemoteDataSource).get()
        test.assertError(remoteThrowable)
    }
}
