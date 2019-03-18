@file:Suppress("IllegalIdentifier")

package com.example.posts.data.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.example.posts.comment
import com.example.posts.data.repository.implementations.CommentRepositoryImpl
import com.example.posts.post
import com.example.posts.data.source.local.cache.interfaces.CommentCache
import com.example.posts.data.source.remote.interfaces.CommentRemote
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class CommentRepositoryImplTest {

    private lateinit var repository: CommentRepositoryImpl

    private val mockCache: CommentCache = mock()
    private val mockRemoteDataSource: CommentRemote = mock()

    private val postId = post.id

    private val cacheItem = comment.copy(name = "cache")
    private val remoteItem = comment.copy(name = "remote")

    private val cacheList = listOf(cacheItem)
    private val remoteList = listOf(remoteItem)

    private val throwable = Throwable()

    @Before
    fun setUp() {
        repository =
            CommentRepositoryImpl(mockCache, mockRemoteDataSource)
    }

    @Test
    fun `get comments cache success`() {
        // given
        whenever(mockCache.get(postId)).thenReturn(Single.just(cacheList))

        // when
        val test = repository.get(postId, false).test()

        // then
        verify(mockCache).get(postId)
        test.assertValue(cacheList)
    }

    @Test
    fun `get comments cache fail fallback remote succeeds`() {
        // given
        whenever(mockCache.get(postId)).thenReturn(Single.error(throwable))
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(postId, remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(postId, false).test()

        // then
        verify(mockCache).get(postId)
        verify(mockRemoteDataSource).get(postId)
        verify(mockCache).set(postId, remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get comments remote success`() {
        // given
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.just(remoteList))
        whenever(mockCache.set(postId, remoteList)).thenReturn(Single.just(remoteList))

        // when
        val test = repository.get(postId, true).test()

        // then
        verify(mockRemoteDataSource).get(postId)
        verify(mockCache).set(postId, remoteList)
        test.assertValue(remoteList)
    }

    @Test
    fun `get comments remote fail`() {
        // given
        whenever(mockRemoteDataSource.get(postId)).thenReturn(Single.error(throwable))

        // when
        val test = repository.get(postId, true).test()

        // then
        verify(mockRemoteDataSource).get(postId)
        test.assertError(throwable)
    }
}
