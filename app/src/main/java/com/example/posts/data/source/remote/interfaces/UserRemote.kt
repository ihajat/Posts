package com.example.posts.data.source.remote.interfaces

import com.example.posts.domain.model.User
import io.reactivex.Single

interface UserRemote {

    fun get(): Single<List<User>>

    fun get(userId: String): Single<User>
}
