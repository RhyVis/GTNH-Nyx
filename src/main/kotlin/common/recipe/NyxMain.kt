@file:Suppress("ktlint:standard:filename")

package rhynia.nyx.common.recipe

import gregtech.api.enums.Materials
import gregtech.api.recipe.RecipeMaps
import rhynia.nyx.api.enums.RecipeValues.INGOT
import rhynia.nyx.api.enums.ref.SolderMaterial
import rhynia.nyx.api.enums.ref.SuperConductorPart
import rhynia.nyx.api.enums.ref.Tier
import rhynia.nyx.api.recipe.RecipePool
import rhynia.nyx.api.recipe.dsl.withRecipeMap
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.config.ConfigMachine

class NyxMainRecipes : RecipePool() {
    override fun loadRecipes() {
        withRecipeMap(RecipeMaps.assemblerRecipes) {
            newRecipeIf(ConfigMachine.MTE_COPIER) {
                itemInputs(
                    Tier.MV.getHull(1),
                    Tier.MV.getCircuit(1),
                    Tier.MV.getComponent(Tier.Component.Emitter, 1),
                    Materials.Aluminium.getPlates(4),
                    NyxMaterials.Restone.getDust(16),
                    ic(2),
                )
                fluidInputs(
                    SolderMaterial.T1.getFluidStack(2 * INGOT),
                )
                itemOutputs(
                    NyxItemList.ControllerCopier.get(1),
                )
                eut(Tier.MV)
                durSec(4)
            }
            newRecipeIf(ConfigMachine.MTE_PROXY) {
                itemInputs(
                    Tier.HV.getHull(6),
                    Tier.HV.getCircuit(6),
                    Tier.HV.getComponent(Tier.Component.Sensor, 6),
                    Materials.Titanium.getPlates(6),
                    SuperConductorPart.EV.getWire(6),
                    ic(6),
                )
                fluidInputs(
                    SolderMaterial.T1.getFluidStack(6 * INGOT),
                )
                itemOutputs(
                    NyxItemList.ControllerProxy.get(1),
                )
                eut(Tier.HV)
                durSec(6)
            }
        }
    }
}
