package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.*
import com.google.firebase.database.*

class YoutubeActivity : YouTubeBaseActivity(), YoutubeAdapter.OnRecyclerInfo {

    private val TAG = "YouTubeActivity"

    lateinit var recyclerViewYT: RecyclerView
    lateinit var textInput: TextView
    lateinit var videoAdapter: YoutubeAdapter

    lateinit var firebaseDB: FirebaseDatabase
    lateinit var videoRef: DatabaseReference

    private lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var mOnInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var mYouTubePlayer: YouTubePlayer
    //lateinit var video1: String
    var video1 = ""

    lateinit var userTYPE: String
    lateinit var activityType: String
    lateinit var cate: String

    private val dataArray = ArrayList<YouTubeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        //supportActionBar?.hide()

        // import user type, check first
        // import user type, check first
        if (intent.hasExtra("USER_TYPE_VIDEO")) {
            userTYPE = intent.getStringExtra("USER_TYPE_VIDEO").toString()
        } else {
            userTYPE = "user type not found"
        }

        // import activity type, check first,
        if (intent.hasExtra("2FA")) {
            activityType = intent.getStringExtra("2FA").toString()
        } else if (intent.hasExtra("PHISHING")) {
            activityType = intent.getStringExtra("PHISHING").toString()
        } else if (intent.hasExtra("SOFTWAREUPDATES")) {
            activityType = intent.getStringExtra("SOFTWAREUPDATES").toString()
        } else if (intent.hasExtra("PASSWORDMANAGERS")) {
            activityType = intent.getStringExtra("PASSWORDMANAGERS").toString()
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
        } else if (userTYPE == "EASY" && activityType == "SOFTWAREUPDATES") {
            cate = "D1"
        } else if (userTYPE == "MEDIUM" && activityType == "SOFTWAREUPDATES") {
            cate = "D2"
        } else if (userTYPE == "HARD" && activityType == "SOFTWAREUPDATES") {
            cate = "D3"
        } else {
            val sample = 1
        }

        recyclerViewYT = findViewById(R.id.recyclerViewYoutube)
        recyclerViewYT.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getData(cate)


        youTubePlayerSetup()

    }


    private fun getData(param: String) {
        //--------------- READ DATA ---------------------------------------------------------------

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        videoRef = firebaseDB.getReference("YouTubeData")

        videoRef.orderByChild("cat").equalTo(param)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        val children = dataSnapshot.children
                        children.forEach { it ->
                            val data = it.getValue(YouTubeData::class.java)
                            Log.d(TAG, "Data: ${data?.title}, ${data?.url}")
                            dataArray.add(data!!)
                        }
                    }
                    Log.d(TAG, "Data: ${dataArray.size}")
                    Log.d(TAG, "Category1: ${dataArray[0].cat.toString()}")
                    video1 = parserYouTubeUrl(dataArray[0].url.toString())
                    videoAdapter = YoutubeAdapter(this@YoutubeActivity, dataArray)
                    recyclerViewYT.adapter = videoAdapter

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
    }


    private fun youTubePlayerSetup() {

        youtubePlayerView = findViewById(R.id.youtubePlayer)
        val api = getString(R.string.GOOGLE_API_KEY)

        mOnInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                mYouTubePlayer = player!!
                mYouTubePlayer.cueVideo(video1)

                player.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
                    override fun onPlaying() {}

                    override fun onPaused() {}

                    override fun onStopped() {}

                    override fun onBuffering(p0: Boolean) {}

                    override fun onSeekTo(p0: Int) {}

                })

                player.setPlayerStateChangeListener(object :
                    YouTubePlayer.PlayerStateChangeListener {
                    override fun onLoading() {}

                    override fun onLoaded(p0: String?) {}

                    override fun onAdStarted() {}

                    override fun onVideoStarted() {}

                    override fun onVideoEnded() {}

                    override fun onError(p0: YouTubePlayer.ErrorReason?) {}
                })

            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
            }
        }

        youtubePlayerView.initialize(api, mOnInitializedListener)


    }


    override fun recyclerInfo(param: String) {
        Log.d(TAG, "recyclerInfo: $param")
        mYouTubePlayer.cueVideo(param)
    }

    private fun parserYouTubeUrl(param: String): String {
        val separated: List<String> = param.split("=")
        return separated[1]
    }

    fun toMainActivityYouTube(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}