package com.snappy.socialx.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.snappy.socialx.MySingleton
import com.snappy.socialx.model.News
import com.snappy.socialx.ui.adapter.NewsListAdapter
import com.snappy.socialx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: NewsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

    private fun fetchData() {

        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=5453697c048f40db834a52f372b13461"

        val jsonObjectRequest = object :JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    Log.e("No of Array objects","${newsJsonArray.length()}")
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
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