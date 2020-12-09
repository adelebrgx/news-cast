package com.example.news_cast_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataset: ArrayList<ArticlePreview>):

    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val TAG= "ok"
        val txtAuthor: TextView
        val txtTitle: TextView
        val txtDate: TextView

        init {
            v.setOnClickListener{ Log.d(TAG ,"Element $adapterPosition clicked")}
            txtAuthor=v.findViewById(R.id.txtAuthor)
            txtTitle=v.findViewById(R.id.txtTitle)
            txtDate=v.findViewById(R.id.txtDate)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.list_item_preview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set")
        holder.txtAuthor.text=dataset[position].author
        holder.txtTitle.text=dataset[position].title
        holder.txtDate.text=dataset[position].date

    }

    override fun getItemCount()=dataset.size

    companion object{
        private val TAG="CustomAdapter"
    }

}