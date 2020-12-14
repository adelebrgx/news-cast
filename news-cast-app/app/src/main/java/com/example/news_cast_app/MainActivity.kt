package com.example.news_cast_app

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    /* List of article previews which will be displayed in the main activity */
    var articlePreviews = ArrayList<ArticlePreview>()

    /* Sources retrieved from the api call */
    var sources=ArrayList<Source>()

    /* Future dropdown */
    lateinit var mySpinner: Spinner
    lateinit var spinArrayAdapter: SpinnerAdapter

    /* Source being displayed */
    var sourceActual="google-news-fr"
    var displayed="Google News (France)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this

        /* Creating a popup for loading and handling retry */
        val alert = AlertDialog.Builder(this).create()
        alert.setMessage("The articles are charging ... \n Please wait!")
        alert.show()

        val textView= findViewById<TextView>(R.id.txtTitle)
        val queue= Volley.newRequestQueue(this)

        val spinner=findViewById(R.id.mySpinner) as Spinner


        /* First request to fetch news sources */
        val urlSources= "https://newsapi.org/v2/sources?apiKey=86e41137f6db4ed2a15179544239ee12&language=fr"

        val jsonObjectRequestSources=object: JsonObjectRequest(Request.Method.GET, urlSources, null,
            {response ->

                /* Formatting the sources to be displayed in dropdown */
                val sourcesJSON=response.getJSONArray("sources")
                var list = formatJSONToSources(sourcesJSON)
                /* Creating a dropdown menu to offer the opportunity to choose another source */
                val spinadapter:ArrayAdapter<String> = object: ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        listOf(displayed,
                                sources[0].name,
                                sources[1].name,
                                sources[2].name,
                                sources[3].name,
                                sources[4].name
                        )
                ){
                    override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                    ): View {
                        val view:TextView = super.getDropDownView(
                                position,
                                convertView,
                                parent
                        ) as TextView

                        view.setTypeface(view.typeface, Typeface.BOLD)
                        if (position == spinner.selectedItemPosition){
                            view.background = ColorDrawable(Color.parseColor("#F3C6D5"))
                            view.setTextColor(Color.parseColor("#E91E63"))
                            view.setTypeface(null, Typeface.ITALIC);
                        }
                        return view
                    }
                }
                spinner.adapter = spinadapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                    ) {
                        /* Changing source except for the first element which indicates the current source */
                        if (position!=0){
                            intent.putExtra("url","https://newsapi.org/v2/everything?apiKey=86e41137f6db4ed2a15179544239ee12&language=fr&sources="+sources[position-1].id )
                            intent.putExtra("source",sources[position-1].id )
                            intent.putExtra("name",sources[position-1].name )
                            finish()
                            startActivity(intent)
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                /* dismissing the loading bar */
                alert.dismiss()

            },
            { error ->
                alert.dismiss()

                /* Handling errors when fetching */
                val dialogBuilder = AlertDialog.Builder(this)

                dialogBuilder.setMessage("Data couldn't be fetched. Do you want to refresh ?")
                        .setCancelable(false)
                        /* Handling retry to try fetching the data again */
                        .setPositiveButton("Retry", DialogInterface.OnClickListener {
                            dialog, id -> run {
                            /* Recreating the activity to try fetching the data again */
                            this.recreate();
                        }
                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                        })
                val alertError = dialogBuilder.create()
                alertError.setTitle("An error has occurred")
                alertError.show()
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

        /* Making a request to display articles according to source */

        /*When the app is instantiating */
        var urlArticles="https://newsapi.org/v2/everything?apiKey=86e41137f6db4ed2a15179544239ee12&language=fr&sources=google-news-fr"
        if(getIntent().getStringExtra("url").toString()=="null"){
            Log.d("url", "activité principale")
        }
        /* When a dropdown source has been chosen */
        else{
            urlArticles=getIntent().getStringExtra("url").toString()
            sourceActual=getIntent().getStringExtra("source").toString()
            displayed=getIntent().getStringExtra("name").toString()
        }

        val jsonObjectRequestArticles = object: JsonObjectRequest(Request.Method.GET, urlArticles, null,
            {response ->

                val articlesJSON=response.getJSONArray("articles")

                /* Formatting the data to adequate format */
                formatJSONToArticles(articlesJSON)

                var viewManager = LinearLayoutManager(this)
                var viewAdapter=CustomAdapter(articlePreviews)

                /* Displaying in a recycler view */
                var recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
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

    /* Function to format JSON data ro sources format */
    fun formatJSONToSources(responseArray: JSONArray){
        for (i in 0 until responseArray.length()){
            val JSONsource = responseArray.getJSONObject(i)
            val source= Source( JSONsource.get("id").toString(),JSONsource.get("name").toString())
            sources.add(source)
        }
        }

    /* Function to format JSON data to article format */
    fun formatJSONToArticles(responseArray: JSONArray) {
        for (i in 0 until responseArray.length()){
            val JSONarticle = responseArray.getJSONObject(i)
            val articlePrev= ArticlePreview( JSONarticle.get("author").toString(),JSONarticle.get("title").toString(), JSONarticle.get("publishedAt").toString(), JSONarticle.get("url").toString(), JSONarticle.get("urlToImage").toString(), JSONarticle.get("description").toString(),sourceActual, displayed)
            articlePreviews.add(articlePrev)
        }
    }






}