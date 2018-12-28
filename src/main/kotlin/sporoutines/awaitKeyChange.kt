package sporoutines

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimaps
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.value.BaseValue
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.data.ChangeDataHolderEvent
import org.spongepowered.api.event.filter.cause.First
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KeyChangeListener {

    data class KeyContinuation<out T>(val expected: T, val continuation: Continuation<Unit>)

    internal val keyMap = Multimaps.synchronizedSetMultimap(
        HashMultimap.create<Key<out BaseValue<out Any>>, KeyContinuation<Any>>()
    )

    @Listener
    fun onValueChange(event: ChangeDataHolderEvent.ValueChange, @First player: Player) {
        for (value in event.endResult.successfulData) {
            for (cont in keyMap[value.key]) {
                if (cont.expected == value.get()) {
                    cont.continuation.resume(Unit)
                    keyMap.remove(value.key, cont)
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
suspend fun <T> Player.awaitKeyChange(key: Key<out BaseValue<T>>, expected: T) = suspendCoroutine<Unit> {
    KeyChangeListener.keyMap.put(
        key,
        KeyChangeListener.KeyContinuation(expected, it) as KeyChangeListener.KeyContinuation<Any>
    )
}