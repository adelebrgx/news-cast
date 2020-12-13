package com.example.news_cast_app

class ArticlePreview{
    var author: String = ""
    var title: String = ""
    var date: String = ""
    var url=""
    var image= ""
    var description=""
    var source=""

    constructor(){}

    constructor(author: String, title: String, date: String, url: String, image: String, description:String, source:String){
        this.author=author
        this.title=title
        this.date=date
        this.url=url
        this.image=image
        this.description=description
        this.source=source
    }




}