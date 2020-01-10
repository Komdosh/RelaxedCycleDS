package pro.komdosh.api

import java.util.concurrent.BlockingQueue

interface RelaxedCircularDS<S : BlockingQueue<T>, T> {
    fun offer(el: T)
    fun poll(): T?
    fun peek(): T?
    fun isEmpty(): Boolean
    fun print()
}
