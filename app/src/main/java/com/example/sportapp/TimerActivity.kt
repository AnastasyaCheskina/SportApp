package com.example.sportapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.Adapters.APIRequests
import com.example.sportapp.Model.Workout
import com.example.sportapp.databinding.ActivityTimerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private var idCategory = 0
    private var idTraining = 0
    private var counter = 0
    private val URL_API = "https://iis.ngknn.ru/ngknn/МамшеваЮС/6/"
    private var listWorkout: List<Workout> = ArrayList<Workout>()
    private var allCount = 0
    private var currentCount = 0

    private var currentTime = 0
    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idTraining = intent.getIntExtra("idTraining",0)
        idCategory = intent.getIntExtra("idTraining",0)
        getDataWorkout()
        with(binding) {
            btnEndTraining.setOnClickListener {
                val intent = Intent(this@TimerActivity,EndTrainingActivity::class.java)
                startActivity(intent)
                finish()
            }
            btnMiss.setOnClickListener {
                if (allCount == currentCount) {
                    val intent = Intent(this@TimerActivity,EndTrainingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    counter++
                    initItems()
                }
            }
        }
    }
    private fun startCountDownTimer(timeMillis: Long) { //в аргументах - время которое нужно отсчитать (мс)
        timer?.cancel()
        timer = object :
            CountDownTimer(timeMillis, 1000) { //тут время и шаг: в данном случае 1с (1000 мс)
            override fun onTick(timeM: Long) { //отсчет времени
                val decimalFormat = DecimalFormat("00")
                val timeFormat = "00:" + decimalFormat.format(timeM / 1000)
                binding.time.text = timeFormat
                binding.circularProgressBar.max = (timeM / 1000).toInt()
                binding.circularProgressBar.progress = (timeM / 1000).toInt()
            }
            //когда время вышло
            override fun onFinish() {
                if (allCount == currentCount) {
                    val intent = Intent(this@TimerActivity,EndTrainingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    counter++
                    initItems()
                }
            }
        }.start()
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
                    listWorkout = data
                    runOnUiThread {initItems()}
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, e.toString())
                }
            }
        }
    }
    private fun initItems() {
        if (listWorkout.isNotEmpty()) {
            var selectedItem = listWorkout[counter]
            with(binding) {
                nameWorkout.text = selectedItem.name
                currentTime = selectedItem.durationInSec
                allCount = listWorkout.count()
                currentCount = counter + 1
                startCountDownTimer((currentTime*1000).toLong())
            }
        }
    }
}