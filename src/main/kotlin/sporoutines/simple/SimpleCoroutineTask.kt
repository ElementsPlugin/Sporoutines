package sporoutines.simple

import org.spongepowered.api.scheduler.Task
import sporoutines.CoroutineTask
import kotlin.coroutines.resume

class SimpleCoroutineTask(private val manager: SimpleCoroutineTaskManager) : CoroutineTask {

    override val plugin: Any
        get() = manager.plugin

    override val currentTask: Task?
        get() = manager.currentTask

    override val isSynchronous: Boolean
        get() = !(manager.currentTask?.isAsynchronous ?: true)

    override val isAsynchronous: Boolean
        get() = manager.currentTask?.isAsynchronous ?: false

    override fun cancel() {
        manager.resume(Unit)
    }
}