
import pro.komdosh.api.RelaxedCycleDS
import pro.komdosh.implementation.RelaxedCycleDSImp
import java.lang.Thread.sleep
import java.util.concurrent.BlockingQueue
import kotlin.concurrent.thread

fun main() {
    val rcd: RelaxedCycleDS<BlockingQueue<Int>, Int> = RelaxedCycleDSImp()

    val iterations = 10
    thread{
        for (i in 0..iterations) {
            rcd.insert(i)
            sleep(100)
        }
    }
    thread {
        for (i in 0..iterations) {
            rcd.insert(i)
            sleep(100)
        }
    }

    sleep(2000)
    rcd.print()
    sleep(2000)

    thread {
        for (i in 0..iterations) {
            rcd.pop()
            sleep(100)
        }
    }

    thread {
        for (i in 0..iterations) {
            rcd.pop()
            sleep(100)
        }
    }
    sleep(2000)
}
