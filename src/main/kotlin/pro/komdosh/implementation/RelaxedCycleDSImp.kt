package pro.komdosh.implementation

import kotlinx.coroutines.sync.Mutex
import pro.komdosh.api.RelaxedCycleDS
import pro.komdosh.model.Node
import java.util.*

class RelaxedCycleDSImp<S : Queue<T>, T> : RelaxedCycleDS<S, T> {

    private var head: Node<S, T>? = null

    override fun insert(el: T) {
        if (head == null) {
            head = Node(LinkedList<T>() as S, null, Mutex())
            head!!.next = head
        }
        val valHead = head!!
        var node = head!!
        if (!head!!.mutex.tryLock()) {
            do {
                node = valHead.next!!
            } while (node != head && !node.mutex.tryLock())

            if (node == head) {
                node = node.createNewNext()!!
            }
        }

        println("insert $el")
        node.insert(el)
        node.mutex.unlock()
    }

    override fun pop(): T? {
        if (head == null) {
            return null
        }

        val valHead = head!!
        var node = head!!
        while (!node.mutex.tryLock()) {
            node = valHead.next!!
        }

        val value: T? = node.pop()
        println("pop $value")
        node.mutex.unlock()

        return value;
    }

}