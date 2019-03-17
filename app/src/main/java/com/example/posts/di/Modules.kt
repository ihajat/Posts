package com.example.posts.di

import com.example.posts.BuildConfig
import com.example.posts.data.network.createNetworkClient
import com.example.posts.data.repository.CommentRepositoryImpl
import com.example.posts.data.repository.PostRepositoryImpl
import com.example.posts.data.repository.UserRepositoryImpl
import com.example.posts.data.source.local.cache.implementation.CommentCacheImpl
import com.example.posts.data.source.local.cache.implementation.PostCacheImpl
import com.example.posts.data.cache.ReactiveCache
import com.example.posts.data.source.local.cache.implementation.UserCacheImpl
import com.example.posts.data.model.PostEntity
import com.example.posts.data.model.UserEntity
import com.example.posts.data.network.apis.CommentsApi
import com.example.posts.data.network.apis.PostsApi
import com.example.posts.data.network.apis.UsersApi
import com.example.posts.data.source.local.cache.interfaces.UserCache
import com.example.posts.data.source.local.cache.interfaces.PostCache
import com.example.posts.data.source.local.cache.interfaces.CommentCache
import com.example.posts.data.source.remote.interfaces.CommentRemote
import com.example.posts.data.source.remote.interfaces.PostRemote
import com.example.posts.data.source.remote.interfaces.UserRemote
import com.example.posts.data.source.remote.implementation.CommentRemoteImpl
import com.example.posts.data.source.remote.implementation.PostRemoteDataSourceImpl
import com.example.posts.data.source.remote.implementation.UserRemoteImpl
import com.example.posts.domain.model.Comment
import com.example.posts.domain.repository.CommentRepository
import com.example.posts.domain.repository.PostRepository
import com.example.posts.domain.repository.UserRepository
import com.example.posts.domain.usecase.CommentsUseCase
import com.example.posts.domain.usecase.UserPostUseCase
import com.example.posts.domain.usecase.UsersPostsUseCase
import com.example.posts.presentation.postdetails.PostDetailsViewModel
import com.example.posts.presentation.postlist.PostListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

import retrofit2.Retrofit

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataSourceModule,
        networkModule,
        cacheModule
    )
}

val viewModelModule: Module = module {
    viewModel { PostListViewModel(usersPostsUseCase = get()) }
    viewModel { PostDetailsViewModel(userPostUseCase = get(), commentsUseCase = get()) }
}

val useCaseModule: Module = module {
    factory { UsersPostsUseCase(userRepository = get(), postRepository = get()) }
    factory { UserPostUseCase(userRepository = get(), postRepository = get()) }
    factory { CommentsUseCase(commentRepository = get()) }
}

val repositoryModule: Module = module {
    single { UserRepositoryImpl(cache = get(), remote = get()) as UserRepository }
    single { PostRepositoryImpl(cache = get(), remoteDataSource = get()) as PostRepository }
    single { CommentRepositoryImpl(cache = get(), remoteDataSource = get()) as CommentRepository }
}

val dataSourceModule: Module = module {
    single { UserCacheImpl(cache = get(USER_CACHE)) as UserCache }
    single { UserRemoteImpl(api = usersApi) as UserRemote }
    single { PostCacheImpl(cache = get(POST_CACHE)) as PostCache }
    single { PostRemoteDataSourceImpl(api = postsApi) as PostRemote }
    single { CommentCacheImpl(cache = get(COMMENT_CACHE)) as CommentCache }
    single { CommentRemoteImpl(api = commentsApi) as CommentRemote }
}

val networkModule: Module = module {
    single { usersApi }
    single { postsApi }
    single { commentsApi }
}

val cacheModule: Module = module {
    single(name = USER_CACHE) { ReactiveCache<List<UserEntity>>() }
    single(name = POST_CACHE) { ReactiveCache<List<PostEntity>>() }
    single(name = COMMENT_CACHE) { ReactiveCache<List<Comment>>() }
}

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

private val retrofit: Retrofit = createNetworkClient(BASE_URL, BuildConfig.DEBUG)

private val postsApi: PostsApi = retrofit.create(PostsApi::class.java)
private val usersApi: UsersApi = retrofit.create(UsersApi::class.java)
private val commentsApi: CommentsApi = retrofit.create(CommentsApi::class.java)

private const val USER_CACHE = "USER_CACHE"
private const val POST_CACHE = "POST_CACHE"
private const val COMMENT_CACHE = "COMMENT_CACHE"
