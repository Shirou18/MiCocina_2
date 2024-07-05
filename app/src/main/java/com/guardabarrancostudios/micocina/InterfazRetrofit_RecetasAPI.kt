package com.guardabarrancostudios.micocina

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InterfazRetrofit_RecetasAPI {
    //@GET("recetas")
    //fun obtenerRecetas(): Call<List<Receta>>

    @POST("api/Recetas_API")
    fun crearReceta(@Body receta: ModelReceta): Call<Void>

    @PUT("api/Recetas_API/{id}")
    fun editarReceta(@Path("id") id: Int, @Body receta: ModelReceta): Call<Void>

    @DELETE("api/Recetas_API/{id}")
    fun eliminarReceta(@Path("id") id: Int): Call<Void>
}
