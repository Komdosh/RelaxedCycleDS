package pro.komdosh.implementation

import kotlinx.coroutines.sync.Mutex
import pro.komdosh.api.RelaxedCycleDS
import pro.komdosh.model.Node
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

class RelaxedCycleDSImp<S : BlockingQueue<T>, T> : RelaxedCycleDS<S, T> {

    private val head: Node<S, T> = Node(structure = PriorityBlockingQueue<T>() as S, isHead = true)

    private val cycleChanges: Mutex = Mutex()

    override fun insert(el: T) {
        var node = head
        if (!node.mutex.tryLock()) {
            do {
                node = node.next
            } while (!node.isHead && !node.mutex.tryLock())

            if (node.isHead) {
                while (!cycleChanges.tryLock());
                node = node.createNewNext(head)
                cycleChanges.unlock()
            }
        }
        node.insert(el)
        if (node.mutex.isLocked) {
            node.mutex.unlock()
        }
    }

    override fun pop(): T? {
        var node = head
        var prev: Node<S, T>
        do {
            prev = node
            node = node.next
        } while (!node.isEmpty() && !node.mutex.tryLock())

        val value: T? = node.pop()
        if (!node.isHead && node.readyToDelete()) {
            while (!cycleChanges.tryLock());
            prev.next = node.next
            cycleChanges.unlock()
        }
        if (node.mutex.isLocked) {
            node.mutex.unlock()
        }

        return value
    }

    override fun print() {
        var node = head
        do {
            println(node)
            node = node.next
        } while (!node.isHead)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RelaxedCycleDSImp<*, *>

        if (head != other.head) return false

        return true
    }

    override fun hashCode(): Int {
        return head.hashCode()
    }

}
