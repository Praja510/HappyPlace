package com.mprajadinata.happyplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprajadinata.happyplace.adapter.HappyPlaceAdapter
import com.mprajadinata.happyplace.database.DatabaseHandler
import com.mprajadinata.happyplace.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

        getHappyPlacesListFromLocalDB()
    }

    private fun getHappyPlacesListFromLocalDB() {

        val dbHandler = DatabaseHandler(this)
        val getHappyPlacesList = dbHandler.getHappyPlaceList()

        if (getHappyPlacesList.size > 0) {
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records.visibility = View.GONE

            setupHappyPlaceRecyclerView(getHappyPlacesList)
        } else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlaceRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>) {

        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)
        val placesAdapter = HappyPlaceAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter
    }
}