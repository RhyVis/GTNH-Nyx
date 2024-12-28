package vis.rhynia.nova.api.process.logic

import gregtech.api.logic.ProcessingLogic
import gregtech.api.util.GTRecipe
import gregtech.api.util.ParallelHelper

open class NovaProcessingLogic : ProcessingLogic() {
  protected override fun createParallelHelper(recipe: GTRecipe): ParallelHelper {
    return NovaParallelHelper()
        .setRecipe(recipe)
        .setItemInputs(*inputItems)
        .setFluidInputs(*inputFluids)
        .setAvailableEUt(availableVoltage * availableAmperage)
        .setMachine(machine, protectItems, protectFluids)
        .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
        .setMaxParallel(maxParallel)
        .setEUtModifier(euModifier)
        .enableBatchMode(batchSize)
        .setConsumption(true)
        .setOutputCalculation(true)
  }
}
