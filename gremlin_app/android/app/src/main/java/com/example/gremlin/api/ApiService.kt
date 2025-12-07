package com.example.gremlin.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ChatRequest(val message: String, val chaosPreference: Float)
data class ChatResponse(val response: String, val sentiment: String)
data class ActionRequest(val action: String)
data class ActionResponse(val response: String)

interface ApiService {
    @POST("/chat")
    fun chat(@Body request: ChatRequest): Call<ChatResponse>

    @POST("/quick-action")
    fun quickAction(@Body request: ActionRequest): Call<ActionResponse>
}
