package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class ArticleActivity : AppCompatActivity() {

    private val TAG = "ArticleActivity"

    val titleArray = mutableListOf<String>(
        "2 Factor Authentication",
        "Phishing",
        "Password Managers",
        "Software Updates"
    )

    lateinit var recyclerViewArticle: RecyclerView
    lateinit var articleAdapter: ArticleAdapter

    private lateinit var titleTextArticle: TextView

    lateinit var firebaseDB: FirebaseDatabase
    lateinit var articleRef: DatabaseReference

    lateinit var userTYPE: String
    lateinit var activityType: String
    lateinit var cate: String

    private val articleArray = ArrayList<ArticleData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        supportActionBar?.hide()

        //val url2 = "https://www.youtube.com/"
        //val dummyData = ArticleData("A", "User Privacy", url2)

        //articleArray.add(dummyData)

        // import user type, check first
        if (intent.hasExtra("USER_TYPE_ARTICLE")) {
            userTYPE = intent.getStringExtra("USER_TYPE_ARTICLE").toString()
        } else {
            userTYPE = "user type not found"
        }

        // import activity type, check first, set title
        titleTextArticle = findViewById(R.id.tv_TipsActivityTitleCat)
        if (intent.hasExtra("2FA")) {
            activityType = intent.getStringExtra("2FA").toString()
            titleTextArticle.text = "${titleArray[0]} Articles"
        } else if (intent.hasExtra("PHISHING")) {
            activityType = intent.getStringExtra("PHISHING").toString()
            titleTextArticle.text = "${titleArray[1]} Articles"
        } else if (intent.hasExtra("SOFTWAREUPDATES")) {
            activityType = intent.getStringExtra("SOFTWAREUPDATES").toString()
            titleTextArticle.text = "${titleArray[3]} Articles"
        } else if (intent.hasExtra("PASSWORDMANAGERS")) {
            activityType = intent.getStringExtra("PASSWORDMANAGERS").toString()
            titleTextArticle.text = "${titleArray[2]} Articles"
        } else {
            activityType = "activity type not found"
        }

        Log.d(TAG, "user type from intent: $userTYPE")
        Log.d(TAG, "activity type from intent: $activityType")


        //set category
        if (userTYPE == "EASY" && activityType == "2FA") {
            cate = "A1"
        } else if (userTYPE == "MEDIUM" && activityType == "2FA") {
            cate = "A2"
        } else if (userTYPE == "HARD" && activityType == "2FA") {
            cate = "A3"
        } else if (userTYPE == "EASY" && activityType == "PHISHING") {
            cate = "B1"
        } else if (userTYPE == "MEDIUM" && activityType == "PHISHING") {
            cate = "B2"
        } else if (userTYPE == "HARD" && activityType == "PHISHING") {
            cate = "B3"
        } else if (userTYPE == "EASY" && activityType == "PASSWORDMANAGERS") {
            cate = "C1"
        } else if (userTYPE == "MEDIUM" && activityType == "PASSWORDMANAGERS") {
            cate = "C2"
        } else if (userTYPE == "HARD" && activityType == "PASSWORDMANAGERS") {
            cate = "C3"
        }else if (userTYPE == "EASY" && activityType == "SOFTWAREUPDATES") {
            cate = "D1"
        } else if (userTYPE == "MEDIUM" && activityType == "SOFTWAREUPDATES") {
            cate = "D2"
        } else if (userTYPE == "HARD" && activityType == "SOFTWAREUPDATES") {
            cate = "D3"
        }else {
            val sample = 1
        }

        recyclerViewArticle = findViewById(R.id.articleRecyclerView)
        // to go horizontal scrollable list
        recyclerViewArticle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        // recyclerView.addItemDecoration(dividerItemDecoration)

        getData(cate)

    }

    private fun getData(category: String) {

        //--------------- READ DATA ---------------------------------------------------------------
        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        articleRef = firebaseDB.getReference("Article")
        Log.d(TAG, "category from function: $category")

        articleRef
            .orderByChild("cat")
            .equalTo(category)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        val children = dataSnapshot.children
                        children.forEach { it ->
                            val data = it.getValue(ArticleData::class.java)
                            Log.d(TAG, "Data: ${data?.title}, ${data?.url}")
                            articleArray.add(data!!)
                        }
                    }

                    Log.d(TAG, "Data: ${articleArray.size}")
                    Log.d(TAG, "Category1: ${articleArray[0].cat.toString()}")
                    articleAdapter = ArticleAdapter(articleArray)
                    //tipsAdapter.notifyDataSetChanged()
                    recyclerViewArticle.adapter = articleAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

    }

    fun toMainActivityArticle(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}