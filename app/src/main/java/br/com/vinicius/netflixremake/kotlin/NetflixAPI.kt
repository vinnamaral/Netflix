package br.com.vinicius.netflixremake.kotlin

import br.com.vinicius.netflixremake.model.Categories
import br.com.vinicius.netflixremake.model.MovieDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface NetflixAPI {

    @GET("home")
    fun listCategories(): Call<Categories>

    @GET("{id}")
    fun getMovieBy(@Path("id") id: Int) : Call<MovieDetail>

}

fun retrofit() : Retrofit =
    Retrofit.Builder()
        .baseUrl("https://tiagoaguiar.co/api/netflix/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
