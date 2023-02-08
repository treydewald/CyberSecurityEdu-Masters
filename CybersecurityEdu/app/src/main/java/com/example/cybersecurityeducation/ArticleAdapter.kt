package com.example.cybersecurityeducation

import android.content.Intent
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter(private val articles : ArrayList<ArticleData>):RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {



    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val articleTitle = itemView.findViewById<TextView>(R.id.tv_article_title)
        val articleURL = itemView.findViewById<TextView>(R.id.tv_article_url)

        init {
            itemView.setOnClickListener {
                val selectedItem = adapterPosition
                val webIntent = Intent(itemView.context,WebViewActivity::class.java)
                webIntent.putExtra("URL_WEB_VIEW","${articles[selectedItem].url}")
                itemView.context.startActivity(webIntent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        // create the new views, inflate the recycler  view by adding row items
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_row_item,parent,false)


        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = articles[position]

        holder.articleTitle.text = currentItem.title
        holder.articleTitle.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(41))
        holder.articleTitle.setOnClickListener {
        }

    }

    override fun getItemCount(): Int {
        return articles.size
    }
}