package com.leunesmedia.soundhub.Domain

import java.io.Serializable

//TODO (Moeten nog audiobestand bij, een locatie, een genre)
data class Post(val ID: Int, val titel: String, val imgResourceId: String, val tekst: String) {
}