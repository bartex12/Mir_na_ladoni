package com.bartex.statesmvvm.view.fragments.states

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    fun ui(): Scheduler
}