package rhynia.constellation.init

import net.minecraft.item.ItemStack
import rhynia.constellation.api.interfaces.Loader
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.tile.hatch.CelHatchDistilledWater
import rhynia.constellation.common.tile.hatch.CelHatchHumongousCalibrationHalfInput
import rhynia.constellation.common.tile.hatch.CelHatchLava
import rhynia.constellation.common.tile.hatch.CelHatchLiquidAir
import rhynia.constellation.common.tile.hatch.CelHatchLubricant
import rhynia.constellation.common.tile.hatch.CelHatchOil
import rhynia.constellation.common.tile.hatch.CelHatchSteam
import rhynia.constellation.common.tile.hatch.CelMTEHatchHumongousCalibrationInput
import rhynia.constellation.common.tile.hatch.CelMTEZeroGenerator
import rhynia.constellation.common.tile.multi.creation.CelMTECreator
import rhynia.constellation.common.tile.multi.creation.CelMTEEyeOfUltimate
import rhynia.constellation.common.tile.multi.generation.CelMTESelectedEnergyGenerator
import rhynia.constellation.common.tile.multi.process.CelMTEAssemblyMatrix
import rhynia.constellation.common.tile.multi.process.CelMTEAstralForge
import rhynia.constellation.common.tile.multi.process.CelMTEAtomMacro
import rhynia.constellation.common.tile.multi.process.CelMTEDenseEndpoint
import rhynia.constellation.common.tile.multi.process.CelMTEKelvinTransformField

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
      CelMTEAstralForge(17501, "MTEAstralForge", "星辉锻造台").getStackForm(1)
    AtomMacro =
      CelMTEAtomMacro(17502, "MTEUltimateHeater", "粒子宏").getStackForm(1)
    AssemblyMatrix =
      CelMTEAssemblyMatrix(17503, "MTEAssemblyMatrix", "组装矩阵").getStackForm(1)
    KelvinTransformField =
      CelMTEKelvinTransformField(17504, "MTEKelvinTransformField", "开尔文变换场").getStackForm(1)
    SelectedEnergyGenerator =
      CelMTESelectedEnergyGenerator(17505, "MTESelectedEnergyGenerator", "能量发生器").getStackForm(1)
    Creator =
      CelMTECreator(17506, "MTECreator", "逆向奇点").getStackForm(1)
    EyeOfUltimate =
      CelMTEEyeOfUltimate(17507, "MTEEyeOfUltimate", "终极之眼").getStackForm(1)
    DenseEndpoint =
      CelMTEDenseEndpoint(17508, "MTEDenseEndpoint", "致密极点").getStackForm(1)

    InfiniteLiquidAirHatch =
      CelHatchLiquidAir(17401, "MTEInfiniteLiquidAirHatch", "无限液气仓", 10).getStackForm(1)
    InfiniteDistilledWaterHatch =
      CelHatchDistilledWater(17402, "MTEInfiniteDistilledWaterHatch", "无限蒸馏仓", 9).getStackForm(1)
    InfiniteLavaHatch =
      CelHatchLava(17403, "MTEInfiniteLavaHatch", "无限岩浆仓", 8).getStackForm(1)
    InfiniteOilHatch =
      CelHatchOil(17404, "MTEInfiniteOilHatch", "无限石油仓", 8).getStackForm(1)
    InfiniteLubricantHatch =
      CelHatchLubricant(17405, "MTEInfiniteLubricantHatch", "无限润滑油仓", 10).getStackForm(1)
    InfiniteSteamHatch =
      CelHatchSteam(17406, "MTEInfiniteSteamHatch", "无限蒸汽仓", 8).getStackForm(1)

    ZeroGenerator =
      CelMTEZeroGenerator(17407, "MTEZeroGenerator", "零点发生器", 14).getStackForm(1)

    HumongousCalibrationHalfInputHatch =
      CelHatchHumongousCalibrationHalfInput(17408, "MTECalibrationHalfInputHatch", "鸿蒙半标定仓").getStackForm(1)
    HumongousCalibrationInputHatch =
      CelMTEHatchHumongousCalibrationInput(17409, "MTECalibrationInputHatch", "鸿蒙标定仓").getStackForm(1)
  }
  // spotless:on

  private fun initializeItemList() {
    CelItemList.AstralForge.set(AstralForge)
    CelItemList.AtomMacro.set(AtomMacro)
    CelItemList.AssemblyMatrix.set(AssemblyMatrix)
    CelItemList.KelvinTransformField.set(KelvinTransformField)
    CelItemList.SelectedEnergyGenerator.set(SelectedEnergyGenerator)
    CelItemList.Creator.set(Creator)
    CelItemList.EyeOfUltimate.set(EyeOfUltimate)
    CelItemList.DenseEndpoint.set(DenseEndpoint)

    CelItemList.InfiniteLiquidAirHatch.set(InfiniteLiquidAirHatch)
    CelItemList.InfiniteDistilledWaterHatch.set(InfiniteDistilledWaterHatch)
    CelItemList.InfiniteLavaHatch.set(InfiniteLavaHatch)
    CelItemList.InfiniteOilHatch.set(InfiniteOilHatch)
    CelItemList.InfiniteLubricantHatch.set(InfiniteLubricantHatch)
    CelItemList.InfiniteSteamHatch.set(InfiniteSteamHatch)

    CelItemList.ZeroGenerator.set(ZeroGenerator)

    CelItemList.HumongousCalibrationHalfInputHatch.set(HumongousCalibrationHalfInputHatch)
    CelItemList.HumongousCalibrationInputHatch.set(HumongousCalibrationInputHatch)
  }
}
