package com.example.covidtrackerapp

import Data.RecyclerViewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    companion object{
        var sr_no =1
        lateinit var adapter:RecyclerViewAdapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val intent = intent
        val user=intent.getStringExtra("user")
        var list:ArrayList<String> = ArrayList<String>()
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val searchView = findViewById<SearchView>(R.id.searchview)
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                // Override onQueryTextSubmit method
                // which is call
                // when submitquery is searched
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 != null) {
                        var list1: ArrayList<String> = ArrayList<String>()
                        if(p0.isNotEmpty()) {
                            for(i in list)
                            {
                                if(i.startsWith(p0))
                                    list1.add(i)
                            }
                            updateUI(list1)
                        }
                    }
                    else
                    {
                        updateUI(list)
                    }
                    return true
                }

                // This method is overridden to filter
                // the adapter according to a search query
                // when the user is typing search
            })
        searchView.setOnSearchClickListener {  }
//        if (user != null) {
//            Log.e("TAG",user)
//        }
       MainScope().async() {
           list = getData(url,list)
           Log.e("list1",list.toString())
       }
        updateUI(list)
        Log.e("list",list.toString())
        val logout= findViewById<MaterialButton>(R.id.logout)
        logout.setOnClickListener({
            logoutUser()
        })


    }
    fun updateUI(list:ArrayList<String>)
    {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val layourManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layourManager
        recyclerView.adapter = RecyclerViewAdapter(list)

    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()

    }

    fun getData(url:String,list:ArrayList<String>):ArrayList<String>
    {

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
               val obj:JSONObject = response.getJSONObject("data")
                val jsonArray:JSONArray = obj.getJSONArray("regional")
                //Log.e("data",jsonArray.toString())
                for (i in 0.. jsonArray.length()-1) {
                    //val tempObj :JSONObject = JSONObject(jsonArray.getString(i))
                     val obj1 :JSONObject= jsonArray[i] as JSONObject
                   val state =  obj1.get("loc") as String
                   val count = obj1.getString("confirmedCasesIndian")
                    val item = sr_no++.toString()+"," +state + "," +count
                    list.add(item)
                }
                Log.e("UI",list.toString())
                updateUI(list)
            },
            { error ->
                Log.d("vol",error.toString())
            }
        )
        queue.add(jsonObjectRequest)
        return list
    }
}