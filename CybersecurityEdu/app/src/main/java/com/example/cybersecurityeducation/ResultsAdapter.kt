package com.example.cybersecurityeducation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultsAdapter(private val questions: ArrayList<ResultData>) :
    RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    inner class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question = itemView.findViewById<TextView>(R.id.tv_Question)
        val userAnser = itemView.findViewById<TextView>(R.id.tv_Answer)
        val correctImg = itemView.findViewById<ImageView>(R.id.iv_correct)
        val wrongImg = itemView.findViewById<ImageView>(R.id.iv_incorrect)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.result_row_item, parent, false)


        return ResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val currentItem = questions[position]

        holder.question.text = currentItem.questionTitle
        holder.userAnser.text = currentItem.userResponse
        if (currentItem.correctOrNot) {
            holder.correctImg.visibility = View.VISIBLE
            holder.wrongImg.visibility = View.INVISIBLE
        } else {
            holder.correctImg.visibility = View.INVISIBLE
            holder.wrongImg.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}

