package com.example.news_cast_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ArticleView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        var source=intent.getStringExtra("source")
        var name=intent.getStringExtra("name")
        val buttonBack=findViewById(R.id.buttonBack) as Button
        buttonBack.setOnClickListener{

            val newIntent = Intent(this, MainActivity::class.java)
            Log.d("retour", "https://newsapi.org/v2/everything?apiKey=86e41137f6db4ed2a15179544239ee12&language=fr&sources="+source)
            newIntent.putExtra("url","https://newsapi.org/v2/everything?apiKey=86e41137f6db4ed2a15179544239ee12&language=fr&sources="+source)
            newIntent.putExtra("source",source)
            newIntent.putExtra("name",name)
            startActivity(newIntent)
        }

        val textTitle: TextView = findViewById(R.id.title_view) as TextView
        textTitle.text=intent.getStringExtra("title")

        val textAuthor: TextView = findViewById(R.id.author_view) as TextView
        textAuthor.text=intent.getStringExtra("author")

        val textDate: TextView = findViewById(R.id.date_view) as TextView
        textDate.text=intent.getStringExtra("date")

        val textDescription: TextView = findViewById(R.id.description_view) as TextView
        textDescription.text=intent.getStringExtra("description")

        val url=intent.getStringExtra("url")
        val myImageView= findViewById<ImageView>(R.id.image)
        val urlstatic="https://cdn3.iconfinder.com/data/icons/design-n-code/100/272127c4-8d19-4bd3-bd22-2b75ce94ccb4-512.png"

        Picasso.get().load(urlstatic).into(myImageView)



        if (intent.getStringExtra("image")=="null"){
            Log.d("hey", "je suis passé ici")

        }
        else {
            Log.d("hey", "je suis passé là")
            val urlImage=intent.getStringExtra("image").toString()
            Picasso.get().load(urlImage).into(myImageView)
        }







        val buttonSee=findViewById(R.id.buttonRead) as Button
        buttonSee.setOnClickListener{

            val intent = Intent(this, WebviewArticle::class.java)
            intent.putExtra("url",url.toString())
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            startActivity(intent)
        }

    }
}