package com.example.sportapp.Adapters

import com.example.sportapp.Model.Category
import com.example.sportapp.Model.Training
import com.example.sportapp.Model.Workout
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIRequests {
    @GET("api/GetCurrentCategory/{id}")
    fun getCurrentCategory(@Path("id") id: Int): Call<Category>
    @GET("api/GetAllTrainingsForCategory/{id}")
    fun getAllTrainingsForCategory(@Path("id") id: Int): Call<List<Training>>
    @GET("api/GetAllWorkoutForTraining/{idCategory}/{idTraining}")
    fun getAllWorkoutForTraining(@Path("idCategory") idCategory: Int, @Path("idTraining") idTraining: Int): Call<List<Workout>>
}