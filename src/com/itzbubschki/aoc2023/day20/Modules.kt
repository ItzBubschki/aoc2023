package com.itzbubschki.aoc2023.day20
sealed class Module(val moduleName: String, val outgoing: List<String>) {
    val pulseCount = mutableMapOf(Pulse.LOW to 0L, Pulse.HIGH to 0L)

    open fun process(pulse: Pulse, source: String): List<Signal> = emptyList()
}

// Button module
//   push -> send low to broadcaster
class Button(moduleName: String, outgoing: List<String>) : Module(
    moduleName, outgoing
) {
    fun process(): List<Signal> =
        this.process(Pulse.LOW, "VOID")

    override fun process(pulse: Pulse, source: String): List<Signal> {
        pulseCount[pulse] = pulseCount.getValue(pulse) + outgoing.size
        return outgoing.map { out ->
            Signal(moduleName, pulse, out)
        }
    }
}

// Receiver makes nothing
class Receiver(moduleName: String, outgoing: List<String>) : Module(
    moduleName, outgoing
) {
    private var receivedLow = false

    override fun process(pulse: Pulse, source: String): List<Signal> {
        if (pulse == Pulse.LOW) receivedLow = true
        return emptyList()
    }
}

// Broadcaster module
//   sends received pulse to all destination modules
class Broadcaster(moduleName: String, outgoing: List<String>) : Module(
    moduleName, outgoing
) {
    override fun process(pulse: Pulse, source: String): List<Signal> {
        pulseCount[pulse] = pulseCount.getValue(pulse) + outgoing.size

        return outgoing.map { out ->
            Signal(moduleName, pulse, out)
        }
    }
}

// FlipFlop (prefix %)
//   initially: memory=off
//   high -> NOTHING
//   low  -> memory = !memory
class FlipFlop(moduleName: String, outgoing: List<String>) : Module(
    moduleName, outgoing
) {
    private var state = false

    override fun process(pulse: Pulse, source: String): List<Signal> {

        return when (pulse) {
            Pulse.HIGH -> emptyList()
            Pulse.LOW -> {
                val next = if (state) Pulse.LOW else Pulse.HIGH
                pulseCount[next] = pulseCount.getValue(next) + outgoing.size
                state = !state
                outgoing.map { out ->
                    Signal(moduleName, next, out)
                }
            }
        }
    }
}

// Conjunction (prefix &)
//   remember most recent pulse from each input module
//   initially: memory=low for all modules
//   memory[input module] = pulse from input module
//   all pulses == high -> send low
//                     else send high
class Conjunction(moduleName: String, outgoing: List<String>) : Module(
    moduleName, outgoing
) {
    private val pulseMemory = mutableMapOf<String, Pulse>()
    var incoming: List<String> = emptyList()
        set(inMods) {
            field = inMods
            inMods.forEach { pulseMemory[it] = Pulse.LOW }
        }

    override fun process(pulse: Pulse, source: String): List<Signal> {

        pulseMemory[source] = pulse
        val prevIncomingPulses = incoming.map { pulseMemory.getValue(it) }
        val allHigh = prevIncomingPulses.all { it == Pulse.HIGH }
        val next = if (allHigh) Pulse.LOW else Pulse.HIGH
        pulseCount[next] = pulseCount.getValue(next) + outgoing.size

        return outgoing.map { out ->
            Signal(moduleName, next, out)
        }
    }
}