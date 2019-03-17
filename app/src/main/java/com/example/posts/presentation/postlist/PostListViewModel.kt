package com.example.posts.presentation.postlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.posts.domain.usecase.UsersPostsUseCase
import com.example.posts.presentation.states.Resource
import com.example.posts.presentation.model.PostItem
import com.example.posts.presentation.model.mapToPresentation
import com.example.posts.presentation.utils.setError
import com.example.posts.presentation.utils.setLoading
import com.example.posts.presentation.utils.setSuccess
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostListViewModel constructor(private val usersPostsUseCase: UsersPostsUseCase) :
    ViewModel() {

    val posts = MutableLiveData<Resource<List<PostItem>>>()
    private val compositeDisposable = CompositeDisposable()

    fun get(refresh: Boolean = false) =
        compositeDisposable.add(usersPostsUseCase.get(refresh)
            .doOnSubscribe { posts.setLoading() }
            .subscribeOn(Schedulers.io())
            .map { it.mapToPresentation() }
            .subscribe({ posts.setSuccess(it) }, { posts.setError(it.message) })
        )

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
