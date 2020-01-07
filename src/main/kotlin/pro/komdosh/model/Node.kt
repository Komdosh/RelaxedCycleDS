package pro.komdosh.model

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

data class Node<S : BlockingQueue<T>, T>(
    private val structure: S,
    val mutex: Mutex = Mutex(),
    var isHead: Boolean = false
) {

    var next: Node<S, T>

    init {
        next = this
    }

    fun insert(el: T) {
        structure.add(el)
    }

    fun pop(): T? {
        return structure.poll()
    }

    fun createNewNext(head: Node<S, T>): Node<S, T> {
        next = Node(PriorityBlockingQueue<T>() as S, Mutex(true))
        next.next = head
        return next
    }

    override fun toString(): String {
        return "Node(structure=$structure, mutex=$mutex)"
    }

    fun readyToDelete(): Boolean {
        return structure.isEmpty()
    }

    fun isEmpty(): Boolean {
        return structure.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*, *>


        if (!arrayOf(structure.toList()).contentEquals(arrayOf(other.structure.toList()))) return false
        if (isHead != other.isHead) return false

        return true
    }

    override fun hashCode(): Int {
        var result = arrayOf(structure.toList()).contentHashCode()

        result = 31 * result + isHead.hashCode()
        return result
    }
}
