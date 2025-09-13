package rhynia.nyx.api.process

import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.check.CheckRecipeResult
import rhynia.nyx.api.interfaces.mte.ProcessInfo

open class NyxProcessingLogic : ProcessingLogic() {
    fun setOverclock(type: OverclockType): NyxProcessingLogic =
        apply {
            setOverclock(type.timeDec, type.powerInc)
        }

    init {
        setUnlimitedTierSkips()
    }
}

class NyxAutoProcessingLogic(
    private val info: ProcessInfo,
) : NyxProcessingLogic() {
    override fun process(): CheckRecipeResult {
        setEuModifier(info.rEuModifier)
        setSpeedBonus(info.rTimeModifier)
        setOverclock(info.rOverclockType)
        return super.process()
    }

    init {
        setMaxParallelSupplier(info::rMaxParallel)
    }
}
