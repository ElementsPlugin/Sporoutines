package sporoutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.spongepowered.api.scheduler.Scheduler

fun Scheduler.syncDispatcher(plugin: Any): CoroutineDispatcher =
    this.createSyncExecutor(plugin).asCoroutineDispatcher()

fun Scheduler.asyncDispatcher(plugin: Any): CoroutineDispatcher =
    this.createAsyncExecutor(plugin).asCoroutineDispatcher()