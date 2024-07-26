package cn.cutemic.konata.features.command

abstract class Command(var name: String) {
    abstract fun execute(args: Array<String>)
}