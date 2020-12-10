package com.example.news_cast_app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    var articlePreviews = ArrayList<ArticlePreview>()
    var sources=ArrayList<Source>()
    lateinit var mySpinner: Spinner
    lateinit var spinArrayAdapter: SpinnerAdapter

    var sources_dropdown = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alert = AlertDialog.Builder(this).create()
        alert.setMessage("The articles are charging ... \n Please wait!")
        alert.show()

        val queue= Volley.newRequestQueue(this)

        mySpinner=findViewById(R.id.mySpinner)

        spinArrayAdapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, sources_dropdown)
        mySpinner.setAdapter(spinArrayAdapter)

        val urlSources= "https://newsapi.org/v2/sources?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr"

        val jsonObjectRequestSources=object: JsonObjectRequest(Request.Method.GET, urlSources, null,
            {response ->


                val sourcesJSON=response.getJSONArray("sources")
                Log.d("hey", response.getJSONArray("sources").toString())
                formatJSONToSources(sourcesJSON)
                alert.dismiss()

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
        queue.add(jsonObjectRequestSources)






        val urlArticles= "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=google-news-fr"
        val jsonObjectRequestArticles = object: JsonObjectRequest(Request.Method.GET, urlArticles, null,
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

        queue.add(jsonObjectRequestArticles)



    }

    fun formatJSONToSources(responseArray: JSONArray){
        for (i in 0 until responseArray.length()){
            val JSONsource = responseArray.getJSONObject(i)
            Log.d("hey", JSONsource.toString())
            val source= Source( JSONsource.get("id").toString(),JSONsource.get("name").toString())
            sources.add(source)
            sources_dropdown.add(JSONsource.get("name").toString())
        }
    }
    fun formatJSONToArticles(responseArray: JSONArray) {
        for (i in 0 until responseArray.length()){
            val JSONarticle = responseArray.getJSONObject(i)
            val articlePrev= ArticlePreview( JSONarticle.get("author").toString(),JSONarticle.get("title").toString(), JSONarticle.get("publishedAt").toString(), JSONarticle.get("url").toString(), JSONarticle.get("urlToImage").toString(), JSONarticle.get("description").toString())
            articlePreviews.add(articlePrev)
        }
    }


}