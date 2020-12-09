package com.example.news_cast_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class ArticleView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        val buttonBack=findViewById(R.id.buttonBack) as Button
        buttonBack.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val textTitle: TextView = findViewById(R.id.title_view) as TextView
        textTitle.text=intent.getStringExtra("title")

        val textAuthor: TextView = findViewById(R.id.author_view) as TextView
        textAuthor.text=intent.getStringExtra("author")

        val textDate: TextView = findViewById(R.id.date_view) as TextView
        textDate.text=intent.getStringExtra("date")



    }
}