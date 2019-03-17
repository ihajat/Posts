package com.example.posts.data.source.remote.implementation

import com.example.posts.data.network.apis.UsersApi
import com.example.posts.data.source.remote.interfaces.UserRemote
import com.example.posts.data.model.mapToDomain
import com.example.posts.domain.model.User
import io.reactivex.Single

class UserRemoteImpl(
    private val api: UsersApi
) : UserRemote {

    override fun get(): Single<List<User>> =
        api.getUsers()
            .map { it.mapToDomain() }

    override fun get(userId: String): Single<User> =
        api.getUser(userId)
            .map { it.mapToDomain() }
}
