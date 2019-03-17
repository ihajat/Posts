package com.example.posts.data.source.remote.implementation

import com.example.posts.data.network.apis.PostsApi
import com.example.posts.data.source.remote.interfaces.PostRemote
import com.example.posts.data.model.mapToDomain
import com.example.posts.domain.model.Post
import io.reactivex.Single

class PostRemoteDataSourceImpl(
    private val api: PostsApi
) : PostRemote {

    override fun get(): Single<List<Post>> =
        api.getPosts()
            .map { it.mapToDomain() }

    override fun get(postId: String): Single<Post> =
        api.getPost(postId)
            .map { it.mapToDomain() }
}
