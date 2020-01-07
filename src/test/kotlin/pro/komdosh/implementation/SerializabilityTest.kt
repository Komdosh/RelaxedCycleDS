package pro.komdosh.implementation

import org.jetbrains.kotlinx.lincheck.LinChecker
import org.jetbrains.kotlinx.lincheck.LoggingLevel
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTest
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.jetbrains.kotlinx.lincheck.verifier.SerializabilityVerifier
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.jupiter.api.Test
import pro.komdosh.api.RelaxedCycleDS
import java.util.concurrent.BlockingQueue

@StressCTest(verifier = SerializabilityVerifier::class)
internal class SerializabilityTest : VerifierState() {
    private val rcds: RelaxedCycleDS<BlockingQueue<Int>, Int> = RelaxedCycleDSImp()

    @Operation
    fun offer(value: Int) {
        return rcds.insert(value)
    }

    @Operation
    fun poll(): Int? {
        return rcds.pop()
    }

    @Test
    fun main() {
        val opts = StressOptions()
            .iterations(10)
            .threads(2)
            .logLevel(LoggingLevel.INFO)
        LinChecker.check(SerializabilityTest::class.java, opts)
    }

    @Override
    override fun extractState(): Any {
        return rcds
    }
}