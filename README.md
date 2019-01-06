# Artist Hunt

This is an app for musicians, by fellow musicians. Find eachother, colab and make some new music together!

## Getting Started

Download the full APK from here or from the Play Store
> [ArtistHunt APK](https://drive.google.com/file/d/1GomuOmWdP_KsUwzLAD1S6UHjv-ioo_dW/view?usp=sharing)

### Prerequisites

An Android phone with at least API 19 (Kitkat 4.4)
Make sure you have install from unknown sources enabled

### Break down into end to end tests

The tests consist of testing the Login, Register UI and Room Database functionality

```
@Test
    fun insertingUserSavesUser() {
        userDao.insert(user)

        val dbUser = userDao.getUser()
        assert(dbUser.value?.firstname == user.firstname)
    }
```
## Documentation

Inside Doc Folder

## Built With

* [Kotlin](https://kotlinlang.org) - Language
* [Lottie](https://airbnb.design/lottie/) - Animation Module
* [Room](https://developer.android.com/topic/libraries/architecture/room) - Local Database
* [Retrofit](https://square.github.io/retrofit/) - HTTPClient for Android
* [Dagger](https://google.github.io/dagger/) - Dependency Injection
* [Moshi](https://github.com/square/moshi) - JSON converter
* [Anko](https://github.com/Kotlin/anko) - Kotlin and Android made more easy
* [JUnit4](https://github.com/junit-team/junit4) - Testing
* [Espresso](https://github.com/googlesamples/android-testing/tree/master/ui/espresso) - E2E Testing
* [CircularImageView](https://github.com/lopspower/CircularImageView) - A Circular ImageView for Android
* [Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper) - Image Cropper for Android
* [Picasso](http://square.github.io/picasso/) - Proper async image loading
* [Dokka](https://github.com/Kotlin/dokka) - Documentation Generator for Kotlin

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Michiel Leunens** - *Initial work* - [LeunensMichiel](https://github.com/LeunensMichiel)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Stackoverflow
* Jari Duyvejonck and Nicolaas Leenknegt for help on the backend side
