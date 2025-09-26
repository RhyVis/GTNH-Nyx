package rhynia.nyx.api.interfaces.mte

import rhynia.nyx.api.process.OverclockType

/**
 * Interface for machines that have processing information:
 * such as overclocking, EU modifier, time modifier, and max parallel processes.
 */
interface ProcessInfo {
    /**
     * The type of overclocking applied to this machine.
     */
    val rOverclockType: OverclockType
        get() = OverclockType.Normal

    /**
     * The eu cost modifier for this machine.
     */
    val rEuModifier
        get() = 1.0

    /**
     * The processing time modifier for this machine.
     */
    val rTimeModifier
        get() = 1.0

    /**
     * The maximum number of parallel processes this machine can handle.
     */
    val rMaxParallel
        get() = 1
}
