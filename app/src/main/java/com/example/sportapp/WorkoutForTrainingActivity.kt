package com.example.sportapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportapp.Adapters.APIRequests
import com.example.sportapp.Adapters.AdapterWorkout
import com.example.sportapp.Model.Training
import com.example.sportapp.Model.Workout
import com.example.sportapp.databinding.ActivityWorkoutForTrainingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class WorkoutForTrainingActivity : AppCompatActivity(), AdapterWorkout.Listener {
    private lateinit var binding: ActivityWorkoutForTrainingBinding
    private val URL_API = "https://iis.ngknn.ru/ngknn/МамшеваЮС/6/"
    private val adapterWorkout = AdapterWorkout(this)
    private var listItems: List<Workout> = ArrayList<Workout>()
    private var idCategory = 0
    private var idTraining = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutForTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idTraining = intent.getIntExtra("idTraining",0)
        idCategory = intent.getIntExtra("idTraining",0)
        with(binding) {
            btnBack.setOnClickListener {
                val intent = Intent(this@WorkoutForTrainingActivity,MainPage::class.java)
                startActivity(intent)
                finish()
            }
            btnStart.setOnClickListener {
                val intent = Intent(this@WorkoutForTrainingActivity,TimerActivity::class.java)
                intent.putExtra("idCategory",idCategory)
                intent.putExtra("idTraining",idTraining)
                startActivity(intent)
                finish()
            }
            var nameCategory = intent.getStringExtra("nameCategory")
            var nameTraining = intent.getStringExtra("nameTraining")
            if (nameCategory != null && nameTraining != null) {
                CategoryAndTrainingNames.text = nameCategory + "." + nameTraining
            }
        }
        getDataWorkout()
    }
    private fun getDataWorkout() {
        val api = Retrofit.Builder()
            .baseUrl(URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequests::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getAllWorkoutForTraining(idCategory,idTraining).awaitResponse()
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
    private fun initItems(data: List<Workout>) {
        with(binding) {
            listWorkout.layoutManager = LinearLayoutManager(this@WorkoutForTrainingActivity,
                LinearLayoutManager.VERTICAL, false)
            listWorkout.adapter = adapterWorkout
            listItems = data
            if (listItems.isNotEmpty()) {
                for (element in listItems) {
                    adapterWorkout.addResult(element)
                }
            }
        }
    }
    override fun OnClick(result: Workout) {
        val itemListDialogFragment = ItemListDialogFragment()
        val bundle = Bundle()
        bundle.putSerializable("iteminformation",result)
        itemListDialogFragment.arguments = bundle
        itemListDialogFragment.show(supportFragmentManager,"pop")
    }
}