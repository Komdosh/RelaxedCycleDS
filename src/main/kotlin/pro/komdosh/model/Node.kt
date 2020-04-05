package pro.komdosh.model

import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

data class Node<S : BlockingQueue<T>, T : Comparable<T>>(
    private val structure: S,
    val isUsed: AtomicBoolean = AtomicBoolean(),
    private val priorityComparator: Comparator<T>?,
    var isHead: AtomicBoolean = AtomicBoolean()
) {
    var next: Node<S, T> = this

    fun insert(el: T): Boolean {
        return structure.offer(el)
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
            PriorityBlockingQueue(1024, priorityComparator) as S,
            AtomicBoolean(true),
            priorityComparator = priorityComparator
        )
        node.next = next
        return node
    }

    override fun toString(): String {
        return "Node(structure=${structure.size}, mutex=$isUsed)"
    }

    fun readyToDelete(): Boolean {
        return !isHead.get() && isEmpty()
    }

    fun isEmpty(): Boolean {
        return structure.isEmpty()
    }
}
