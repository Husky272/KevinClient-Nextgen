package kevin.event.struct

/**
 * 继承了这个class的Event将拥有一个cancelEvent的函数，也就是这个Event可以被取消。
 */
open class CancellableEvent : Event(){
    var isCancelled: Boolean = false
        protected set
    /**
     * Cancel the event
     */
    fun cancelEvent() {
        isCancelled = true
    }
}