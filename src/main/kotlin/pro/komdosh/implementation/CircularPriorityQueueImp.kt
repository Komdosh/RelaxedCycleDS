package pro.komdosh.implementation

import pro.komdosh.api.RelaxedCircularDS
import pro.komdosh.model.Node
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

class CircularPriorityQueueImp<S : BlockingQueue<T>, T : Comparable<T>>(
    private val priority: Priority = Priority.MAX
) : RelaxedCircularDS<S, T> {

    private val priorityComparator: Comparator<T>? = if (priority == Priority.MAX) Collections.reverseOrder() else null

    enum class Priority {
        MAX, MIN
    }

    private val head: Node<S, T> = Node(
        structure = PriorityBlockingQueue<T>(1024, priorityComparator) as S,
        isHead = true,
        priorityComparator = priorityComparator
    )

    override fun offer(el: T) {
        var node = head
        if (!node.mutex.tryLock()) {
            do {
                node = node.next
            } while (!node.isHead && !node.mutex.tryLock())

            if (node.isHead) {
                head.next = node.createNewNext()
                node = head.next
            }
        }
        node.insert(el)
        node.mutex.unlock()
    }

    override fun poll(): T? {
        val (prev: Node<S, T>, nodeToPop: Node<S, T>) = getPriorNode()

        val value: T? = nodeToPop.pop()
        if (nodeToPop.readyToDelete()) {
            prev.next = nodeToPop.next
        }

        return value
    }

    override fun peek(): T? {
        val (_, nodeToPop: Node<S, T>) = getPriorNode()
        return nodeToPop.peek()
    }

    private fun getPriorNode(): Pair<Node<S, T>, Node<S, T>> {
        var node = head.next
        var prevNode: Node<S, T> = head

        if (node.isHead) {
            return Pair(prevNode, node)
        }

        var priorNode = head
        var priorValue = head.peek()

        do {
            val value = node.peek()
            if (priorValue == null || (value != null && priorValue < value)) {
                priorValue = value
                priorNode = node
            }
            node = node.next
            prevNode = node
        } while (!node.isHead)

        return Pair(prevNode, priorNode)
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
