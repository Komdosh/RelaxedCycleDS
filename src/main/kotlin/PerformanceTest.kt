import pro.komdosh.api.RelaxedCircularDS
import pro.komdosh.implementation.CircularPriorityQueueImp
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.measureNanoTime

const val THREADS_NUM = 8
const val ITERATIONS = 20_000_000
const val ITERATION_STEP = 10_000_000
const val REPEATS = 4

data class Measurements(
    val threads: Int,
    var iterations: Int = 0,
    var repeats: Int = 0,
    var insertTime: Long = 0,
    var popTime: Long = 0,
    var randomTime: Long = 0
) {
    override fun toString(): String {
        val insertThroughput = getThroughputForSeconds(insertTime)
        val popThroughput = getThroughputForSeconds(popTime)
        val randomThroughput = getThroughputForSeconds(randomTime)
        return "$threads,$insertThroughput,$popThroughput,$randomThroughput"
    }

    private fun getThroughputForSeconds(time: Long) = (((iterations * repeats) / time.toDouble()) * 1000 * 1000).toLong()
}

fun main() {
    println("BlockingQueue")
    println("Threads,Iterations,Insert,Pop,Random")
    for (threads in 1..THREADS_NUM) {
        val measurements = Measurements(threads)
        for (iterations in ITERATION_STEP..ITERATIONS step ITERATION_STEP) {
            for (repeat in 1..REPEATS) {
                runBlocking(threads, iterations / threads, measurements)
            }
        }
        println(measurements.toString())
    }

    println("RelaxedCircularDS")
    println("Threads,Iterations,Insert,Pop,Random")
    for (threads in 1..THREADS_NUM) {
        val measurements = Measurements(threads)
        for (iterations in ITERATION_STEP..ITERATIONS step ITERATION_STEP) {
            for (repeat in 1..REPEATS) {
                runRelaxed(threads, iterations / threads, measurements)
            }
        }
        println(measurements.toString())
    }

}

fun runBlocking(threads: Int, iterations: Int, measurements: Measurements) {
    val blockingQueue: BlockingQueue<Int> = PriorityBlockingQueue(ITERATIONS, Collections.reverseOrder())

    //usage example
    val insertTime = measureNanoTime {
        for (i in 0..threads) {
            insertBlocking(blockingQueue, iterations)
        }
    }

    sleep(2000)

    val popTime = measureNanoTime {
        for (i in 0..threads) {
            popBlocking(blockingQueue, iterations)
        }
    }

    val randomTime = measureNanoTime {
        for (i in 0..threads) {
            if (Random(0).nextBoolean())
                insertBlocking(blockingQueue, iterations)
            else
                popBlocking(blockingQueue, iterations)
        }
    }

    measurements.insertTime += insertTime
    measurements.popTime += popTime
    measurements.randomTime += randomTime
    measurements.repeats += 1
    measurements.iterations += iterations * threads
}

fun runRelaxed(threads: Int, iterations: Int, measurements: Measurements) {
    val rcd: RelaxedCircularDS<BlockingQueue<Int>, Int> = CircularPriorityQueueImp()

    //usage example
    val insertTime = measureNanoTime {
        for (i in 0..threads) {
            insertRelaxed(rcd, iterations / threads)
        }
    }

    sleep(2000)

    val popTime = measureNanoTime {
        for (i in 0..threads) {
            popRelaxed(rcd, iterations / threads)
        }
    }

    val randomTime = measureNanoTime {
        for (i in 0..threads) {
            if (Random(0).nextBoolean())
                insertRelaxed(rcd, iterations / threads)
            else
                popRelaxed(rcd, iterations / threads)
        }
    }

    measurements.insertTime += insertTime
    measurements.popTime += popTime
    measurements.randomTime += randomTime
    measurements.repeats += 1
    measurements.iterations += iterations * threads
}

private fun popRelaxed(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = ITERATIONS
) {
    thread {
        for (i in 0..iterations) {
            rcd.poll()
        }
    }
}

private fun insertRelaxed(
    rcd: RelaxedCircularDS<BlockingQueue<Int>, Int>,
    iterations: Int = ITERATIONS
) {
    thread {
        for (i in 0..iterations) {
            rcd.offer(i)
        }
    }
}

private fun popBlocking(
    rcd: BlockingQueue<Int>,
    iterations: Int = ITERATIONS
) {
    thread {
        for (i in 0..iterations) {
            rcd.poll()
        }
    }
}

private fun insertBlocking(
    rcd: BlockingQueue<Int>,
    iterations: Int = ITERATIONS
) {
    thread {
        for (i in 0..iterations) {
            rcd.offer(i)
        }
    }
}
