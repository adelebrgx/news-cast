package com.example.news_cast_app

/* Class to model sources, could be eliminated in a future refactoring */
class Source {
    var id: String = ""
    var name: String = ""

    constructor(){}

    constructor(id: String, name: String){
        this.id=id
        this.name=name

    }
}