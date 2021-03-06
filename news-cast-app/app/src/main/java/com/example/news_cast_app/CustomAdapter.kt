package com.example.news_cast_app

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

/* Adapter to display article in Recycler View */
class CustomAdapter(private val dataset: ArrayList<ArticlePreview>):

    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val TAG= "ok"
        val txtAuthor: TextView
        val txtTitle: TextView
        val txtDate: TextView
        /* Button to see details*/
        val button: Button
        val image: ImageView


        init {

            txtAuthor=v.findViewById(R.id.txtAuthor)
            txtTitle=v.findViewById(R.id.txtTitle)
            txtDate=v.findViewById(R.id.txtDate)
            button=v.findViewById(R.id.button)
            image=v.findViewById(R.id.imageMin)


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

        val urlstatic="https://cdn3.iconfinder.com/data/icons/design-n-code/100/272127c4-8d19-4bd3-bd22-2b75ce94ccb4-512.png"
        Picasso.get().load(urlstatic).into(holder.image)

        if (dataset[position].image=="null"){
            Log.d("debug", "pas d'image")

        }
        else {

            val urlImage=dataset[position].image
            Picasso.get().load(urlImage).into(holder.image)
        }
        /* Creating a conditionnal layout 1/2 */
        holder.image.visibility=View.INVISIBLE
        if(position%2 ==0){Log.d("debug", position.toString())
                holder.image.visibility=View.VISIBLE
           }
        holder.txtTitle.text=dataset[position].title
        holder.txtDate.text=dataset[position].date
        Log.d("source",dataset[position].source)
        var source=dataset[position].source
        var name=dataset[position].name
        holder.button.setOnClickListener{
            val article= ArticlePreview(dataset[position].author, dataset[position].title, dataset[position].date, dataset[position].url, dataset[position].image,dataset[position].description, dataset[position].source, dataset[position].name)

            val intent = Intent(context, ArticleView::class.java)
            intent.putExtra("author",article.author)
            intent.putExtra("title",article.title)
            intent.putExtra("date",article.date)
            intent.putExtra("url",article.url)

            intent.putExtra("image",article.image)
            intent.putExtra("description",article.description)
            intent.putExtra("source", source)
            intent.putExtra("name", name)

            context.startActivity(intent)
        }


    }

    override fun getItemCount()=dataset.size

    companion object{
        private val TAG="CustomAdapter"
    }

}