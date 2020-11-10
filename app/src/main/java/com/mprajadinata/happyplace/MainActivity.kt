package com.mprajadinata.happyplace

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mprajadinata.happyplace.activity.AddHappyPlaceActivity
import com.mprajadinata.happyplace.activity.HappyDetailActivity
import com.mprajadinata.happyplace.adapter.HappyPlaceAdapter
import com.mprajadinata.happyplace.callback.SwipeToEditCallback
import com.mprajadinata.happyplace.database.DatabaseHandler
import com.mprajadinata.happyplace.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
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

        placesAdapter.setOnClickListener(object : HappyPlaceAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAIL, model)

                startActivity(intent)

            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_happy_places_list.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }

        }
    }

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        const val EXTRA_PLACE_DETAIL = "extra_place_detail"
    }


}