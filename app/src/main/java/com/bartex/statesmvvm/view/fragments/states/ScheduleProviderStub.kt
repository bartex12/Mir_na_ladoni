package com.bartex.statesmvvm.view.fragments.states

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class ScheduleProviderStub:SchedulerProvider {

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

}