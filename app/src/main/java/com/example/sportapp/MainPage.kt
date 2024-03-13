package com.example.sportapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.Adapters.APIRequests
import com.example.sportapp.Adapters.AdapterTrainings
import com.example.sportapp.Model.Category
import com.example.sportapp.Model.Training
import com.example.sportapp.databinding.ActivityMainPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainPage : AppCompatActivity(), AdapterTrainings.Listener {
    private lateinit var binding: ActivityMainPageBinding
    private val URL_API = "https://iis.ngknn.ru/ngknn/МамшеваЮС/6/"
    private val adapterTrainings = AdapterTrainings(this)
    private var listItems: List<Training> = ArrayList<Training>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataCategory()
        getDataTrainings()
    }
    private fun getDataCategory() {
        val api = Retrofit.Builder()
            .baseUrl(URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequests::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getCurrentCategory(1).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    runOnUiThread {initCategory(data)}
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, e.toString())
                }
            }
        }
    }
    private fun getDataTrainings() {
        val api = Retrofit.Builder()
            .baseUrl(URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequests::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getAllTrainingsForCategory(1).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    runOnUiThread {initItems(data)}
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, e.toString())
                }
            }
        }
    }
    private fun initItems(data: List<Training>) {
        with(binding) {
            listTrainings.layoutManager = LinearLayoutManager(this@MainPage,LinearLayoutManager.VERTICAL, false)
            listTrainings.adapter = adapterTrainings
            listItems = data
            if (listItems.isNotEmpty()) {
                for (element in listItems) {
                    adapterTrainings.addResult(element)
                }
            }
        }
    }
    private fun initCategory(category: Category) {
        with(binding) {
            nameCategory.text = category.name
        }
    }
    override fun OnClick(result: Training) {
        val intent = Intent(this,WorkoutForTrainingActivity::class.java)
        intent.putExtra("idTraining",result.idTraining)
        intent.putExtra("idCategory",1)
        intent.putExtra("nameCategory","Легкая")
        intent.putExtra("nameTraining",result.name)
        startActivity(intent)
        finish()
    }
}