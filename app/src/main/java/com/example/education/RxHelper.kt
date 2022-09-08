package com.example.education

import android.widget.SearchView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

object RxSearchObservable {
    fun fromView(searchView: SearchView): Observable<String> {
        val subject: BehaviorSubject<String> = BehaviorSubject.create()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                subject.onNext(text ?: "")
                return true
            }
        })
        return subject
    }
}