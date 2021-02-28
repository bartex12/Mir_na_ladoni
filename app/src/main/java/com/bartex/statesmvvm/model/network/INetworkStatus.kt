package com.bartex.statesmvvm.model.network

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

//Предусмотрим возможность подписаться на изменения статуса сети с помощью isOnline()
// или получить его один раз с помощью isOnlineSingle().
interface INetworkStatus {
    fun isOnline(): Observable<Boolean>
    fun isOnlineSingle(): Single<Boolean>
}