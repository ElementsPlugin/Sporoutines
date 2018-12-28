package sporoutines.simple

import org.spongepowered.api.scheduler.Task
import sporoutines.SyncContext
import sporoutines.TaskRunner
import java.util.concurrent.TimeUnit

class SimpleTaskRunner(private val plugin: Any) : TaskRunner {

    override var currentTask: Task? = null

    override fun doDelay(delay: Long, unit: TimeUnit, task: (Long) -> Unit) {
        currentTask = Task.builder()
            .delay(delay, unit)
            .apply { if (SyncContext.CURRENT.isAsync) async() }
            .execute { _ -> task(delay) }
            .submit(plugin)
    }

    override fun doYield(unit: TimeUnit, task: (Long) -> Unit) {
        doDelay(0, unit, task)
    }

    override fun doSwitchContext(context: SyncContext, task: (Boolean) -> Unit) {
        val current = SyncContext.CURRENT
        if (context == current) {
            task(false)
        } else {
            doNewContext(context) { task(true) }
        }
    }

    override fun doNewContext(context: SyncContext, task: () -> Unit) {
        currentTask = Task.builder()
            .apply { if (context.isAsync) async() }
            .execute(task)
            .submit(plugin)
    }
}