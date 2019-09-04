package pro.komdosh.api

import java.util.*

interface RelaxedCycleDS<S: Queue<T>, T> {
    fun insert(el: T)
    fun pop(): T?
}