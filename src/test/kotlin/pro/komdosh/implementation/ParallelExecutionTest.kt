package pro.komdosh.implementation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pro.komdosh.api.RelaxedCycleDS
import java.util.concurrent.BlockingQueue

internal class ParallelExecutionTest {
    private lateinit var rcds: RelaxedCycleDS<BlockingQueue<Int>, Int>

    @BeforeEach
    fun init() {
        rcds = RelaxedCycleDSImp()
    }

    @Test
    fun singleThread() {
        val job = Job()
        CoroutineScope(job).run {
            rcds.insert(1)
        }

        assert(1 == rcds.pop())
    }
}
