package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.leunesmedia.artisthunt.domain.InjectedViewModel
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.persistence.API.PostApi
import com.leunesmedia.artisthunt.persistence.local_db.repository.PostRepository
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostViewModel : InjectedViewModel() {
    var uiMessage = MutableLiveData<Model.Message>()
    var userPosts = MutableLiveData<Array<Model.Post>>()

    private lateinit var subscription: Disposable

    @Inject
    lateinit var postApi: PostApi
    @Inject
    lateinit var postRepo: PostRepository
    @Inject
    lateinit var userRepo: UserRespository


    /**
     * Hier worden alle posts opgehaald. Deze methode word opgeroepen in de postfragment.
     * Bij succes wordt de posts geupdated en wordt de observer in postfragment getriggered.
     * UiMessage zorgt ervoor dat de UI correct wordt geupdated
     */
    fun retrieveAllPosts() {
        subscription = postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result.forEach {
                        if (!postRepo.allPosts.value?.contains(it)!!) {
                            postRepo.insert(it)
                        }
                    }
                    uiMessage.postValue(Model.Message("retrieveAllPostsSucces"))

                },
                { error ->
                    uiMessage.postValue(Model.Message("retrieveAllPostsError"))
                }
            )
    }

    /**
     * Hier worden alle posts opgehaald van de USER. Deze methode word opgeroepen in de postfragment.
     * Bij succes wordt de posts geupdated en wordt de observer in postfragment getriggered.
     * UiMessage zorgt ervoor dat de UI correct wordt geupdated
     */
    fun retrieveUserPosts() {
        subscription = postApi.getUserPosts(userRepo.user.value?._id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result.forEach {
                        if (!postRepo.allPosts.value?.contains(it)!!) {
                            postRepo.insert(it)
                        }
                    }
                    userPosts.postValue(result)
                    uiMessage.postValue(Model.Message("retrieveUserPostsSucces"))
                    Log.d("Bla", result.toString())
                },
                { error ->
                    Log.d("Bla", error.message.toString())
                    uiMessage.postValue(Model.Message("retrieveUserPostsError"))
                }
            )
    }

    fun addPost(post: Model.Post) {
        subscription = postApi.addPost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    postRepo.insert(result)
                    uiMessage.postValue(Model.Message("addPostsSucces"))

                },
                { error ->
                    uiMessage.postValue(Model.Message("addPostsError"))
                }
            )
    }


}