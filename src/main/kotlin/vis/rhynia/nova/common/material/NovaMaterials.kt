package vis.rhynia.nova.common.material

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

  val Null = SimpleMaterial(0, "null", "空", shortArrayOf(0, 0, 0, 255))

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

  /**
   * private const val OFFSET_ELEMENT = 23000 private const val OFFSET_MIXTURE = 23100 private const
   * val OFFSET_PRODUCT = 23200
   *
   * private val GenFeaturesUniversal = Werkstoff.GenerationFeatures() private val
   * GenFeaturesDustMolten = Werkstoff.GenerationFeatures().disable().onlyDust().addMolten()
   *
   * // Astrium val Astrium: Werkstoff = Werkstoff( shortArrayOf(30, 144, 252, 255), "Astrium",
   * "Aμ", Werkstoff.Stats() .setProtons(170) .setMass(452) .setBlastFurnace(true)
   * .setMeltingPoint(8500), Werkstoff.Types.ELEMENT, GenFeaturesDustMolten, OFFSET_ELEMENT + 1,
   * TextureSet.SET_GEM_HORIZONTAL)
   *
   * // Astrium Infinity val AstriumInfinity: Werkstoff = Werkstoff( shortArrayOf(0, 191, 255, 255),
   * "AstriumInfinity", "Aμⁿ", Werkstoff.Stats() .setProtons(191) .setMass(510)
   * .setBlastFurnace(true) .setMeltingPoint(12560), Werkstoff.Types.ELEMENT, GenFeaturesDustMolten,
   * OFFSET_ELEMENT + 2, TextureSet.SET_GEM_VERTICAL)
   *
   * // Astrium Magic val AstriumMagic: Werkstoff = Werkstoff( shortArrayOf(0, 32, 178, 170),
   * "AstriumMagic", "AμMa",
   * Werkstoff.Stats().setProtons(170).setMass(475).setBlastFurnace(true).setMeltingPoint(7),
   * Werkstoff.Types.ELEMENT, GenFeaturesDustMolten, OFFSET_ELEMENT + 3, TextureSet.SET_DIAMOND)
   *
   * // Primoium val Primoium = Werkstoff( shortArrayOf(0x87.toShort(), 0xce.toShort(),
   * 0xeb.toShort()), "Primoium", "Pr", Werkstoff.Stats() .setProtons(145) .setMass(385)
   * .setBlastFurnace(true) .setMeltingPoint(7250), Werkstoff.Types.ELEMENT,
   * Werkstoff.GenerationFeatures().onlyDust().addMolten(), OFFSET_ELEMENT + 4,
   * TextureSet.SET_SHINY)
   *
   * // Originium val Originium = Werkstoff( shortArrayOf(0xda.toShort(), 0xa5.toShort(),
   * 0x20.toShort()), "Originium", "Or*", Werkstoff.Stats() .setProtons(165) .setMass(445)
   * .setBlastFurnace(true) .setMeltingPoint(8540), Werkstoff.Types.ELEMENT,
   * Werkstoff.GenerationFeatures().onlyDust().addMolten(), OFFSET_ELEMENT + 5,
   * TextureSet.SET_SHINY)
   *
   * val AstralCatalystBase = Werkstoff( shortArrayOf(0x48, 0x3d, 0x8b), "AstralCatalystBase",
   * subscriptNumbers("Aμⁿ2If3"), Werkstoff.Stats(), Werkstoff.Types.COMPOUND,
   * Werkstoff.GenerationFeatures().disable().onlyDust().addCells().enforceUnification(),
   * OFFSET_MIXTURE + 1, TextureSet.SET_FLUID)
   *
   * val AstralCatalystBaseExcited = Werkstoff( shortArrayOf(0x6a, 0x5a, 0xcd),
   * "AstralCatalystBaseExcited", subscriptNumbers("(?Aμⁿ2If3?)*"), Werkstoff.Stats(),
   * Werkstoff.Types.COMPOUND, Werkstoff.GenerationFeatures().disable().addCells(), OFFSET_MIXTURE +
   * 2, TextureSet.SET_FLUID)
   *
   * val AstralCatalystReforged = Werkstoff( shortArrayOf(0x41, 0x69, 0xe1),
   * "AstralCatalystReforged", subscriptNumbers("Aμⁿ4?3"), Werkstoff.Stats(),
   * Werkstoff.Types.COMPOUND, Werkstoff.GenerationFeatures().disable().addCells(), OFFSET_MIXTURE +
   * 3, TextureSet.SET_FLUID)
   *
   * val AstralCatalystReforgedExcited = Werkstoff( shortArrayOf(0x41, 0x84, 0xe1),
   * "AstralCatalystReforgedExcited", subscriptNumbers("(Aμⁿ4?3)*"), Werkstoff.Stats(),
   * Werkstoff.Types.COMPOUND, Werkstoff.GenerationFeatures().disable().addCells(), OFFSET_MIXTURE +
   * 4, TextureSet.SET_FLUID)
   *
   * val AstralResidue = Werkstoff( shortArrayOf(0x19, 0x19, 0x70), "AstralResidue", "Aμ°",
   * Werkstoff.Stats(), Werkstoff.Types.ELEMENT,
   * Werkstoff.GenerationFeatures().disable().addCells(), OFFSET_PRODUCT + 1, TextureSet.SET_FLUID)
   *
   * val LapotronEnhancedFluid = Werkstoff( shortArrayOf(0x64, 0x95, 0xed), "LapotronEnhancedFluid",
   * "[-Lapo-Lapo-]", Werkstoff.Stats(), Werkstoff.Types.COMPOUND,
   * Werkstoff.GenerationFeatures().disable().addCells(), OFFSET_PRODUCT + 2, TextureSet.SET_FLUID)
   *
   * const val SC_FLUX_TT = "(?Aμⁿ2If3?)*12D*12M11If*10SpNt8In7Nq+6Nq*5Or5(⚷⚙⚷Ni4Ti6)4(✧◇✧)4" val
   * SuperconductorFluxRaw = Werkstoff( shortArrayOf(0x69, 0x69, 0x69), "SuperconductorFluxRaw",
   * subscriptNumbers(SC_FLUX_TT), Werkstoff.Stats().setMeltingPoint(3), Werkstoff.Types.MIXTURE,
   * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().enforceUnification(),
   * OFFSET_PRODUCT + 3, TextureSet.SET_MAGNETIC)
   *
   * val SuperconductorFlux = Werkstoff( shortArrayOf(0xC0, 0xC0, 0xC0), "SuperconductorFlux", "Sx",
   * Werkstoff.Stats().setMeltingPoint(1), Werkstoff.Types.ELEMENT,
   * Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(), OFFSET_PRODUCT + 4,
   * TextureSet.SET_FLUID)
   *
   * override fun run() { OrePrefixes.entries.forEach(GenFeaturesUniversal::addPrefix)
   * GenFeaturesUniversal.removePrefix(OrePrefixes.ore) }
   */
}
