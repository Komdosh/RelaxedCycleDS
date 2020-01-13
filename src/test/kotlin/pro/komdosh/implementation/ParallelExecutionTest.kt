package pro.komdosh.implementation

import kotlinx.coroutines.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pro.komdosh.api.RelaxedCircularDS
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal class ParallelExecutionTest {
    private lateinit var rcds: RelaxedCircularDS<BlockingQueue<Int>, Int>
    private lateinit var blockingQueue: PriorityBlockingQueue<Int>

    @BeforeEach
    fun init() {
        rcds = CircularPriorityQueueImp()
        blockingQueue = PriorityBlockingQueue(1024, Collections.reverseOrder())
    }

    @Test
    @ExperimentalTime
    fun multipleThreadStressTest() {
        val iterations = 2000000
        val step = 100000
        val threads = 10

        var total = 0
        var wins = 0
        var time = 0.0
        for (t in 2..threads) {
            for (iter in step..iterations step step) {
                total++
                println("Threads: $t, Iterations: $iter")
                val relaxedDuration = structureTest(t, iter, "Relaxed", { i -> rcds.offer(i) }, { rcds.poll() })
                val strongDuration =
                    structureTest(t, iter, "Strong", { i -> blockingQueue.offer(i) }, { blockingQueue.poll() })
                val res = if (strongDuration > relaxedDuration) {
                    wins++
                    "win"
                } else "loose"
                time += (strongDuration - relaxedDuration).inMilliseconds
                println("${strongDuration - relaxedDuration} $res")
            }
        }
        println("Summary $wins/$total $time")

    }

    @ExperimentalTime
    private fun structureTest(
        threads: Int,
        iterations: Int,
        name: String,
        offerFunction: (Int) -> Unit,
        pollFunction: () -> Int?
    ): Duration {
        val offerTime = measureTime {
            runIterations(threads, iterations) {
                offerFunction(it)
            }
        }
        val pollTime = measureTime {
            runIterations(threads, iterations) {
                pollFunction()
            }
        }
        val duration = offerTime + pollTime
        println("$name OfferTime: $offerTime, PollTime: $pollTime, TotalTime: $duration")
        return duration
    }

    private fun runIterations(threads: Int, iterations: Int, doOperation: (Int) -> Unit) {
        runBlocking {
            withContext(Dispatchers.IO + Job()) {
                for (t in 0 until threads) {
                    launch {
                        for (i in 0 until iterations) {
                            doOperation(i)
                        }
                    }
                }
            }
        }
    }
}
