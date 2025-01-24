package vis.rhynia.nova.common.material

import bartworks.system.material.Werkstoff
import bartworks.util.BWUtil.subscriptNumbers
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.TextureSet

object NovaMaterial : Runnable {
  private const val OFFSET_ELEMENT = 23000
  private const val OFFSET_MIXTURE = 23100
  private const val OFFSET_PRODUCT = 23200

  private val GenFeaturesUniversal = Werkstoff.GenerationFeatures()
  private val GenFeaturesDustMolten =
      Werkstoff.GenerationFeatures().disable().onlyDust().addMolten()

  // Astrium
  val Astrium: Werkstoff =
      Werkstoff(
          shortArrayOf(30, 144, 252, 255),
          "Astrium",
          "Aμ",
          Werkstoff.Stats()
              .setProtons(170)
              .setMass(452)
              .setBlastFurnace(true)
              .setMeltingPoint(8500),
          Werkstoff.Types.ELEMENT,
          GenFeaturesDustMolten,
          OFFSET_ELEMENT + 1,
          TextureSet.SET_GEM_HORIZONTAL)

  // Astrium Infinity
  val AstriumInfinity: Werkstoff =
      Werkstoff(
          shortArrayOf(0, 191, 255, 255),
          "AstriumInfinity",
          "Aμⁿ",
          Werkstoff.Stats()
              .setProtons(191)
              .setMass(510)
              .setBlastFurnace(true)
              .setMeltingPoint(12560),
          Werkstoff.Types.ELEMENT,
          GenFeaturesDustMolten,
          OFFSET_ELEMENT + 2,
          TextureSet.SET_GEM_VERTICAL)

  // Astrium Magic
  val AstriumMagic: Werkstoff =
      Werkstoff(
          shortArrayOf(0, 32, 178, 170),
          "AstriumMagic",
          "AμMa",
          Werkstoff.Stats().setProtons(170).setMass(475).setBlastFurnace(true).setMeltingPoint(7),
          Werkstoff.Types.ELEMENT,
          GenFeaturesDustMolten,
          OFFSET_ELEMENT + 3,
          TextureSet.SET_DIAMOND)

  // Primoium
  val Primoium =
      Werkstoff(
          shortArrayOf(0x87.toShort(), 0xce.toShort(), 0xeb.toShort()),
          "Primoium",
          "Pr",
          Werkstoff.Stats()
              .setProtons(145)
              .setMass(385)
              .setBlastFurnace(true)
              .setMeltingPoint(7250),
          Werkstoff.Types.ELEMENT,
          Werkstoff.GenerationFeatures().onlyDust().addMolten(),
          OFFSET_ELEMENT + 4,
          TextureSet.SET_SHINY)

  // Originium
  val Originium =
      Werkstoff(
          shortArrayOf(0xda.toShort(), 0xa5.toShort(), 0x20.toShort()),
          "Originium",
          "Or*",
          Werkstoff.Stats()
              .setProtons(165)
              .setMass(445)
              .setBlastFurnace(true)
              .setMeltingPoint(8540),
          Werkstoff.Types.ELEMENT,
          Werkstoff.GenerationFeatures().onlyDust().addMolten(),
          OFFSET_ELEMENT + 5,
          TextureSet.SET_SHINY)

  val AstralCatalystBase =
      Werkstoff(
          shortArrayOf(0x48, 0x3d, 0x8b),
          "AstralCatalystBase",
          subscriptNumbers("Aμⁿ2If3"),
          Werkstoff.Stats(),
          Werkstoff.Types.COMPOUND,
          Werkstoff.GenerationFeatures().disable().onlyDust().addCells().enforceUnification(),
          OFFSET_MIXTURE + 1,
          TextureSet.SET_FLUID)

  val AstralCatalystBaseExcited =
      Werkstoff(
          shortArrayOf(0x6a, 0x5a, 0xcd),
          "AstralCatalystBaseExcited",
          subscriptNumbers("(?Aμⁿ2If3?)*"),
          Werkstoff.Stats(),
          Werkstoff.Types.COMPOUND,
          Werkstoff.GenerationFeatures().disable().addCells(),
          OFFSET_MIXTURE + 2,
          TextureSet.SET_FLUID)

  val AstralCatalystReforged =
      Werkstoff(
          shortArrayOf(0x41, 0x69, 0xe1),
          "AstralCatalystReforged",
          subscriptNumbers("Aμⁿ4?3"),
          Werkstoff.Stats(),
          Werkstoff.Types.COMPOUND,
          Werkstoff.GenerationFeatures().disable().addCells(),
          OFFSET_MIXTURE + 3,
          TextureSet.SET_FLUID)

  val AstralCatalystReforgedExcited =
      Werkstoff(
          shortArrayOf(0x41, 0x84, 0xe1),
          "AstralCatalystReforgedExcited",
          subscriptNumbers("(Aμⁿ4?3)*"),
          Werkstoff.Stats(),
          Werkstoff.Types.COMPOUND,
          Werkstoff.GenerationFeatures().disable().addCells(),
          OFFSET_MIXTURE + 4,
          TextureSet.SET_FLUID)

  val AstralResidue =
      Werkstoff(
          shortArrayOf(0x19, 0x19, 0x70),
          "AstralResidue",
          "Aμ°",
          Werkstoff.Stats(),
          Werkstoff.Types.ELEMENT,
          Werkstoff.GenerationFeatures().disable().addCells(),
          OFFSET_PRODUCT + 1,
          TextureSet.SET_FLUID)

  val LapotronEnhancedFluid =
      Werkstoff(
          shortArrayOf(0x64, 0x95, 0xed),
          "LapotronEnhancedFluid",
          "[-Lapo-Lapo-]",
          Werkstoff.Stats(),
          Werkstoff.Types.COMPOUND,
          Werkstoff.GenerationFeatures().disable().addCells(),
          OFFSET_PRODUCT + 2,
          TextureSet.SET_FLUID)

  const val SC_FLUX_TT = "(?Aμⁿ2If3?)*12D*12M11If*10SpNt8In7Nq+6Nq*5Or5(⚷⚙⚷Ni4Ti6)4(✧◇✧)4"
  val SuperconductorFluxRaw =
      Werkstoff(
          shortArrayOf(0x69, 0x69, 0x69),
          "SuperconductorFluxRaw",
          subscriptNumbers(SC_FLUX_TT),
          Werkstoff.Stats().setMeltingPoint(3),
          Werkstoff.Types.MIXTURE,
          Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().enforceUnification(),
          OFFSET_PRODUCT + 3,
          TextureSet.SET_MAGNETIC)

  val SuperconductorFlux =
      Werkstoff(
          shortArrayOf(0xC0, 0xC0, 0xC0),
          "SuperconductorFlux",
          "Sx",
          Werkstoff.Stats().setMeltingPoint(1),
          Werkstoff.Types.ELEMENT,
          Werkstoff.GenerationFeatures().disable().addCells().enforceUnification(),
          OFFSET_PRODUCT + 4,
          TextureSet.SET_FLUID)

  override fun run() {
    OrePrefixes.entries.forEach { GenFeaturesUniversal.addPrefix(it) }
    GenFeaturesUniversal.removePrefix(OrePrefixes.ore)
  }
}
