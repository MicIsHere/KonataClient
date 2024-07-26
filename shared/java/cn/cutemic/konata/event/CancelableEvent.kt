package cn.cutemic.konata.event

open class CancelableEvent : Event {
    var isCanceled = false
        private set

    fun cancel() {
        isCanceled = true
    }
}
