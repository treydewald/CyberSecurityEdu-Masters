package com.example.cybersecurityeducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class TipsActivity : AppCompatActivity() {

    private val TAG = "TipsActivity"

    val titleData = mutableListOf<String>(
        "Share with care – What you post can last a lifetime",
        "Personal information is like money. Value it. Protect it"
    )
    val bodyData = mutableListOf<String>(
        "Information about your kids, such as the games they like to play and what they search for online, has value – just like money. Talk to your kids about the value of their information and how to be selective with the information they provide to apps and websites."
    )

    val titleArray = mutableListOf<String>(
        "2 Factor Authentication",
        "Phishing detection",
        "Password Managers",
        "Using Strong Passwords",
        "Software Updates",
        "Privacy: Browsing the Internet"
    )

    lateinit var recyclerView: RecyclerView
    lateinit var tipsAdapter: TipsAdapter
    private lateinit var titleText: TextView

    lateinit var firebaseDB: FirebaseDatabase
    lateinit var TipsRef: DatabaseReference

    lateinit var userTYPE : String
    lateinit var viewModel: UserTypeViewModel
    lateinit var cate : String

    //val _tips = mutableListOf("Tip1")
    val tipsArray = ArrayList<TipsData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        supportActionBar?.hide()

        // import user type, check first
        if(intent.hasExtra("USER_TYPE_TIPS")){
            userTYPE = intent.getStringExtra("USER_TYPE_TIPS").toString()
        }else{
            userTYPE = "no user type found"
        }
        Log.d(TAG, "user type from intent: $userTYPE")

        //set title
        titleText = findViewById(R.id.tv_TipsActivityTitleCat)
        when (userTYPE) {
            "EASY" -> {
                cate = "A"
                titleText.text = "${titleArray[5]}\n Security Tips & Recommendations" }
            "MEDIUM" -> {
                cate = "F"
                titleText.text = "${titleArray[1]}\nSecurity Tips & Recommendations"  }
            else -> {
                cate = "E"
                titleText.text = "${titleArray[0]}\nSecurity Tips & Recommendations" }
        }


        recyclerView = findViewById(R.id.tipsRecyclerView)
        // to go horizontal scrollable list
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        // recyclerView.addItemDecoration(dividerItemDecoration)


        //--------------- READ DATA ---------------------------------------------------------------

        // set up realtime database
        firebaseDB = FirebaseDatabase.getInstance()
        TipsRef = firebaseDB.getReference("Tips")

        TipsRef
            .orderByChild("cat")
            .equalTo(cate)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val children = dataSnapshot.children
                    children.forEach { it ->
                        val data = it.getValue(TipsData::class.java)
                        Log.d(TAG, "Data: ${data?.title}, ${data?.body}")
                        tipsArray.add(data!!)
                    }
                }

                Log.d(TAG, "Data: ${tipsArray.size}")
                Log.d(TAG, "Category1: ${tipsArray[0].cat.toString()}")
                //Log.d(TAG, "Category2: ${tipsArray[11].cat.toString()}")
                tipsAdapter = TipsAdapter(tipsArray)
                //tipsAdapter.notifyDataSetChanged()
                recyclerView.adapter = tipsAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


        //--------------- Initial Message for User------------------------------------------------

        showDialog("Pro Tip", "Swipe Down to see more tips!")
        //tipsAdapter.notifyDataSetChanged()




    }

    fun toMainActivity(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDialog(title: String, message: String) {
        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_baseline_check_circle_24)
        builder.setPositiveButton("OK") { dialog, which ->
            // code to run when YES is pressed
        }
        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }


}