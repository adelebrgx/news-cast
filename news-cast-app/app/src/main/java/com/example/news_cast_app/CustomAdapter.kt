package com.example.news_cast_app

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataset: ArrayList<ArticlePreview>):

    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val TAG= "ok"
        val txtAuthor: TextView
        val txtTitle: TextView
        val txtDate: TextView
        val button: Button


        init {

            txtAuthor=v.findViewById(R.id.txtAuthor)
            txtTitle=v.findViewById(R.id.txtTitle)
            txtDate=v.findViewById(R.id.txtDate)
            button=v.findViewById(R.id.button)



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_item_preview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.getContext()
        Log.d(TAG, "Element $position set")
        holder.txtAuthor.text=dataset[position].author
        holder.txtTitle.text=dataset[position].title
        holder.txtDate.text=dataset[position].date
        holder.button.setOnClickListener{
            val article= ArticlePreview(dataset[position].author, dataset[position].title, dataset[position].date, dataset[position].url, dataset[position].image,dataset[position].description)

            val intent = Intent(context, ArticleView::class.java)
            intent.putExtra("author",article.author)
            intent.putExtra("title",article.title)
            intent.putExtra("date",article.date)
            intent.putExtra("url",article.url)

            intent.putExtra("image",article.image)
            intent.putExtra("description",article.description)

            context.startActivity(intent)
        }


    }

    override fun getItemCount()=dataset.size

    companion object{
        private val TAG="CustomAdapter"
    }

}