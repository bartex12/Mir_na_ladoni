package com.bartex.statesmvvm.model.api

import com.bartex.statesmvvm.model.entity.state.State
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IDataSourceState {

    @GET("all")
    fun getStates(): Single<List<State>>

//    @GET("name/{name}")
//    fun searchStates( @Path("name") name: String): Single<List<State>>
}