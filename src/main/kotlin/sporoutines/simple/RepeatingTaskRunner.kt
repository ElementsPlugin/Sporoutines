package sporoutines.simple

import org.spongepowered.api.scheduler.Task
import sporoutines.SyncContext
import sporoutines.TaskRunner
import java.util.concurrent.TimeUnit

class RepeatingTaskRunner(
    private val interval: Long,
    private val unit: TimeUnit,
    private val plugin: Any
) : TaskRunner {

    override var currentTask: Task? = null

    private var next: RepetitionContinuation? = null

    override fun doDelay(delay: Long, unit: TimeUnit, task: (Long) -> Unit) {
        next = RepetitionContinuation(task, delay)
    }

    override fun doYield(unit: TimeUnit, task: (Long) -> Unit) {
        next = RepetitionContinuation(task)
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
        doYield(TimeUnit.MILLISECONDS) { task() }
        currentTask?.cancel()

        val resuming: () -> Unit = { next?.tryResume(interval) }

        currentTask = Task.builder()
            .interval(interval, unit)
            .execute(resuming)
            .submit(plugin)
    }

    private class RepetitionContinuation(val resume: (Long) -> Unit, val delay: Long = 0) {

        var passedTime: Long = 0
        var resumed: Boolean = false

        fun tryResume(passedTime: Long) {
            if (this.resumed) throw IllegalStateException("Task is already resumed")

            this.passedTime += passedTime

            if (this.passedTime >= delay) {
                this.resumed = true
                resume(this.passedTime)
            }
        }
    }
}