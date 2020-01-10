package pro.komdosh.implementation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pro.komdosh.api.RelaxedCircularDS
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal class ParallelExecutionTest {
    private lateinit var rcds: RelaxedCircularDS<BlockingQueue<Int>, Int>
    private lateinit var blockingQueue: PriorityBlockingQueue<Int>

    @BeforeEach
    fun init() {
        rcds = CircularPriorityQueueImp()
        blockingQueue = PriorityBlockingQueue(16, Collections.reverseOrder())
    }

    @ExperimentalTime
    @Test
    fun multipleThreadStressTest() {
        val iterations = 2000000
        val threads = 3

        val relaxedTime = measureTime {
            structureTest(threads, iterations, { i -> rcds.offer(i) }, { rcds.poll() })
        }

        rcds.print()

        val traditionalTime = measureTime {
            structureTest(threads, iterations, { i -> blockingQueue.offer(i) }, { blockingQueue.poll() })
        }

        println(blockingQueue.size)
        println(relaxedTime)
        println(traditionalTime)
    }


    private fun structureTest(threads: Int, iterations: Int, offerFunction: (Int) -> Unit, pollFunction: () -> Int?) {
        runIterations(threads, iterations) {
            offerFunction(it)
        }
        runIterations(threads, iterations) {
            pollFunction()
        }
    }

    private fun runIterations(threads: Int, iterations: Int, doOperation: (Int) -> Unit) {
        runBlocking {
            withContext(Dispatchers.IO) {
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
