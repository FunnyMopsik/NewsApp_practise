package com.shpachinsky.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.Transliterator.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.FieldPosition


private  lateinit var dbref: DatabaseReference
private lateinit var newsRecyclerView: RecyclerView
private  lateinit var newsArrayList: ArrayList<News>
class NewslistActivity : AppCompatActivity() {




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newslist)

        newsRecyclerView = findViewById(R.id.newsList)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.setHasFixedSize(true)

        newsArrayList = arrayListOf<News>()
        getNewsData()
    }

    private fun getNewsData() {
        dbref = FirebaseDatabase.getInstance().getReference("news")
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(newsSnapshot in snapshot.children){
                        val news = newsSnapshot.getValue(News::class.java)
                        newsArrayList.add(news!!)
                    }

                    var adapter = MyAdapter(newsArrayList)
                    newsRecyclerView.adapter=adapter
                    adapter.setOnItemClickListener(object:MyAdapter.onItemClickListener{
                        @SuppressLint("SuspiciousIndentation")
                        override fun onItemClick(position: Int) {

                            var array = newsArrayList[position].toString()
                            var url:String = array.substring(array.indexOf("link="))
                            url =url.dropLast(3)
                            url = url.drop(5)
                            Log.i("enter to fun", url)

                            Log.i("activity is changet","true")

                        val intent = Intent(this@NewslistActivity,news_desc_web::class.java)
                            intent.putExtra("url",url)
                            startActivity(intent)



                        }
                    })
                }




            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}