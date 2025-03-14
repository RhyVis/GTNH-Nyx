package rhynia.nyx.init

import gregtech.api.GregTechAPI
import net.minecraft.item.ItemStack
import rhynia.nyx.Config
import rhynia.nyx.Log
import rhynia.nyx.MOD_ID
import rhynia.nyx.MOD_NAME
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
import rhynia.nyx.common.tile.multi.process.NyxMTEProcessingComplex

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
  lateinit var ProcessingComplex: ItemStack

  // endregion

  private val offset by lazy { Config.MTE_ID_OFFSET }

  override fun load() {
    checkOccupation()
    initializeMachineClass()
    initializeItemList()
  }

  private fun checkOccupation() {
    for (i in (offset + 1)..(offset + 40)) {
      if (GregTechAPI.METATILEENTITIES[i] != null) {
        val mte = GregTechAPI.METATILEENTITIES[i]

        Log.warn("=========================================")
        Log.warn("           ID CRASH DETECTED             ")
        Log.warn("Seems that there's a conflict with ID $i")
        Log.warn("$MOD_NAME will preserve ${(offset + 1)..(offset + 40)} for machine ids")
        Log.warn("Now ID $i is occupied by ${mte.localName} form ${mte.javaClass}")
        Log.warn("If this is an MTE from GT:NH, report this to $MOD_NAME's author")
        Log.warn("If from another self-installed mod, you can change the offset in config")
        Log.warn("It's named $MOD_ID.cfg in your config folder")
        Log.warn("=========================================")

        throw IllegalStateException("ID $i preserved by $MOD_NAME is occupied by ${mte.localName}")
      }
    }
  }

  // spotless:off
  private fun initializeMachineClass() {
    AstralForge =
      NyxMTEAstralForge(offset + 1, "MTEAstralForge", "星辉锻造台").getStackForm(1)
    AtomMacro =
      NyxMTEAtomMacro(offset + 2, "MTEUltimateHeater", "粒子宏").getStackForm(1)
    AssemblyMatrix =
      NyxMTEAssemblyMatrix(offset + 3, "MTEAssemblyMatrix", "组装矩阵").getStackForm(1)
    KelvinTransformField =
      NyxMTEKelvinTransformField(offset + 4, "MTEKelvinTransformField", "开尔文变换场").getStackForm(1)
    SelectedEnergyGenerator =
      NyxMTESelectedEnergyGenerator(offset + 5, "MTESelectedEnergyGenerator", "能量发生器").getStackForm(1)
    Creator =
      NyxMTECreator(offset + 6, "MTECreator", "逆向奇点").getStackForm(1)
    EyeOfUltimate =
      NyxMTEEyeOfUltimate(offset + 7, "MTEEyeOfUltimate", "终极之眼").getStackForm(1)
    DenseEndpoint =
      NyxMTEDenseEndpoint(offset + 8, "MTEDenseEndpoint", "致密极点").getStackForm(1)
    ProcessingComplex =
      NyxMTEProcessingComplex(offset + 9, "MTEProcessingComplex", "处理矩阵").getStackForm(1)

    InfiniteLiquidAirHatch =
      NyxHatchLiquidAir(offset + 21, "MTEInfiniteLiquidAirHatch", "无限液气仓", 10).getStackForm(1)
    InfiniteDistilledWaterHatch =
      NyxHatchDistilledWater(offset + 22, "MTEInfiniteDistilledWaterHatch", "无限蒸馏仓", 9).getStackForm(1)
    InfiniteLavaHatch =
      NyxHatchLava(offset + 23, "MTEInfiniteLavaHatch", "无限岩浆仓", 8).getStackForm(1)
    InfiniteOilHatch =
      NyxHatchOil(offset + 24, "MTEInfiniteOilHatch", "无限石油仓", 8).getStackForm(1)
    InfiniteLubricantHatch =
      NyxHatchLubricant(offset + 25, "MTEInfiniteLubricantHatch", "无限润滑油仓", 10).getStackForm(1)
    InfiniteSteamHatch =
      NyxHatchSteam(offset + 26, "MTEInfiniteSteamHatch", "无限蒸汽仓", 8).getStackForm(1)

    ZeroGenerator =
      NyxMTEZeroGenerator(offset + 27, "MTEZeroGenerator", "零点发生器", 14).getStackForm(1)

    HumongousCalibrationHalfInputHatch =
      NyxHatchHumongousCalibrationHalfInput(offset + 28, "MTECalibrationHalfInputHatch", "鸿蒙半标定仓").getStackForm(1)
    HumongousCalibrationInputHatch =
      NyxMTEHatchHumongousCalibrationInput(offset + 29, "MTECalibrationInputHatch", "鸿蒙标定仓").getStackForm(1)
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
    NyxItemList.ProcessingComplex.set(ProcessingComplex)

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
