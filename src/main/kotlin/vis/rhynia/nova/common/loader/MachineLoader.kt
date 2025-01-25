package vis.rhynia.nova.common.loader

import net.minecraft.item.ItemStack
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchDistilledWater
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchHumongousCalibrationHalfInput
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchHumongousCalibrationInput
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchLava
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchLiquidAir
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchLubricant
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchOil
import vis.rhynia.nova.common.tile.hatch.NovaMTEHatchSteam
import vis.rhynia.nova.common.tile.hatch.NovaMTEZeroGenerator
import vis.rhynia.nova.common.tile.multi.creation.NovaMTECreator
import vis.rhynia.nova.common.tile.multi.creation.NovaMTEEyeOfUltimate
import vis.rhynia.nova.common.tile.multi.generation.NovaMTESelectedEnergyGenerator
import vis.rhynia.nova.common.tile.multi.process.NovaMTEAssemblyMatrix
import vis.rhynia.nova.common.tile.multi.process.NovaMTEAstralForge
import vis.rhynia.nova.common.tile.multi.process.NovaMTEAtomMacro
import vis.rhynia.nova.common.tile.multi.process.NovaMTEDenseEndpoint
import vis.rhynia.nova.common.tile.multi.process.NovaMTEKelvinTransformField

object MachineLoader {

  // region sig Machine
  var InfiniteLiquidAirHatch: ItemStack? = null
  var InfiniteDistilledWaterHatch: ItemStack? = null
  var InfiniteLavaHatch: ItemStack? = null
  var InfiniteOilHatch: ItemStack? = null
  var InfiniteLubricantHatch: ItemStack? = null
  var InfiniteSteamHatch: ItemStack? = null
  var ZeroGenerator: ItemStack? = null
  var HumongousCalibrationInputHatch: ItemStack? = null
  var HumongousCalibrationHalfInputHatch: ItemStack? = null
  // endregion

  // region multi Machine controller
  var AstralForge: ItemStack? = null
  var AtomMacro: ItemStack? = null
  var AssemblyMatrix: ItemStack? = null
  var KelvinTransformField: ItemStack? = null
  var SelectedEnergyGenerator: ItemStack? = null
  var Creator: ItemStack? = null
  var EyeOfUltimate: ItemStack? = null
  var DenseEndpoint: ItemStack? = null

  fun load() {
    initializeMachineClass()
    initializeItemList()
  }

  // spotless:off
  private fun initializeMachineClass() {
    AstralForge =
      NovaMTEAstralForge(17501, "MTEAstralForge", "星辉锻造台").getStackForm(1)
    AtomMacro =
      NovaMTEAtomMacro(17502, "MTEUltimateHeater", "粒子宏").getStackForm(1)
    AssemblyMatrix =
      NovaMTEAssemblyMatrix(17503, "MTEAssemblyMatrix", "组装矩阵").getStackForm(1)
    KelvinTransformField =
      NovaMTEKelvinTransformField(17504, "MTEKelvinTransformField", "开尔文变换场").getStackForm(1)
    SelectedEnergyGenerator =
      NovaMTESelectedEnergyGenerator(17505, "MTESelectedEnergyGenerator", "能量发生器").getStackForm(1)
    Creator =
      NovaMTECreator(17506, "MTECreator", "逆向奇点").getStackForm(1)
    EyeOfUltimate =
      NovaMTEEyeOfUltimate(17507, "MTEEyeOfUltimate", "终极之眼").getStackForm(1)
    DenseEndpoint =
      NovaMTEDenseEndpoint(17508, "MTEDenseEndpoint", "致密极点").getStackForm(1)

    InfiniteLiquidAirHatch =
      NovaMTEHatchLiquidAir(17401, "MTEInfiniteLiquidAirHatch", "无限液气仓", 10).getStackForm(1)
    InfiniteDistilledWaterHatch =
      NovaMTEHatchDistilledWater(17402, "MTEInfiniteDistilledWaterHatch", "无限蒸馏仓", 9).getStackForm(1)
    InfiniteLavaHatch =
      NovaMTEHatchLava(17403, "MTEInfiniteLavaHatch", "无限岩浆仓", 8).getStackForm(1)
    InfiniteOilHatch =
      NovaMTEHatchOil(17404, "MTEInfiniteOilHatch", "无限石油仓", 8).getStackForm(1)
    InfiniteLubricantHatch =
      NovaMTEHatchLubricant(17405, "MTEInfiniteLubricantHatch", "无限润滑油仓", 10).getStackForm(1)
    InfiniteSteamHatch =
      NovaMTEHatchSteam(17406, "MTEInfiniteSteamHatch", "无限蒸汽仓", 8).getStackForm(1)

    ZeroGenerator =
      NovaMTEZeroGenerator(17407, "MTEZeroGenerator", "零点发生器", 14).getStackForm(1)

    HumongousCalibrationHalfInputHatch =
      NovaMTEHatchHumongousCalibrationHalfInput(17408, "MTECalibrationHalfInputHatch", "鸿蒙半标定仓").getStackForm(1)
    HumongousCalibrationInputHatch =
      NovaMTEHatchHumongousCalibrationInput(17409, "MTECalibrationInputHatch", "鸿蒙标定仓").getStackForm(1)
  }
  // spotless:on

  private fun initializeItemList() {
    NovaItemList.AstralForge.set(AstralForge)
    NovaItemList.AtomMacro.set(AtomMacro)
    NovaItemList.AssemblyMatrix.set(AssemblyMatrix)
    NovaItemList.KelvinTransformField.set(KelvinTransformField)
    NovaItemList.SelectedEnergyGenerator.set(SelectedEnergyGenerator)
    NovaItemList.Creator.set(Creator)
    NovaItemList.EyeOfUltimate.set(EyeOfUltimate)
    NovaItemList.DenseEndpoint.set(DenseEndpoint)

    NovaItemList.InfiniteLiquidAirHatch.set(InfiniteLiquidAirHatch)
    NovaItemList.InfiniteDistilledWaterHatch.set(InfiniteDistilledWaterHatch)
    NovaItemList.InfiniteLavaHatch.set(InfiniteLavaHatch)
    NovaItemList.InfiniteOilHatch.set(InfiniteOilHatch)
    NovaItemList.InfiniteLubricantHatch.set(InfiniteLubricantHatch)
    NovaItemList.InfiniteSteamHatch.set(InfiniteSteamHatch)

    NovaItemList.ZeroGenerator.set(ZeroGenerator)

    NovaItemList.HumongousCalibrationHalfInputHatch.set(HumongousCalibrationHalfInputHatch)
    NovaItemList.HumongousCalibrationInputHatch.set(HumongousCalibrationInputHatch)
  }
}
