
import pro.komdosh.api.RelaxedCircularDS
import pro.komdosh.implementation.CircularPriorityQueueImp
import java.lang.Thread.sleep
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

fun main() {
    val rcd: RelaxedCircularDS<BlockingQueue<Int>, Int> = CircularPriorityQueueImp()

    //usage example
    insert(rcd)
    insert(rcd)
    insert(rcd)

    sleep(2000)
    rcd.printInfo()
    sleep(2000)

    pop(rcd)
    pop(rcd)
    pop(rcd)

}

private fun pop(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = 1000
) {
    thread {
        for (i in 0..iterations) {
            val value = rcd.poll()
            println("Pop $value")
        }
    }
}

private fun insert(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = 1000
) {
    thread {
        for (i in 0..iterations) {
            rcd.offer(i)
            println("Insert $i")
        }
    }
}
