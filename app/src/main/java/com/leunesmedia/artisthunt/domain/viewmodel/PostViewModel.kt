package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
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
    fun retrievePosts() {
        subscription = postApi.getPosts(userRepo.user.value?._id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result.forEach {
                        if (!userRepo.user.value?.post_ids!!.contains(it._id)) {
                            userRepo.user.value?.post_ids!!.add(it._id!!)
                            postRepo.insert(it)
                        }
                        userRepo.updateUser(userRepo.user.value!!)
                    }
                    uiMessage.postValue(Model.Message("retrievePostsSucces"))

                },
                { error ->
                    uiMessage.postValue(Model.Message("retrievePostsError"))
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
                    userRepo.user.value?.post_ids!!.add(result._id!!)
                    userRepo.updateUser(userRepo.user.value!!)
                    uiMessage.postValue(Model.Message("addPostsSucces"))

                },
                { error ->
                    uiMessage.postValue(Model.Message("addPostsError"))
                }
            )
    }


}