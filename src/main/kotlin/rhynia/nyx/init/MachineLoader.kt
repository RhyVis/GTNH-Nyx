package rhynia.nyx.init

import net.minecraft.item.ItemStack
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.tile.hatch.NyxHatchDistilledWater
import rhynia.nyx.common.tile.hatch.NyxHatchHumongousCalibrationHalfInput
import rhynia.nyx.common.tile.hatch.NyxHatchLava
import rhynia.nyx.common.tile.hatch.NyxHatchLiquidAir
import rhynia.nyx.common.tile.hatch.NyxHatchLubricant
import rhynia.nyx.common.tile.hatch.NyxHatchOil
import rhynia.nyx.common.tile.hatch.NyxHatchSteam
import rhynia.nyx.common.tile.hatch.NyxMTEHatchHumongousCalibrationInput
import rhynia.nyx.common.tile.hatch.NyxMTEZeroGenerator
import rhynia.nyx.common.tile.multi.creation.NyxMTECreator
import rhynia.nyx.common.tile.multi.creation.NyxMTEEyeOfUltimate
import rhynia.nyx.common.tile.multi.generation.NyxMTESelectedEnergyGenerator
import rhynia.nyx.common.tile.multi.process.NyxMTEAssemblyMatrix
import rhynia.nyx.common.tile.multi.process.NyxMTEAstralForge
import rhynia.nyx.common.tile.multi.process.NyxMTEAtomMacro
import rhynia.nyx.common.tile.multi.process.NyxMTEDenseEndpoint
import rhynia.nyx.common.tile.multi.process.NyxMTEKelvinTransformField

object MachineLoader : Loader {

  // region sig Machine

  lateinit var InfiniteLiquidAirHatch: ItemStack
  lateinit var InfiniteDistilledWaterHatch: ItemStack
  lateinit var InfiniteLavaHatch: ItemStack
  lateinit var InfiniteOilHatch: ItemStack
  lateinit var InfiniteLubricantHatch: ItemStack
  lateinit var InfiniteSteamHatch: ItemStack
  lateinit var ZeroGenerator: ItemStack
  lateinit var HumongousCalibrationInputHatch: ItemStack
  lateinit var HumongousCalibrationHalfInputHatch: ItemStack

  // endregion

  // region multi Machine controller

  lateinit var AstralForge: ItemStack
  lateinit var AtomMacro: ItemStack
  lateinit var AssemblyMatrix: ItemStack
  lateinit var KelvinTransformField: ItemStack
  lateinit var SelectedEnergyGenerator: ItemStack
  lateinit var Creator: ItemStack
  lateinit var EyeOfUltimate: ItemStack
  lateinit var DenseEndpoint: ItemStack

  // endregion

  override fun load() {
    initializeMachineClass()
    initializeItemList()
  }

  // spotless:off
  private fun initializeMachineClass() {
    AstralForge =
      NyxMTEAstralForge(17501, "MTEAstralForge", "星辉锻造台").getStackForm(1)
    AtomMacro =
      NyxMTEAtomMacro(17502, "MTEUltimateHeater", "粒子宏").getStackForm(1)
    AssemblyMatrix =
      NyxMTEAssemblyMatrix(17503, "MTEAssemblyMatrix", "组装矩阵").getStackForm(1)
    KelvinTransformField =
      NyxMTEKelvinTransformField(17504, "MTEKelvinTransformField", "开尔文变换场").getStackForm(1)
    SelectedEnergyGenerator =
      NyxMTESelectedEnergyGenerator(17505, "MTESelectedEnergyGenerator", "能量发生器").getStackForm(1)
    Creator =
      NyxMTECreator(17506, "MTECreator", "逆向奇点").getStackForm(1)
    EyeOfUltimate =
      NyxMTEEyeOfUltimate(17507, "MTEEyeOfUltimate", "终极之眼").getStackForm(1)
    DenseEndpoint =
      NyxMTEDenseEndpoint(17508, "MTEDenseEndpoint", "致密极点").getStackForm(1)

    InfiniteLiquidAirHatch =
      NyxHatchLiquidAir(17401, "MTEInfiniteLiquidAirHatch", "无限液气仓", 10).getStackForm(1)
    InfiniteDistilledWaterHatch =
      NyxHatchDistilledWater(17402, "MTEInfiniteDistilledWaterHatch", "无限蒸馏仓", 9).getStackForm(1)
    InfiniteLavaHatch =
      NyxHatchLava(17403, "MTEInfiniteLavaHatch", "无限岩浆仓", 8).getStackForm(1)
    InfiniteOilHatch =
      NyxHatchOil(17404, "MTEInfiniteOilHatch", "无限石油仓", 8).getStackForm(1)
    InfiniteLubricantHatch =
      NyxHatchLubricant(17405, "MTEInfiniteLubricantHatch", "无限润滑油仓", 10).getStackForm(1)
    InfiniteSteamHatch =
      NyxHatchSteam(17406, "MTEInfiniteSteamHatch", "无限蒸汽仓", 8).getStackForm(1)

    ZeroGenerator =
      NyxMTEZeroGenerator(17407, "MTEZeroGenerator", "零点发生器", 14).getStackForm(1)

    HumongousCalibrationHalfInputHatch =
      NyxHatchHumongousCalibrationHalfInput(17408, "MTECalibrationHalfInputHatch", "鸿蒙半标定仓").getStackForm(1)
    HumongousCalibrationInputHatch =
      NyxMTEHatchHumongousCalibrationInput(17409, "MTECalibrationInputHatch", "鸿蒙标定仓").getStackForm(1)
  }
  // spotless:on

  private fun initializeItemList() {
    NyxItemList.AstralForge.set(AstralForge)
    NyxItemList.AtomMacro.set(AtomMacro)
    NyxItemList.AssemblyMatrix.set(AssemblyMatrix)
    NyxItemList.KelvinTransformField.set(KelvinTransformField)
    NyxItemList.SelectedEnergyGenerator.set(SelectedEnergyGenerator)
    NyxItemList.Creator.set(Creator)
    NyxItemList.EyeOfUltimate.set(EyeOfUltimate)
    NyxItemList.DenseEndpoint.set(DenseEndpoint)

    NyxItemList.InfiniteLiquidAirHatch.set(InfiniteLiquidAirHatch)
    NyxItemList.InfiniteDistilledWaterHatch.set(InfiniteDistilledWaterHatch)
    NyxItemList.InfiniteLavaHatch.set(InfiniteLavaHatch)
    NyxItemList.InfiniteOilHatch.set(InfiniteOilHatch)
    NyxItemList.InfiniteLubricantHatch.set(InfiniteLubricantHatch)
    NyxItemList.InfiniteSteamHatch.set(InfiniteSteamHatch)

    NyxItemList.ZeroGenerator.set(ZeroGenerator)

    NyxItemList.HumongousCalibrationHalfInputHatch.set(HumongousCalibrationHalfInputHatch)
    NyxItemList.HumongousCalibrationInputHatch.set(HumongousCalibrationInputHatch)
  }
}
