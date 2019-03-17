@file:Suppress("IllegalIdentifier")

package com.example.posts.data.source.remote.implemetation

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.example.posts.commentEntity
import com.example.posts.data.model.mapToDomain
import com.example.posts.data.network.apis.CommentsApi
import com.example.posts.post
import com.example.posts.data.source.remote.implementation.CommentRemoteImpl
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class CommentRemoteImplTest {

    private lateinit var dataSource: CommentRemoteImpl

    private val mockApi: CommentsApi = mock()

    private val postId = post.id

    private val remoteItem = commentEntity.copy(name = "remote")

    private val remoteList = listOf(remoteItem)

    private val throwable = Throwable()

    @Before
    fun setUp() {
        dataSource = CommentRemoteImpl(mockApi)
    }

    @Test
    fun `get comments remote success`() {
        // given
        whenever(mockApi.getComments(postId)).thenReturn(Single.just(remoteList))

        // when
        val test = dataSource.get(postId).test()

        // then
        verify(mockApi).getComments(postId)
        test.assertValue(remoteList.mapToDomain())
    }

    @Test
    fun `get comments remote fail`() {
        // given
        whenever(mockApi.getComments(postId)).thenReturn(Single.error(throwable))

        // when
        val test = dataSource.get(postId).test()

        // then
        verify(mockApi).getComments(postId)
        test.assertError(throwable)
    }
}
