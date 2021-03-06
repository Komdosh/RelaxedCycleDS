package pro.komdosh.api

import java.util.concurrent.BlockingQueue

interface RelaxedCircularDS<S : BlockingQueue<T>, T : Comparable<T>> {
    fun offer(el: T): Boolean
    fun poll(): T?
    fun peek(): T?
    fun clear()
    fun isEmpty(): Boolean
    fun printInfo()
}
