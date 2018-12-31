package sporoutines

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.value.BaseValue
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.EventListener
import org.spongepowered.api.event.data.ChangeDataHolderEvent
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Player.awaitKeyChange(plugin: Any, key: Key<out BaseValue<T>>, expected: T) =
    suspendCoroutine<Unit> { cont ->
        val listener = object : EventListener<ChangeDataHolderEvent.ValueChange> {
            override fun handle(event: ChangeDataHolderEvent.ValueChange) {
                for (value in event.endResult.successfulData) {
                    if (value.key == key && value.get() == expected) {
                        Sponge.getEventManager().unregisterListeners(this)
                        cont.resume(Unit)
                    }
                }
            }
        }

        Sponge.getEventManager().registerListener(plugin, ChangeDataHolderEvent.ValueChange::class.java, listener)
    }