package com.example.marvelcharacterinfoapp.providers

import android.util.Log
import com.example.marvelcharacterinfoapp.models.MarvelCharacter
import com.example.marvelcharacterinfoapp.models.MarvelResponse
import com.google.gson.Gson
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import kotlin.math.ceil

//https://developer.marvel.com/documentation/generalinfo
//https://developer.marvel.com/docs#!/public/getCreatorCollection_get_0
//https://www.raymondcamden.com/2014/02/02/Examples-of-the-Marvel-API
class CharacterProvider {

    val publicKey = "6bd6020db387bd61aa2c34164e2b7779"
    val privateKey = ""
    val limit = 4
    var totalPages = 1

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    fun getCharacters(page: Int, name: String) : List<MarvelCharacter> {
        val ts = System.currentTimeMillis().toString()
        val hash = (ts + privateKey + publicKey).md5()
        val offset = (page - 1) * limit

        var url = "http://gateway.marvel.com/v1/public/characters?limit=$limit&offset=$offset&ts=$ts&apikey=$publicKey&hash=$hash"
        if (name != "")
            url += "&name=$name"

        Log.d("LINK", url)
        val json = URL(url).readText()
        Log.d("JSON", json)

        val result = Gson().fromJson(json, MarvelResponse::class.java)
        totalPages = 0
        if (result?.data != null) {
            totalPages = ceil(result.data.total.toFloat() / limit).toInt()
            return result.data.results
        }
        return emptyList()
    }
}