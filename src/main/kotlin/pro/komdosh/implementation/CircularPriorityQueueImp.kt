package pro.komdosh.implementation

import pro.komdosh.api.RelaxedCircularDS
import pro.komdosh.model.Node
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

class CircularPriorityQueueImp<S : BlockingQueue<T>, T : Comparable<T>>(
    priority: Priority = Priority.MAX
) : RelaxedCircularDS<S, T> {

    private val priorityComparator: Comparator<T>? = if (priority == Priority.MAX) Collections.reverseOrder() else null

    enum class Priority {
        MAX, MIN
    }

    private val head: Node<S, T> = Node(
        structure = PriorityBlockingQueue(1024, priorityComparator) as S,
        isHead = true,
        priorityComparator = priorityComparator
    )

    override fun offer(el: T): Boolean {
        var node = head
        if (!node.isUsed.compareAndSet(false, true)) {
            if (!node.next.isHead) {
                do {
                    node = node.next
                } while (!node.isHead && !node.isUsed.compareAndSet(false, true))
            }
            if (node.isHead) {
                val next = node.createNewNext()
                head.next = next
                node = next
            }
        }
        val inserted = node.insert(el)
        node.isUsed.set(false)
        return inserted
    }

    override fun poll(): T? {
        val prev = getPrevPriorNode()
        val nodeToPop = prev.next

        val value: T? = nodeToPop.pop()
        if (nodeToPop.readyToDelete()) {
            prev.next = nodeToPop.next
        }

        return value
    }

    override fun peek(): T? {
        val prev = getPrevPriorNode()
        return prev.next.peek()
    }

    private fun getPrevPriorNode(): Node<S, T> {
        var node = head.next
        var prevNode: Node<S, T> = head

        if (node.isHead) {
            return node
        }

        var priorValue: T? = prevNode.peek()
        var prevPriorNode: Node<S, T> = prevNode

        do {
            val value = node.peek()
            if (priorValue == null || (value != null && priorValue < value)) {
                priorValue = value
                prevPriorNode = prevNode
            }
            node = node.next
            prevNode = node
        } while (!node.isHead)

        return prevPriorNode
    }


    override fun isEmpty(): Boolean {
        var node = head

        do {
            if (!node.isEmpty()) {
                return false
            }
            node = node.next
        } while (node != head)

        return true
    }

    override fun printInfo() {
        var node = head
        var size = 0
        var structures = 0
        do {
            structures++
            size += node.size()
            node = node.next
        } while (!node.isHead)
        println("Size: $size, Structures $structures")
    }
}
