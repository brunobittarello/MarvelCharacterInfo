package com.example.marvelcharacterinfoapp.models

class MarvelResponse(
    val data: MarvelData
)

class MarvelData(
    val total: Int,
    val results: List<MarvelCharacter>
)