package com.example.sportapp.Model

data class Workout(
    val description: String,
    val durationInSec: Int,
    val idWorkout: Int,
    val name: String,
    val videoURL: String
):java.io.Serializable