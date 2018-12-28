package sporoutines

import sporoutines.simple.SimpleCoroutineTask
import sporoutines.simple.SimpleCoroutineTaskManager
import kotlin.coroutines.startCoroutine

fun task(
    plugin: Any,
    initial: SyncContext = SyncContext.SYNC,
    run: suspend CoroutineTaskManager.() -> Unit
): CoroutineTask {
    val manager = SimpleCoroutineTaskManager(plugin)

    val block: suspend SimpleCoroutineTaskManager.() -> Unit = {
        try {
            this.start(initial)
            this.run()
        } finally {
            this.cleanup()
        }
    }

    block.startCoroutine(receiver = manager, completion = manager)
    return SimpleCoroutineTask(manager)
}