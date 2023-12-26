package com.itzbubschki.aoc2023.day20

import com.itzbubschki.aoc2023.utils.println
import com.itzbubschki.aoc2023.utils.product
import com.itzbubschki.aoc2023.utils.readInput

val input = readInput("Day20")

fun main() {
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Long {
    val scheduler = Scheduler(input)
    repeat(1000) {
        scheduler.pressButton()
    }
    val x = scheduler.getPulseCounts()
    val y = x.map { it.getValue(Pulse.LOW) to it.getValue(Pulse.HIGH) }
    val lowCount = y.sumOf { it.first }
    val highCount = y.sumOf { it.second }
    val res = lowCount * highCount
    return res
}

fun part2(input: List<String>): Long {
    val scheduler = Scheduler(input)
    while (true) {
        scheduler.pressButton()
        if (scheduler.receiverGotLow()) break
    }
    val x = scheduler.getCycles()
    return x.product()
}

class Scheduler(val input: List<String>) {
    private var modules = mutableMapOf<String, Module>()
    private val button: Button
    private val receiver: Receiver?
    private var receiverIngoing: String = ""
    private var rxIndirectInputModules: List<String> = emptyList()
    private var rxIndirectCycles: MutableMap<String, Long> = mutableMapOf()
    private var btnPressed = 0

    init {
        val regex = """([&%]?)(\w+) -> (.*)""".toRegex()
        input.map { line ->
            val matchResult = regex.find(line) ?: error("Unexpected input line '$line'")

            val (type, moduleName, outgoingText) = matchResult.destructured
            val outgoing = outgoingText.split(", ")
            when (type) {
                "%" -> modules.put(moduleName, FlipFlop(moduleName, outgoing))
                "&" -> modules.put(moduleName, Conjunction(moduleName, outgoing))
                else -> {
                    if (moduleName == "broadcaster") {
                        modules[moduleName] = Broadcaster(moduleName, outgoing)
                        modules.put("button", Button("button", listOf(moduleName)))
                    } else {
                        modules.put(moduleName, Receiver(moduleName, outgoing))
                    }
                }
            }
        }
        button = (modules.values.firstOrNull { it is Button } ?: error("no Button found")) as Button
        val incomers = modules.entries.mapNotNull { mod ->
            mod.value.outgoing.map { con -> con to mod.key }.takeIf { it.isNotEmpty() }
        }.flatten().groupBy({ it.first }, { it.second })
        incomers.forEach {
            val module = modules.getOrPut(it.key) { Receiver(it.key, emptyList()) }
            if (module is Conjunction) {
                module.incoming = it.value
            }
        }
        receiver = modules.values.firstOrNull { it is Receiver } as Receiver?
        if (receiver != null) {
            val rxInput = modules.values.find { receiver.moduleName in it.outgoing }
            check(rxInput is Conjunction)
            receiverIngoing = rxInput.moduleName
            rxIndirectInputModules = rxInput.incoming
        }
    }

    fun getPulseCounts() =
        modules.map { it.value.pulseCount }

    fun receiverGotLow(): Boolean =
        rxIndirectCycles.size == rxIndirectInputModules.size    // not all needed cycles detected

    fun getCycles(): List<Long> =
        rxIndirectCycles.values.toList()

    fun pressButton() {
        val signals = button.process()
        val queue = ArrayDeque(signals)
        btnPressed++
        var rounds = 0
        while (queue.isNotEmpty()) {
            val signal = queue.removeFirst()
            rounds++
            val mod = modules.getValue(signal.target)
            if (signal.source in rxIndirectInputModules && signal.target == receiverIngoing && signal.pulse == Pulse.HIGH) {
                if (!rxIndirectCycles.containsKey(signal.source)) {
                    rxIndirectCycles[signal.source] = btnPressed.toLong()
                }
            }
            queue += mod.process(signal.pulse, signal.source)
        }
    }
}

enum class Pulse(val text: String) {
    LOW("-low"),
    HIGH("-high")
}

data class Signal(val source: String, val pulse: Pulse, val target: String)

