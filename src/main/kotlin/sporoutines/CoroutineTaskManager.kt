package sporoutines

import kotlinx.coroutines.CoroutineScope
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

interface CoroutineTaskManager : CoroutineScope {

    val currentTask: Task?

    val isRepeating: Boolean

    suspend fun delay(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Long

    suspend fun yield(unit: TimeUnit = TimeUnit.MILLISECONDS): Long

    suspend fun switchContext(context: SyncContext): Boolean

    suspend fun newContext(context: SyncContext)

    suspend fun repeat(interval: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Long
}