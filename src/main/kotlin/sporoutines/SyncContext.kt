package sporoutines

import org.spongepowered.api.Sponge

enum class SyncContext(val isAsync: Boolean) {

    SYNC(false),

    ASYNC(true);

    companion object {

        val CURRENT: SyncContext
            get() = if (Sponge.getServer().isMainThread) SYNC else ASYNC
    }
}