package com.leunesmedia.soundhub.Domain


//TODO (Moeten nog audiobestand bij)
data class Post(val ID: Int, val titel: String, val imgResourceId: String, val tekst: String, val location: String, val genres: Array<String>) {
}