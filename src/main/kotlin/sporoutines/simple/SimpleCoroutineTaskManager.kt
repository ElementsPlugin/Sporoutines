package sporoutines.simple

import org.spongepowered.api.scheduler.Task
import sporoutines.CoroutineTaskManager
import sporoutines.SyncContext
import sporoutines.TaskRunner
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

class SimpleCoroutineTaskManager(val plugin: Any) : CoroutineTaskManager, Continuation<Unit> {
    override val context: CoroutineContext get() = EmptyCoroutineContext
    override val coroutineContext: CoroutineContext get() = context

    private var runner: TaskRunner = SimpleTaskRunner(plugin)

    override val currentTask: Task? get() = runner.currentTask

    override val isRepeating: Boolean get() = runner is RepeatingTaskRunner

    internal suspend fun start(initial: SyncContext) = suspendCoroutine<Unit> { cont ->
        runner.doSwitchContext(initial) { cont.resume(Unit) }
    }

    override suspend fun delay(delay: Long, unit: TimeUnit): Long = suspendCoroutine { cont ->
        runner.doDelay(delay, unit, cont::resume)
    }

    override suspend fun yield(unit: TimeUnit): Long = suspendCoroutine { cont ->
        runner.doYield(unit, cont::resume)
    }

    override suspend fun switchContext(context: SyncContext): Boolean = suspendCoroutine { cont ->
        runner.doSwitchContext(context, cont::resume)
    }

    override suspend fun newContext(context: SyncContext): Unit = suspendCoroutine { cont ->
        runner.doNewContext(context) { cont.resume(Unit) }
    }

    override suspend fun repeat(interval: Long, unit: TimeUnit): Long = suspendCoroutine { cont ->
        runner = RepeatingTaskRunner(interval, unit, plugin)
        runner.doNewContext(SyncContext.CURRENT) { cont.resume(0) }
    }

    internal fun cleanup() {
        currentTask?.cancel()
    }

    override fun resumeWith(result: Result<Unit>) {
        cleanup()
        result.getOrThrow()
    }
}