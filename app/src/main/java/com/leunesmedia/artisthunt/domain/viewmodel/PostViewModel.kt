package com.leunesmedia.artisthunt.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.util.Log
import com.leunesmedia.artisthunt.domain.InjectedViewModel
import com.leunesmedia.artisthunt.domain.Model
import com.leunesmedia.artisthunt.network.API.PostApi
import com.leunesmedia.artisthunt.persistence.local_db.repository.PostRepository
import com.leunesmedia.artisthunt.persistence.local_db.repository.UserRespository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * This viewmodel class contains all the networking and repository logic for a post and injects PostRepository, UserRepository and PostAPI
 * It contains a uiMessage and the userPosts of the current User which the UI can observe
 */
class PostViewModel : InjectedViewModel() {
    var uiMessage = MutableLiveData<Model.Message>()
    var userPosts = MutableLiveData<List<Model.Post>>()
    private lateinit var subscription: Disposable

    @Inject
    lateinit var postApi: PostApi
    @Inject
    lateinit var postRepo: PostRepository
    @Inject
    lateinit var userRepo: UserRespository


    /**
     * All Posts are retrieved and shown to the user. UiMessage gets updated on Result and ond Error
     * If the Room Database doesn't contain a post from the result, it is added here to the Database
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
                    Log.d("HIHIHI", error.message)
                    uiMessage.postValue(Model.Message("retrieveAllPostsError"))
                }
            )
    }

    /**
     * All User Posts are retrieved and shown to the user. UiMessage gets updated on Result and ond Error
     * If the Room Database doesn't contain a userpost from the result, it is added here to the Database
     * The userPosts LiveData gets populated with data from the Room Database
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
                    userPosts.postValue(postRepo.getUserPosts(userRepo.user.value?._id!!))
                    uiMessage.postValue(Model.Message("retrieveUserPostsSucces"))
                },
                { error ->
                    userPosts.postValue(postRepo.getUserPosts(userRepo.user.value?._id!!))
                    uiMessage.postValue(Model.Message("retrieveUserPostsError"))
                }
            )
    }

    /**
     * Text Post is added and inserted in the Server and Local Room Database
     * UiMessage is updated accordingly
     */
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

    /**
     * Image Post is added and inserted in the Server and Local Room Database
     * UiMessage is updated accordingly
     */
    fun addImagePost(file: File, bitmap: Bitmap, post: Model.Post) {
        val reqFile = RequestBody.create(
            MediaType.parse("image/jpg"),
            file
        )
        val body = MultipartBody.Part.createFormData("file", file.name + ".jpg", reqFile)
        subscription = postApi.addImagePost(post, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    postRepo.insert(result)
                    uiMessage.postValue(Model.Message("addPostImageSucces"))
                },
                { error ->
                    Log.d("HIHIHI", error.message)
                    uiMessage.postValue(Model.Message("addPostImageError"))
                }
            )

    }

    /**
     * Tries to update the likes for a post.
     * If succesful, the post shall be liked or unliked accordingly and room database gets updated
     * UiMessage is updated accordingly
     */
    fun updateLikers(post : Model.Post, liker: Model.updateLiker) {
        subscription = postApi.updatePostLikers(post._id!!, liker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (post.likers.contains(liker.liker)) {
                        post.likers.remove(liker.liker)
                    } else {
                        post.likers.add(liker.liker)
                    }
                    postRepo.updatePost(post)
                    uiMessage.postValue(Model.Message("likesUpdated"))
                },
                { error ->
                    Log.d("updateError", error.message)
                    uiMessage.postValue(Model.Message("likesUpdateError"))
                }
            )
    }

}