package com.example.cybersecurityeducation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.ArrayList

class TipsAdapter(private val tips : ArrayList<TipsData>) : RecyclerView.Adapter<TipsAdapter.TipsViewHolder>() {

    val TAG = "TipsAdapter"

    inner class TipsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById<TextView>(R.id.tv_row_item_title)
        val body: TextView = itemView.findViewById<TextView>(R.id.tv_row_item_body)
        val img: ImageView = itemView.findViewById<ImageView>(R.id.tv_row_item_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_items_tips,parent,false)


        return TipsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        val currentItem = tips[position]

        holder.title.text = currentItem.title.toString()
        holder.body.text = currentItem.body.toString()
        holder.img.setImageResource(R.drawable.ic_baseline_image_search_24)
        //Picasso.get().load(R.drawable.ic_baseline_image_search_24).into(holder.img)

        if (currentItem.imgURL!!.isNotEmpty()) {
            Picasso.get().load(currentItem.imgURL)
                .placeholder(R.drawable.ic_baseline_image_search_24)
                .fit().centerCrop()
                .into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.ic_baseline_image_search_24)
        }

    }

    override fun getItemCount(): Int {
        return tips.size
    }

}