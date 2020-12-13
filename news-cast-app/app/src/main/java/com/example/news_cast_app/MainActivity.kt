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

    var articlePreviews = ArrayList<ArticlePreview>()
    var sources=ArrayList<Source>()
    lateinit var mySpinner: Spinner
    lateinit var spinArrayAdapter: SpinnerAdapter
  



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this

        val alert = AlertDialog.Builder(this).create()
        alert.setMessage("The articles are charging ... \n Please wait!")
        alert.show()

        val textView= findViewById<TextView>(R.id.txtTitle)
        val queue= Volley.newRequestQueue(this)

        val spinner=findViewById(R.id.mySpinner) as Spinner


        val urlSources= "https://newsapi.org/v2/sources?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr"

        val jsonObjectRequestSources=object: JsonObjectRequest(Request.Method.GET, urlSources, null,
            {response ->


                val sourcesJSON=response.getJSONArray("sources")
                Log.d("hey", response.getJSONArray("sources").toString())
                var list = formatJSONToSources(sourcesJSON)
                val spinadapter:ArrayAdapter<String> = object: ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        listOf("",
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
                        // set item text bold
                        view.setTypeface(view.typeface, Typeface.BOLD)

                        // set selected item style
                        if (position == spinner.selectedItemPosition){
                            view.background = ColorDrawable(Color.parseColor("#FAEBD7"))
                            view.setTextColor(Color.parseColor("#008000"))
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
                        Log.d("url", "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources="+sources[position].id)
                        intent.putExtra("url","https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources="+sources[position].id )
                        Log.d("url","chosen")
                        if (position!=0){
                            finish()
                            startActivity(intent)
                        }


                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
                alert.dismiss()

            },
            { error ->

                alert.dismiss()
                val dialogBuilder = AlertDialog.Builder(this)

                dialogBuilder.setMessage("Data couldn't be fetched. Do you want to refresh ?")
                        .setCancelable(false)
                        .setPositiveButton("Retry", DialogInterface.OnClickListener {
                            dialog, id -> run {
                            Log.d("debug","retry")
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



        var urlArticles="https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=google-news-fr"
        Log.d("url", getIntent().getStringExtra("url").toString())
        if(getIntent().getStringExtra("url").toString()=="null"){
            Log.d("url", "activité principale")
        }
        else{
            Log.d("url", "dropdown source choisie")
            urlArticles=getIntent().getStringExtra("url").toString()
        }



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