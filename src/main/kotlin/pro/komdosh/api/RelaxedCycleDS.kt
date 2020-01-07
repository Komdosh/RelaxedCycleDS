package pro.komdosh.api

import java.util.concurrent.BlockingQueue

interface RelaxedCycleDS<S : BlockingQueue<T>, T> {
    fun insert(el: T)
    fun pop(): T?
    fun print()
}
