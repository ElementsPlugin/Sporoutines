package sporoutines

import org.spongepowered.api.scheduler.Task

interface CoroutineTask {

    val plugin: Any

    val currentTask: Task?

    val isSynchronous: Boolean

    val isAsynchronous: Boolean

    fun cancel()
}