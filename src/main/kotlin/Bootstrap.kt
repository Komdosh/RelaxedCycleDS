import kotlinx.coroutines.joinAll
import pro.komdosh.api.RelaxedCycleDS
import pro.komdosh.implementation.RelaxedCycleDSImp
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread

fun main() {
    println("init")

    val rcd: RelaxedCycleDS<Queue<Int>,Int> = RelaxedCycleDSImp()
    thread{
        for(i in 0..10){
            rcd.insert(i)
            sleep(100)
        }
    }
    thread{
        for(i in 0..10){
            rcd.insert(i)
            sleep(100)
        }
    }


    sleep(2000)

    thread{
        for(i in 0..10){
            rcd.pop()
            sleep(100)
        }
    }

    thread{
        for(i in 0..10){
            rcd.pop()
            sleep(100)
        }
    }
    sleep(2000)
}