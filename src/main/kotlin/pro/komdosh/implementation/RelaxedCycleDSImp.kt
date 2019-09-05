package pro.komdosh.implementation

import kotlinx.coroutines.sync.Mutex
import pro.komdosh.api.RelaxedCycleDS
import pro.komdosh.model.Node
import java.util.*

class RelaxedCycleDSImp<S : Queue<T>, T> : RelaxedCycleDS<S, T> {

    private var head: Node<S, T>? = null

    private val cycleChanged: Mutex = Mutex()

    override fun insert(el: T) {
        if (head == null) {
            while (!cycleChanged.tryLock());
            if (head == null) {
                head = Node(LinkedList<T>() as S, null, Mutex())
                head!!.next = head
            }
            cycleChanged.unlock()
        }
        val valHead = head!!
        var node = head!!
        if (!head!!.mutex.tryLock()) {
            do {
                node = valHead.next!!
            } while (node != head && !node.mutex.tryLock())

            if (node == head) {
                while (!cycleChanged.tryLock());
                node = node.createNewNext(head!!)
                cycleChanged.unlock()
            }
        }

        println("insert $el")
        node.insert(el)
        node.mutex.unlock()
    }

    override fun pop(): T? {
        if (head == null) {
            println("head null")
            return null
        }

        val valHead = head!!
        var node = head
        var prev = valHead
        while (!node!!.mutex.tryLock()) {
            prev = node
            node = node.next
            if (node == null) {
                println("node null")
                return null
            }
        }

        val value: T? = node.pop()
        if (node.readyToDelete()) {
            while (!cycleChanged.tryLock());
            if (node == head && node.next == head) {
                println("node null")
                head = null
            } else {
                prev.next = node.next
            }
            cycleChanged.unlock()
        }
        println("pop $value")
        node.mutex.unlock()

        return value;
    }

}