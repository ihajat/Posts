package com.example.posts.data.source.local.cache.interfaces

import com.example.posts.domain.model.User
import io.reactivex.Single

interface UserCache {

    fun get(): Single<List<User>>

    fun set(item: User): Single<User>

    fun get(userId: String): Single<User>

    fun set(list: List<User>): Single<List<User>>
}
