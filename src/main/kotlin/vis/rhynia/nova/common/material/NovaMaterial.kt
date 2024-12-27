package vis.rhynia.nova.common.material

import bartworks.system.material.Werkstoff
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

  override fun run() {
    OrePrefixes.entries.forEach { GenFeaturesUniversal.addPrefix(it) }
    GenFeaturesUniversal.removePrefix(OrePrefixes.ore)
  }
}
