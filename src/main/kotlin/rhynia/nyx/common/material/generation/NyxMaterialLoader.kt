package rhynia.nyx.common.material.generation

import gregtech.api.enums.FluidState
import gregtech.api.enums.OrePrefixes
import gregtech.api.fluid.GTFluidFactory
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import rhynia.nyx.Log
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.item.NyxGeneratedMetaItem

object NyxMaterialLoader : Loader {
  override fun load() {
    Log.info("Registering materials...")
    val time1 = System.currentTimeMillis()
    MaterialSet.forEach {
      Log.debug("Loading material: ${it.id}: ${it.internalName}")
      generateFluid(it)
      MaterialMap[it.id] = it
      it.hasInitialiated = true
    }
    generateMetaItem()
    Log.info("Generated ${MaterialMap.size} materials in ${System.currentTimeMillis() - time1}ms")
  }

  /**
   * All registered materials, when an instance of [NyxMaterial] is created, it will be added to
   * this set.
   *
   * So keep all [NyxMaterial] as singleton.
   *
   * @see NyxMaterial
   */
  val MaterialSet = mutableSetOf<NyxMaterial>()

  /**
   * Material map, the key is the material id, and the value is the material instance.
   *
   * The instance of the [MaterialSet] will be added to this map after the fluid generation process.
   */
  val MaterialMap = mutableMapOf<Short, NyxMaterial>()

  /**
   * All needed item instances by all the materials, the key is the [OrePrefixes] and the value is
   * the item instance.
   */
  val ItemMap = mutableMapOf<OrePrefixes, NyxGeneratedMetaItem>()

  /**
   * Fluid map, the key is the material id and the value is another map of [FluidState] to [Fluid].
   */
  val FluidMap = mutableMapOf<Short, MutableMap<FluidState, Fluid>>()

  private fun generateFluid(material: NyxMaterial) {
    if (!material.flagFluid) return
    material.fluidStateMap.forEach { (state, info) ->
      val (name, temperature) = info
      var fluid =
          if (FluidRegistry.isFluidRegistered(name)) {
            Log.warn(
                "Fluid $name is already registered! Referring it as ${material.internalName}'s fluid.")
            FluidRegistry.getFluid(name)
          } else {
            GTFluidFactory.builder(name)
                .withLocalizedName(material.displayName)
                .withStateAndTemperature(state, temperature)
                .withTextureName("autogenerated")
                .withColorRGBA(material.color)
                .buildAndRegister()
                .asFluid()
                .also { Log.debug("Generated fluid $name") }
          }
      FluidMap[material.id]?.let { it[state] = fluid }
          ?: run { FluidMap[material.id] = mutableMapOf(state to fluid) }
    }
  }

  private fun getUsedOrePrefixes(): Set<OrePrefixes> =
      MaterialSet.flatMap { it.getFinalOrePrefixes() }
          .toSortedSet(compareBy { it.ordinal })
          .also { Log.debug("Used ore prefixes: ${it.joinToString(", ") { it.name }}") }

  private fun generateMetaItem() {
    getUsedOrePrefixes().forEach {
      ItemMap.getOrPut(it) { NyxGeneratedMetaItem(it) }
      Log.debug("Generated item for ${it.name}")
    }
  }
}
