package pro.komdosh.model

import kotlinx.coroutines.sync.Mutex
import java.util.*

data class Node<S: Queue<T>, T>(private val structure: S, var next: Node<S, T>?, val mutex: Mutex){
    fun insert(el: T) {
        structure.add(el)
    }

    fun pop(): T? {
       return structure.poll()
    }

    fun createNewNext(): Node<S,T>?{
        val prev = next
        next = Node(LinkedList<T>() as S, null, Mutex(true))
        next!!.next = prev
        return next
    }

    override fun toString(): String {
        return "Node(structure=$structure, mutex=$mutex)"
    }


}