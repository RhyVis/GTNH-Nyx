package vis.rhynia.nova.common.material.generation

import gregtech.api.enums.FluidState
import gregtech.api.enums.FluidState.*
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.TextureSet
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.Log
import vis.rhynia.nova.common.loader.container.NovaItemList

/**
 * Simple material class, it is used to generate materials for GT5U
 *
 * The material instance should be created only once at preInit stage, then the system will handle
 * the auto generation
 */
@Suppress("SpellCheckingInspection")
class SimpleMaterial(
    /**
     * The material id, it should be unique and not larger than 32767, used as item meta in
     * generated items
     */
    val id: Short,
    /**
     * Internal name of the material, should be unique and not contain any space, anyway I will
     * remove the space
     */
    internalName: String,
    /** Localized display name of the material */
    val displayName: String,
    /**
     * The color of the material, it should be an array of 3 or 4 shorts, representing RGBA
     *
     * @see gregtech.api.interfaces.IColorModulationContainer
     */
    val color: ShortArray
) {
  init {
    SimpleMaterialLoader.materialSet.add(this)
  }

  companion object {
    /**
     * Let's say I don't want to generate items for these prefixes, I don't like dealing with world
     * generation etc
     */
    private val disabledOrePrefixes: Set<OrePrefixes> =
        setOf(
            OrePrefixes.rawOre,
            OrePrefixes.ore,
            OrePrefixes.oreSmall,
            OrePrefixes.dustImpure,
            OrePrefixes.dustPure,
            OrePrefixes.crushed,
            OrePrefixes.crushedPurified,
            OrePrefixes.crushedCentrifuged,
        )

    /**
     * Subscript numbers in string, it will convert 0-9 to subscript numbers
     *
     * @return The string with subscript numbers
     */
    fun String.subscriptNumbers(): String =
        this.map {
              when (it) {
                '0' -> '\u2080'
                '1' -> '\u2081'
                '2' -> '\u2082'
                '3' -> '\u2083'
                '4' -> '\u2084'
                '5' -> '\u2085'
                '6' -> '\u2086'
                '7' -> '\u2087'
                '8' -> '\u2088'
                '9' -> '\u2089'
                else -> it
              }
            }
            .joinToString("")

    fun shouldHasCell(state: FluidState) = state in setOf(LIQUID, MOLTEN, PLASMA)

    fun shouldHasForestryCell(state: FluidState) =
        Mods.Forestry.isModLoaded && state in setOf(LIQUID, MOLTEN)
  }

  // region Material Metadata
  val internalName: String = internalName.replace(" ", "")

  var additionalTooltips: Array<String> = arrayOf()

  var textureSet: TextureSet = TextureSet.SET_NONE

  var protons = 32
  var mass: Int = 64

  /**
   * Add the tooltip to the material, commonly used as elemental representation, e.g. "Fe" for iron.
   *
   * Should be called before `addTooltip`
   */
  fun addElementalTooltip(e: String) {
    addTooltip(e.subscriptNumbers())
  }

  /** Add the tooltip to the material */
  fun addTooltip(vararg tooltips: String) {
    additionalTooltips += tooltips
  }
  // endregion

  // region OrePrefix
  private val allowedOrePrefixes: MutableSet<OrePrefixes> = mutableSetOf()

  fun isOrePrefixAllowed(prefix: OrePrefixes) =
      allowedOrePrefixes.contains(prefix) && prefix !in disabledOrePrefixes

  fun getFinalOrePrefixes() = allowedOrePrefixes.filter { it !in disabledOrePrefixes }

  fun addOrePrefix(prefix: OrePrefixes) {
    allowedOrePrefixes.add(prefix)
  }

  /** Enable dusts for the material */
  fun enableDusts() {
    hasDust = true
    addOrePrefix(OrePrefixes.dust)
    addOrePrefix(OrePrefixes.dustSmall)
    addOrePrefix(OrePrefixes.dustTiny)
  }
  // endregion

  // region Item

  /**
   * Get the item stack, it won't throw exception if the item is not found
   *
   * @param orePrefix The ore prefix
   * @param amount The amount of item
   * @return The item stack
   */
  fun get(orePrefix: OrePrefixes, amount: Int = 1): ItemStack =
      SimpleMaterialLoader.itemMap[orePrefix]?.let {
        if (!isOrePrefixAllowed(orePrefix)) return@let null
        ItemStack(it, amount, id.toInt())
      }
          ?: let {
            Log.error("Material $internalName does not have a valid item for ore prefix $orePrefix")
            NovaItemList.TestItem01.get(0)
          }

  /**
   * Get the dust item stack
   *
   * @param amount The amount of dust
   * @return The dust item stack
   */
  fun getDust(amount: Int = 1) = get(OrePrefixes.dust, amount)

  private var hasDust: Boolean = false

  val flagDust: Boolean
    get() = hasDust
  // endregion

  // region Fluid

  private var hasFluid: Boolean = false

  val flagFluid: Boolean
    get() = hasFluid

  val fluidStateMap: MutableMap<FluidState, Pair<String, Int>> = mutableMapOf()

  /**
   * Enable fluids for the material
   *
   * @param states The fluid states and their temperatures
   */
  fun enableFluids(vararg states: Pair<FluidState, Int>) {
    if (states.isEmpty()) return
    hasFluid = true
    states.forEach { (state, temperature) ->
      fluidStateMap[state] =
          when (state) {
            GAS -> "n.gas.$internalName"
            LIQUID -> "n.liquid.$internalName"
            MOLTEN -> "n.molten.$internalName"
            PLASMA -> "n.plasma.$internalName"
            SLURRY -> "n.slurry.$internalName"
          } to temperature
      when (state) {
        LIQUID -> addOrePrefix(OrePrefixes.cell)
        MOLTEN -> addOrePrefix(OrePrefixes.cellMolten)
        PLASMA -> addOrePrefix(OrePrefixes.cellPlasma)
        else -> null
      }
    }
  }

  /**
   * Get the fluid from the fluid state
   *
   * @param state The fluid state
   * @return The fluid
   * @throws IllegalArgumentException If the material does not have a valid fluid state
   */
  fun getFluid(state: FluidState): Fluid =
      SimpleMaterialLoader.fluidMap[this]?.get(state)
          ?: throw IllegalArgumentException(
              "Material $internalName does not have a valid fluid state $state")

  /**
   * In GT system `liquid` state is often called `fluid`, this function is to make it easier to use.
   *
   * This function is actually a wrapper of `getFluidStack(LIQUID, amount)`
   *
   * @param amount The amount of fluid
   */
  fun getFluid(amount: Int = 1000) = getFluidStack(LIQUID, amount)

  /**
   * Get the fluid stack from the fluid state
   *
   * @param state The fluid state
   * @param amount The amount of fluid
   * @return The fluid stack
   * @throws IllegalArgumentException If the material does not have a valid fluid state
   */
  fun getFluidStack(state: FluidState, amount: Int = 1000) = FluidStack(getFluid(state), amount)

  /**
   * Get the gas state fluid stack
   *
   * @param amount The amount of gas
   * @return The gas fluid stack
   * @throws IllegalArgumentException If the material does not have a valid gas state
   */
  fun getGas(amount: Int = 1000) = getFluidStack(GAS, amount)

  /**
   * Get the liquid state fluid stack
   *
   * @param amount The amount of liquid
   * @return The liquid fluid stack
   * @throws IllegalArgumentException If the material does not have a valid liquid state
   */
  fun getLiquid(amount: Int = 1000) = getFluidStack(LIQUID, amount)

  /**
   * Get the molten state fluid stack
   *
   * @param amount The amount of molten
   * @return The molten fluid stack
   * @throws IllegalArgumentException If the material does not have a valid molten state
   */
  fun getMolten(amount: Int = 1000) = getFluidStack(MOLTEN, amount)

  /**
   * Get the plasma state fluid stack
   *
   * @param amount The amount of plasma
   * @return The plasma fluid stack
   * @throws IllegalArgumentException If the material does not have a valid plasma state
   */
  fun getPlasma(amount: Int = 1000) = getFluidStack(PLASMA, amount)

  /**
   * Get the cell item stack from the fluid state
   *
   * @param state The fluid state, should be one of LIQUID, MOLTEN, PLASMA
   * @param amount The amount of cell
   * @return The cell item stack
   * @throws IllegalArgumentException If the material does not have a valid cell state
   */
  fun getCell(state: FluidState, amount: Int = 1): ItemStack {
    if (state == GAS || state == SLURRY || state !in fluidStateMap) {
      Log.warn("Material $internalName does not have a valid cell state $state")
      throw IllegalArgumentException(
          "Material $internalName does not have a valid cell state $state")
    }
    return when (state) {
      LIQUID -> get(OrePrefixes.cell, amount)
      MOLTEN -> get(OrePrefixes.cellMolten, amount)
      PLASMA -> get(OrePrefixes.cellPlasma, amount)
      else ->
          throw IllegalArgumentException(
              "Material $internalName does not have a valid cell state $state")
    }
  }
  // endregion
}
