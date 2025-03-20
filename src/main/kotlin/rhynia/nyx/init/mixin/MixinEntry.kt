package rhynia.nyx.init.mixin

import gregtech.api.enums.Mods
import gregtech.api.enums.Mods.AppliedEnergistics2
import gregtech.api.enums.Mods.BartWorks
import gregtech.api.enums.Mods.GTPlusPlus
import rhynia.nyx.Log

/** Store all late mixins, will be loaded by MixinManager */
@Suppress("unused", "SpellCheckingInspection")
enum class MixinEntry(builder: MixinBuilder) {

  // spotless:off

    PortableCell(
        MixinBuilder().ofClasses("ae.MixinPortableCell").toMod(AppliedEnergistics2)
    ),
    QuantumCell(
        MixinBuilder().ofClasses("ae.MixinQuantumCell").toMod(AppliedEnergistics2)
    ),

    CircuitAssemblyLine(
        MixinBuilder().ofClasses("bw.MixinCircuitAssemblyLine").toMod(BartWorks)
    ),
    MegaMultiBase(
        MixinBuilder().ofClasses("bw.MixinMegaMultiBase").toMod(BartWorks)
    ),
    VoidMiner(
        MixinBuilder().ofClasses("bw.MixinVoidMiner").toMod(BartWorks)
    ),

    ElementalDuplicator(
        MixinBuilder().ofClasses("pp.MixinElementalDuplicator").toMod(GTPlusPlus)
    ),
    IndustrialDehydrator(
        MixinBuilder().ofClasses("pp.MixinIndustrialDehydrator").toMod(GTPlusPlus)
    ),
    IndustrialFluidHeater(
        MixinBuilder().ofClasses("pp.MixinIndustrialFluidHeater").toMod(GTPlusPlus)
    ),
    IndustrialMacerator(
        MixinBuilder().ofClasses("pp.MixinIndustrialMacerator").toMod(GTPlusPlus)
    ),
    IndustrialWashPlant(
        MixinBuilder().ofClasses("pp.MixinIndustrialWashPlant").toMod(GTPlusPlus)
    ),
    MassFabricator(
        MixinBuilder().ofClasses("pp.MixinMassFabricator").toMod(GTPlusPlus)
    ),
    MegaAlloyBlastSmelter(
        MixinBuilder().ofClasses("pp.MixinMegaAlloyBlastSmelter").toMod(GTPlusPlus)
    ),
    TreeFarm(
        MixinBuilder().ofClasses("pp.MixinTreeFarm").toMod(GTPlusPlus)
    ),

    // spotless:on
  ;

  val modId: String =
      builder.targetModId ?: throw IllegalStateException("Target mod ID not set for $name")
  val mixinClasses: List<String> = builder.classes
  val condition: (() -> Boolean) = builder.condition ?: { true }

  companion object {
    fun findLateMixins(loadedMods: Set<String>): List<String> {
      val mixinsToLoad = mutableListOf<String>()
      val mininsNotLoad = mutableListOf<String>()
      MixinEntry.entries.forEach {
        if (it.modId in loadedMods && (it.condition())) {
          mixinsToLoad.addAll(it.mixinClasses)
        } else {
          mininsNotLoad.addAll(it.mixinClasses)
        }
      }
      Log.info("Mixins to load: $mixinsToLoad")
      if (mininsNotLoad.isNotEmpty()) Log.info("Mixins not load: $mininsNotLoad")
      return mixinsToLoad
    }
  }

  private class MixinBuilder {
    val classes = mutableListOf<String>()
    var targetModId: String? = null
    var condition: (() -> Boolean)? = null

    /**
     * Add mixin classes to the builder
     *
     * Note that the class name is under the package `rhynia.nyx.mixins`, the prefix is NOT needed
     */
    fun ofClasses(vararg mixinClasses: String): MixinBuilder = apply {
      classes.addAll(mixinClasses)
    }

    /** Set the target mod ID, must be called or the entry will be invalid */
    fun toMod(id: String) = apply { targetModId = id }

    /** Convert Mods to mod ID */
    fun toMod(targetMod: Mods) = toMod(targetMod.ID)

    /** Extra condition checked when loading the mixin */
    fun applyIf(ai: () -> Boolean) = apply { condition = ai }
  }
}
