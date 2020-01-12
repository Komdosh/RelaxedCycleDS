package pro.komdosh.model

import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

data class Node<S : BlockingQueue<T>, T : Comparable<T>>(
    private val structure: S,
    val isUsed: AtomicBoolean = AtomicBoolean(),
    private val priorityComparator: Comparator<T>?,
    var isHead: Boolean = false
) {
    var next: Node<S, T> = this

    private var maxValue: T? = null

    fun insert(el: T) {
        if (maxValue == null) {
            maxValue = el
        } else if (el > maxValue!!) {
            maxValue = el
        }

        structure.offer(el)
    }

    fun pop(): T? {
        val value = structure.poll()
        maxValue = null
        return value
    }

    fun peek(): T? {
        if (maxValue == null) {
            maxValue = structure.peek()
        }
        return maxValue
    }

    fun size(): Int {
        return structure.size
    }

    fun createNewNext(): Node<S, T> {
        val node = Node(
            PriorityBlockingQueue<T>(1024, priorityComparator) as S,
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
        return !isHead && isEmpty()
    }

    fun isEmpty(): Boolean {
        return structure.isEmpty()
    }
}
