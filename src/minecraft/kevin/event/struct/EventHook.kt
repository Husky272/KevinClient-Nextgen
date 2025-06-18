package kevin.event.struct

import kevin.event.struct.Event
import kevin.event.EventTarget
import kevin.event.Listenable
import java.lang.reflect.Method

internal open class EventHook(val eventClass: Listenable, val method: Method, eventTarget: EventTarget) {
    @JvmField
    val isIgnoreCondition = eventTarget.ignoreCondition

    @Throws(Throwable::class)
    open fun call(event: Event) {
        method.invoke(eventClass, event)
    }
}