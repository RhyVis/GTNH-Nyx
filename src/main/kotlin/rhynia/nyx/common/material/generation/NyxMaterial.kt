package rhynia.nyx.common.material.generation

import gregtech.api.enums.FluidState
import gregtech.api.enums.FluidState.*
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.TextureSet
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.Log
import rhynia.nyx.api.util.firstCharUpperCase
import rhynia.nyx.common.container.NyxItemList

/**
 * Simple material class, it is used to generate materials for GT5U
 *
 * The material instance should be created only once at preInit stage, then the system will handle
 * the auto generation
 */
@Suppress("SpellCheckingInspection", "unused")
class NyxMaterial(
    /**
     * The material id, it should be unique and not larger than 32767, used as item meta in
     * generated items
     */
    val id: Short,
    /**
     * Internal name of the material, should be unique and not contain any space, anyway I will
     * remove the space.
     *
     * 2025.02.09 Add more strict rule by only allowing [A-Za-z0-9_]
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
    NyxMaterialLoader.MaterialSet.add(this)
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

    fun FluidState.shouldHasCell() = this in setOf(LIQUID, MOLTEN, PLASMA)

    fun FluidState.shouldHasForestryCell() =
        Mods.Forestry.isModLoaded && this in setOf(LIQUID, MOLTEN)
  }

  // region Material Metadata

  /**
   * The name used in registration, when it comes to 'finding' a material, [id] is more commonly
   * used
   */
  val internalName: String = internalName.replace(Regex("[^A-Za-z0-9]"), "")

  /** The title case of the internal name, used in orePrefixes, etc. */
  val internalNameTitleCase: String by lazy { internalName.firstCharUpperCase() }

  /** Element desc such as Fe, Au, etc. */
  var elementTooltip: Array<String> = arrayOf()

  /** Additional tooltip for the material, need to press shift to see */
  var extraTooltip: Array<String> = arrayOf()

  /** The texture set for the material, it should be one of the [TextureSet] */
  var textureSet: TextureSet = TextureSet.SET_NONE

  var protons: Int = 32

  var mass: Int = 64

  /** Whether the material has been initialized, cannot be set to false again */
  var hasInitialiated = false
    set(value) {
      if (value) {
        Log.debug("Material $internalName has been initialized")
        field = true
      } else {
        throw IllegalStateException(
            "Material $internalName's initalizaion should not be set to false again")
      }
    }

  /**
   * Whether to skip the recipe generation, if the custom recipe is provided, it should be set to
   * true
   *
   * @see NyxMaterialRecipeLoader
   */
  var skipRecipeGeneration: Boolean = false

  /**
   * Add the tooltip to the material, commonly used as elemental representation, e.g. "Fe" for iron.
   *
   * Should be called before `addTooltip`
   *
   * @param e The element desc
   * @param subscript Whether to subscript the numbers
   */
  fun addElementalTooltip(vararg e: String, subscript: Boolean = true) {
    elementTooltip += e.map { if (subscript) it.subscriptNumbers() else it }
  }

  /**
   * Add additional tooltip to the material, need to press shift
   *
   * @param tooltips The additional tooltips
   */
  fun addExtraTooltip(vararg tooltips: String) {
    extraTooltip += tooltips
  }

  // endregion

  // region OrePrefix

  /**
   * The allowed ore prefixes for the material, in another word, the OrePrefixes can be used to
   * generate item for the material
   */
  private val allowedOrePrefixes: MutableSet<OrePrefixes> = mutableSetOf()

  /**
   * Check if the ore prefix is valid for the material
   *
   * @param prefix The ore prefix
   */
  fun isTypeValid(prefix: OrePrefixes) =
      prefix in allowedOrePrefixes && prefix !in disabledOrePrefixes

  /**
   * Check if the ore prefixes are valid for the material
   *
   * @param prefixes The ore prefixes
   */
  fun isTypeValid(vararg prefixes: OrePrefixes) = prefixes.all(::isTypeValid)

  /** Get all the valid ore prefixes for the material */
  fun getFinalOrePrefixes() = allowedOrePrefixes.filter { it !in disabledOrePrefixes }

  /** Manually add the ore prefix to the material */
  fun addOrePrefix(prefix: OrePrefixes) {
    allowedOrePrefixes.add(prefix)
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
      NyxMaterialLoader.ItemMap[orePrefix]?.let {
        if (!isTypeValid(orePrefix))
            return let {
              Log.error(
                  "Material $internalName does not have a valid item for ore prefix $orePrefix")
              NyxItemList.TestItem01.get(0)
            }
        ItemStack(it, amount, id.toInt())
      }
          ?: let {
            Log.error("OrePrefix $orePrefix is not registered by any material")
            NyxItemList.TestItem01.get(0)
          }

  // endregion

  // region Dust

  /**
   * Flag to enable dusts for the material
   *
   * incluiding dust, dustSmall, dustTiny
   */
  var flagDust: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /** Enable dusts for the material */
  fun enableDusts() {
    flagDust = true
    addOrePrefix(OrePrefixes.dust)
    addOrePrefix(OrePrefixes.dustSmall)
    addOrePrefix(OrePrefixes.dustTiny)
  }

  /**
   * Get the dust item stack
   *
   * @param amount The amount of dust
   * @return The dust item stack
   */
  fun getDust(amount: Int = 1) = get(OrePrefixes.dust, amount)

  // endregion

  // region Ingot

  /**
   * Flag to enable ingots for the material
   *
   * incluiding ingot, ingotDouble, ingotTriple, ingotQuadruple, ingotQuintuple, ingotHot, nugget
   */
  var flagIngot: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /**
   * Enable ingots for the material
   *
   * @param prefixes The ingot ore prefixes
   */
  fun enableIngots(
      vararg prefixes: OrePrefixes =
          arrayOf(
              OrePrefixes.ingot,
              OrePrefixes.ingotDouble,
              OrePrefixes.ingotTriple,
              OrePrefixes.ingotQuadruple,
              OrePrefixes.ingotQuintuple,
              OrePrefixes.ingotHot,
              OrePrefixes.nugget,
          )
  ) {
    if (prefixes.isEmpty()) return
    flagIngot = true
    prefixes.forEach(this::addOrePrefix)
  }

  /**
   * Get the ingot item stack
   *
   * @param amount The amount of ingot
   * @return The ingot item stack
   */
  fun getIngot(amount: Int = 1) = get(OrePrefixes.ingot, amount)

  // endregion

  // region Plate

  /**
   * Flag to enable plates for the material
   *
   * incluiding plate, plateDouble, plateTriple, plateQuadruple, plateQuintuple, plateDense, foil
   */
  var flagPlate: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /**
   * Enable plates for the material
   *
   * @param orePrefixes The plate ore prefixes
   */
  fun enablePlates(
      vararg orePrefixes: OrePrefixes =
          arrayOf(
              OrePrefixes.plate,
              OrePrefixes.plateDouble,
              OrePrefixes.plateTriple,
              OrePrefixes.plateQuadruple,
              OrePrefixes.plateQuintuple,
              OrePrefixes.plateDense,
              OrePrefixes.foil,
          )
  ) {
    if (orePrefixes.isEmpty()) return
    flagPlate = true
    orePrefixes.forEach(this::addOrePrefix)
  }

  /**
   * Get the plate item stack
   *
   * @param amount The amount of plate
   * @return The plate item stack
   */
  fun getPlate(amount: Int = 1) = get(OrePrefixes.plate, amount)

  // endregion

  // region Gem

  /**
   * Flag to enable gems for the material
   *
   * incluiding gem, gemChipped, gemFlawed, gemFlawless, gemExquisite, lens
   */
  var flagGem: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /**
   * Enable gems for the material
   *
   * @param orePrefixes The gem ore prefixes
   */
  fun enableGems(
      vararg orePrefixes: OrePrefixes =
          arrayOf(
              OrePrefixes.gem,
              OrePrefixes.gemChipped,
              OrePrefixes.gemFlawed,
              OrePrefixes.gemFlawless,
              OrePrefixes.gemExquisite,
              OrePrefixes.lens,
          )
  ) {
    if (orePrefixes.isEmpty()) return
    flagGem = true
    orePrefixes.forEach(this::addOrePrefix)
  }

  /**
   * Get the gem itemstack
   *
   * @param amount The amount of gem
   * @return The gem item stack
   */
  fun getGem(amount: Int = 1) = get(OrePrefixes.gem, amount)

  // endregion

  // region Misc

  /**
   * Flag to enable misc items for the material
   *
   * incluiding stick, stickLong, spring, springSmall, bolt, gearGt, gearGtSmall, ring, rotor, screw
   */
  var flagMisc: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /**
   * Enable misc items for the material
   *
   * @param orePrefixes The misc ore prefixes
   */
  fun enableMisc(
      vararg orePrefixes: OrePrefixes =
          arrayOf(
              OrePrefixes.stick,
              OrePrefixes.stickLong,
              OrePrefixes.spring,
              OrePrefixes.springSmall,
              OrePrefixes.bolt,
              OrePrefixes.gearGt,
              OrePrefixes.gearGtSmall,
              OrePrefixes.ring,
              OrePrefixes.rotor,
              OrePrefixes.screw,
          )
  ) {
    if (orePrefixes.isEmpty()) return
    flagMisc = true
    orePrefixes.forEach(this::addOrePrefix)
  }

  // endregion

  // region Fluid

  /**
   * Flag to enable fluids for the material
   *
   * incluiding gas, liquid, molten, plasma, slurry
   */
  var flagFluid: Boolean = false
    private set(value) {
      if (hasInitialiated)
          throw IllegalStateException("Material $internalName has been initialized")
      if (value) field = true
    }

  /** The fluid state map, it contains the fluid state and the temperature */
  val fluidStateMap: MutableMap<FluidState, Pair<String, Int>> = mutableMapOf()

  /**
   * Enable fluids for the material
   *
   * @param states The fluid states and their temperatures
   */
  fun enableFluids(vararg states: Pair<FluidState, Int>) {
    if (states.isEmpty()) return
    flagFluid = true
    states.forEach { (state, temperature) ->
      fluidStateMap[state] =
          when (state) {
            GAS -> "gas.$internalName.n"
            LIQUID -> "liquid.$internalName.n"
            MOLTEN -> "molten.$internalName.n"
            PLASMA -> "plasma.$internalName.n"
            SLURRY -> "slurry.$internalName.n"
          } to temperature
      when (state) {
        LIQUID -> addOrePrefix(OrePrefixes.cell)
        MOLTEN -> addOrePrefix(OrePrefixes.cellMolten)
        PLASMA -> addOrePrefix(OrePrefixes.cellPlasma)
        else -> Unit
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
      NyxMaterialLoader.FluidMap[this.id]?.get(state)
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
    if (state !in fluidStateMap || state == GAS || state == SLURRY) {
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
