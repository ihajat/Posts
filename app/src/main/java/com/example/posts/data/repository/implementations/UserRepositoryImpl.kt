package com.example.posts.data.repository.implementations

import com.example.posts.data.source.local.cache.interfaces.UserCache
import com.example.posts.data.source.remote.interfaces.UserRemote
import com.example.posts.domain.model.User
import com.example.posts.domain.repository.interfaces.UserRepository
import io.reactivex.Single

class UserRepositoryImpl constructor(
    private val cache: UserCache,
    private val remote: UserRemote
) : UserRepository {

    override fun get(refresh: Boolean): Single<List<User>> =
        when (refresh) {
            true -> remote.get()
                .flatMap { cache.set(it) }
            false -> cache.get()
                .onErrorResumeNext { get(true) }
        }

    override fun get(userId: String, refresh: Boolean): Single<User> =
        when (refresh) {
            true -> remote.get(userId)
                .flatMap { cache.set(it) }
            false -> cache.get(userId)
                .onErrorResumeNext { get(userId, true) }
        }
}
