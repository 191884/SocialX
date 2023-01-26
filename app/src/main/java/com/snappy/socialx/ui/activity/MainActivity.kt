package com.snappy.socialx.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snappy.socialx.MySingleton
import com.snappy.socialx.model.News
import com.snappy.socialx.ui.adapter.NewsListAdapter
import com.snappy.socialx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=5453697c048f40db834a52f372b13461"
        fetchData(url)
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter

        binding.searchBar.doAfterTextChanged {
            var q = binding.searchBar.text.toString()
            var url = "https://newsapi.org/v2/everything?q=$q&sortBy=popularity&apiKey=5453697c048f40db834a52f372b13461"
            fetchData(url)
        }

        binding.logOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun fetchData(url: String) {

        val jsonObjectRequest = object :JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("publishedAt")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            }, {
                Toast.makeText(this,"Nothing to Show", Toast.LENGTH_LONG).show()
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

}