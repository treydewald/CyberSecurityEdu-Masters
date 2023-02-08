package com.example.cybersecurityeducation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class YoutubeAdapter(val infoToSend : OnRecyclerInfo, private val videoList: ArrayList<YouTubeData>) :
    RecyclerView.Adapter<YoutubeAdapter.YouViewHolder>() {

    private val TAG = "YouTubeAdapter"

    inner class YouViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val youtubeTitle: TextView = itemView.findViewById<TextView>(R.id.tv_YT_title)
        val youtubeThumbnail: ImageView = itemView.findViewById<ImageView>(R.id.iv_YT_thumbnail)

        init {
            itemView.setOnClickListener { v ->
                val selectedItem = adapterPosition
                val bundle = Bundle()

                val myMessage = videoList[selectedItem].url
                val videoID = parserYouTubeUrl(myMessage!!)

                infoToSend.recyclerInfo(videoID)
                Log.d(TAG, " video id: $videoID")

                /**
                bundle.putString("VIDEO_ID", "sample text")
                Log.d(TAG, " video id: $videoID")

                val videoFragment = VideoFragment()
                videoFragment.arguments = bundle

                val activity: AppCompatActivity = v?.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                .replace(R.id.YoutubeVideoContainer, VideoFragment())
                .addToBackStack(null).commit()
                 */


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouViewHolder {
        // create the new views, inflate the recycler  view by adding row items
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.youtube_row_item, parent, false)


        return YouViewHolder(view)
    }

    override fun onBindViewHolder(holder: YouViewHolder, position: Int) {
        val currentItem = videoList[position]




        holder.youtubeTitle.text = currentItem.title

        val imgThumbnail = imageUrlBuilder(parserYouTubeUrl(currentItem.url!!))
        Log.d(TAG, " img thumbnail: $imgThumbnail")
        /**
        if (currentItem.url.isNullOrEmpty()) {
            Picasso.get().load(imgThumbnail)
                .placeholder(R.drawable.ic_youtubev2)
                .fit().centerCrop()
                .into(holder.youtubeThumbnail)
        } else {
            holder.youtubeThumbnail.setImageResource(R.drawable.ic_youtubev2)
        }
         */
        Picasso.get().load(imgThumbnail).placeholder(R.drawable.ic_youtubev2)
            .fit().centerCrop().into(holder.youtubeThumbnail)


    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    private fun parserYouTubeUrl(param: String): String {
        val separated: List<String> = param.split("=")
        return separated[1]
    }

    private fun imageUrlBuilder(param: String): String {
        return "https://img.youtube.com/vi/${param}/1.jpg"
    }

    interface OnRecyclerInfo{
        fun recyclerInfo(param : String)
    }
}