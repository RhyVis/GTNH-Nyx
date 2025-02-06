package vis.rhynia.nova.common.material

import gregtech.api.enums.FluidState.GAS
import gregtech.api.enums.FluidState.LIQUID
import gregtech.api.enums.FluidState.MOLTEN
import gregtech.api.enums.FluidState.PLASMA
import gregtech.api.enums.TextureSet
import vis.rhynia.nova.Log
import vis.rhynia.nova.api.interfaces.Loader
import vis.rhynia.nova.common.material.generation.SimpleMaterial

@Suppress("SpellCheckingInspection")
object NovaMaterials : Loader {
  override fun load() {
    Log.info("Adding materials...")
  }

  val Null =
      SimpleMaterial(0, "null", "空", shortArrayOf(250, 250, 250, 255)).also {
        it.textureSet = TextureSet.SET_NONE
        it.protons = 999
        it.mass = 999
        it.addElementalTooltip("N")
        it.addExtraTooltip("这是虚空的元素")
        it.enableDusts()
        it.enableFluids(LIQUID to 1111, MOLTEN to 3333, GAS to 6666, PLASMA to 9999)
        it.enableGems()
        it.enableIngots()
        it.enablePlates()
        it.enableMisc()
      }

  val Astrium =
      SimpleMaterial(1, "Astrium", "星源", shortArrayOf(30, 144, 252, 255)).also {
        it.textureSet = TextureSet.SET_GEM_HORIZONTAL
        it.protons = 170
        it.mass = 452
        it.addElementalTooltip("Aμ")
        it.enableDusts()
        it.enableFluids(MOLTEN to 900, PLASMA to 12000)
      }

  val AstriumInfinity =
      SimpleMaterial(2, "AstriumInfinity", "星极", shortArrayOf(0, 191, 255, 255)).also {
        it.textureSet = TextureSet.SET_GEM_VERTICAL
        it.protons = 191
        it.mass = 510
        it.addElementalTooltip("Aμⁿ")
        it.enableDusts()
        it.enableFluids(MOLTEN to 12560)
      }

  val AstriumMagic =
      SimpleMaterial(3, "AstriumMagic", "星辉", shortArrayOf(0, 32, 178, 170)).also {
        it.textureSet = TextureSet.SET_DIAMOND
        it.protons = 170
        it.mass = 475
        it.addElementalTooltip("AμMa")
        it.enableDusts()
        it.enableFluids(MOLTEN to 7)
      }

  val Primoium =
      SimpleMaterial(8, "Primoium", "原石", shortArrayOf(0x87, 0xce, 0xeb)).also {
        it.textureSet = TextureSet.SET_SHINY
        it.protons = 145
        it.mass = 385
        it.addElementalTooltip("Pr")
        it.enableDusts()
        it.enableFluids(MOLTEN to 7250)
      }

  val Originium =
      SimpleMaterial(9, "Originium", "源石", shortArrayOf(0xda, 0xa5, 0x20)).also {
        it.textureSet = TextureSet.SET_SHINY
        it.protons = 165
        it.mass = 445
        it.addElementalTooltip("Or*")
        it.enableDusts()
        it.enableFluids(MOLTEN to 8540)
      }

  val AstralCatalystBase =
      SimpleMaterial(201, "AstralCatalystBase", "星体催化剂", shortArrayOf(0x48, 0x3d, 0x8b)).also {
        it.textureSet = TextureSet.SET_FLUID
        it.enableDusts()
        it.addElementalTooltip("Aμⁿ2If3")
        it.enableFluids(LIQUID to 340)
      }

  val AstralCatalystBaseExcited =
      SimpleMaterial(202, "AstralCatalystBaseExcited", "激发星体催化剂", shortArrayOf(0x6a, 0x5a, 0xcd))
          .also {
            it.textureSet = TextureSet.SET_FLUID
            it.addElementalTooltip("(?Aμⁿ2If3?)*")
            it.enableFluids(LIQUID to 18510)
          }

  val AstralCatalystReforged =
      SimpleMaterial(203, "AstralCatalystReforged", "重铸星体催化剂", shortArrayOf(0x41, 0x84, 0xe1))
          .also {
            it.textureSet = TextureSet.SET_FLUID
            it.addElementalTooltip("Aμⁿ4?3")
            it.enableFluids(LIQUID to 35000)
          }

  val AstralResidue =
      SimpleMaterial(204, "AstralResidue", "矮星物质", shortArrayOf(0x19, 0x19, 0x70)).also {
        it.textureSet = TextureSet.SET_FLUID
        it.addElementalTooltip("Aμ°")
        it.enableFluids(LIQUID to 0)
      }

  val LapotronEnhancedFluid =
      SimpleMaterial(401, "LapotronEnhancedFluid", "兰波顿聚能流体", shortArrayOf(0x64, 0x95, 0xed)).also {
        it.textureSet = TextureSet.SET_FLUID
        it.addElementalTooltip("[-Lapo-Lapo-]")
        it.enableFluids(LIQUID to 400)
      }

  val SuperconductorFluxRaw =
      SimpleMaterial(402, "SuperconductorFluxRaw", "粗制超导流体", shortArrayOf(0x69, 0x69, 0x69)).also {
        it.textureSet = TextureSet.SET_MAGNETIC
        it.addElementalTooltip("(?Aμⁿ2If3?)*12D*12M11If*10SpNt8In7Nq+6Nq*5Or5(⚷⚙⚷Ni4Ti6)4(✧◇✧)4")
        it.enableDusts()
        it.enableFluids(MOLTEN to 3)
      }

  val SuperconductorFlux =
      SimpleMaterial(403, "SuperconductorFlux", "超导流体", shortArrayOf(0xC0, 0xC0, 0xC0)).also {
        it.textureSet = TextureSet.SET_FLUID
        it.addElementalTooltip("[-Sx-]*")
        it.enableFluids(LIQUID to 3)
      }
}
