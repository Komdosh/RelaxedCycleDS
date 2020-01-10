package pro.komdosh.model

import kotlinx.coroutines.sync.Mutex
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

data class Node<S : BlockingQueue<T>, T>(
    private val structure: S,
    val mutex: Mutex = Mutex(),
    private val priorityComparator: Comparator<T>?,
    var isHead: Boolean = false
) {

    var next: Node<S, T> = this

    fun insert(el: T) {
        structure.offer(el)
    }

    fun pop(): T? {
        return structure.poll()
    }

    fun peek(): T? {
        return structure.peek()
    }

    fun size(): Int {
        return structure.size
    }

    fun createNewNext(): Node<S, T> {
        val node = Node(
            PriorityBlockingQueue<T>(16, priorityComparator) as S,
            Mutex(true),
            priorityComparator = priorityComparator
        )
        node.next = next
        return node
    }

    override fun toString(): String {
        return "Node(structure=${structure.size}, mutex=$mutex)"
    }

    fun readyToDelete(): Boolean {
        return !isHead && structure.isEmpty()
    }

    fun isEmpty(): Boolean {
        return structure.isEmpty()
    }
}
