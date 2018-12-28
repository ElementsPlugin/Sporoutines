package sporoutines

import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

interface TaskRunner {

    val currentTask: Task?

    fun doDelay(delay: Long, unit: TimeUnit, task: (Long) -> Unit)

    fun doYield(unit: TimeUnit, task: (Long) -> Unit)

    fun doSwitchContext(context: SyncContext, task: (Boolean) -> Unit)

    fun doNewContext(context: SyncContext, task: () -> Unit)
}