import pro.komdosh.api.RelaxedCircularDS
import pro.komdosh.implementation.CircularPriorityQueueImp
import java.lang.Thread.sleep
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

fun main() {
//    val rcd: BlockingQueue<Int> = PriorityBlockingQueue<Int>(1024, Collections.reverseOrder())
    val rcd: RelaxedCircularDS<BlockingQueue<Int>, Int> = CircularPriorityQueueImp()

    //usage example
    insertRelaxed(rcd)
    insertRelaxed(rcd)
    insertRelaxed(rcd)

    sleep(2000)

    popRelaxed(rcd)
    popRelaxed(rcd)
    popRelaxed(rcd)

}

private fun popRelaxed(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = 100
) {
    thread {
        for (i in 0..iterations) {
            val value = rcd.poll()
            println("Pop $value")
        }
    }
}

private fun insertRelaxed(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = 100
) {
    thread {
        for (i in 0..iterations) {
            rcd.offer(i)
            println("Insert $i")
        }
    }
}
