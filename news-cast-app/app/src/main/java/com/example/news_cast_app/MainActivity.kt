package com.example.news_cast_app

import android.graphics.PointF.length
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    var articlePreviews = ArrayList<ArticlePreview>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







        val queue= Volley.newRequestQueue(this)
        val url= "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=google-news-fr"
        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url, null,
            {response ->
                //textView.text="Response: %s".format(response.toString())
                val articlesJSON=response.getJSONArray("articles")
                formatJSONToArticles(articlesJSON)
                //textView.text="Response: %s".format(articlePreviews[5].author)
                var viewManager = LinearLayoutManager(this)
                var viewAdapter=CustomAdapter(articlePreviews)

                var recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter

                }


            },
            { error ->

                Log.d("error", error.message.toString())
            }
        )

        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        queue.add(jsonObjectRequest)



    }

    fun formatJSONToArticles(responseArray: JSONArray) {
        for (i in 0 until responseArray.length()){
            val JSONarticle = responseArray.getJSONObject(i)
            val articlePrev= ArticlePreview( JSONarticle.get("author").toString(),JSONarticle.get("title").toString(), JSONarticle.get("publishedAt").toString())
            articlePreviews.add(articlePrev)
        }
    }
}