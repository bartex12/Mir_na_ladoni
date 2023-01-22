package com.bartex.statesmvvm.model.api

import com.bartex.statesmvvm.model.constants.Constants
import com.bartex.statesmvvm.model.entity.state.State
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DataSourceRetrofit :IDataSource{

    override suspend fun getStatesCoroutine(): List<State> {
        return getDataSource().getStatesCoroutine()
    }

    private fun getDataSource(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                )).build().create(ApiService::class.java)
    }
}