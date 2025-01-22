package vis.rhynia.nova.api.enums.ref

import goodgenerator.main.GoodGenerator
import gregtech.api.enums.GTValues
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.BartWorks
import gregtech.api.enums.Mods.GTPlusPlus
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import net.minecraft.item.ItemStack
import vis.rhynia.nova.Log
import vis.rhynia.nova.common.NovaItemList

@Suppress("unused", "SpellCheckingInspection")
enum class Tier(private val material: Materials) {
  ULV(Materials.ULV),
  LV(Materials.LV),
  MV(Materials.MV),
  HV(Materials.HV),
  EV(Materials.EV),
  IV(Materials.IV),
  LuV(Materials.LuV),
  ZPM(Materials.ZPM),
  UV(Materials.UV),
  UHV(Materials.UHV),
  UEV(Materials.UEV),
  UIV(Materials.UIV),
  UMV(Materials.UMV),
  UXV(Materials.UXV),
  MAX(Materials.MAX);

  enum class Component(private val enumNamePrefix: String) {
    ElectricMotor("Electric_Motor"),
    ElectricPiston("Electric_Piston"),
    ElectricPump("Electric_Pump"),
    RobotArm("Robot_Arm"),
    ConveyorModule("Conveyor_Module"),
    Emitter("Emitter"),
    Sensor("Sensor"),
    FieldGenerator("Field_Generator");

    val prefix: String
      get() = "${this.enumNamePrefix}_"
  }

  enum class Hatch {
    Dynamo,
    Energy,
    Energy4A,
    Energy16A,
    Energy64A,
    LaserEnergy,
    LaserDynamo,
    WirelessDynamo,
    WirelessEnergy,
    WirelessEnergy4A,
    WirelessEnergy16A,
    WirelessEnergy64A,
    WirelessLaser
  }

  val voltage: Long
    get() = GTValues.V[ordinal]

  val voltageRecipe: Long
    get() = GTValues.VP[ordinal]

  val superConductor: SCPart
    get() =
        when (this) {
          ULV,
          LV -> {
            Log.error("$this tier is too low for standard SC material!")
            SCPart.MV
          }
          UXV,
          MAX -> {
            Log.error("$this tier is too high for standard SC material!")
            SCPart.UMV
          }
          else -> SCPart.entries[this.ordinal - 2]
        }

  val solderMaterial: SolderMaterial
    get() =
        when (this) {
          ULV,
          LV,
          MV,
          HV,
          EV -> SolderMaterial.SolderingAlloy
          UEV,
          UIV,
          UMV,
          UXV,
          MAX -> SolderMaterial.MutatedLivingAlloy
          else -> SolderMaterial.IndaAlloy
        }

  val circuitMaterial: Materials
    get() =
        when (this) {
          UMV -> Materials.Piko
          else -> material
        }

  fun getSolder(amount: Int) = solderMaterial.getFluidStack(amount)

  fun getCircuit(amount: Int): ItemStack =
      GTOreDictUnificator.get(OrePrefixes.circuit, circuitMaterial, amount.toLong())

  fun getCircuitWrap(amount: Int): ItemStack = BundleChip.entries[ordinal].getItemStack(amount)

  fun getComponent(component: Component, amount: Int): ItemStack {
    if (this == ULV) {
      Log.error("Attempting to get ULV component, but it's already removed!")
      return NovaItemList.TestItem01.get(1)
    } else
        return ItemList.valueOf(component.prefix + this.toString())
            .get(amount.toLong(), NovaItemList.TestItem01.get(1))
  }

  fun getCoil(amount: Int): ItemStack =
      when (this) {
        UEV,
        UIV,
        UMV,
        UXV,
        MAX -> {
          Log.error("Attempting to get $this coil, but it doesn't exist!")
          NovaItemList.TestItem01.get(1)
        }
        else ->
            ItemList.valueOf("${this}_Coil").get(amount.toLong(), NovaItemList.TestItem01.get(1))
      }

  fun getComponentAssemblyCasing(amount: Int): ItemStack =
      if (this == ULV) {
        Log.error("Attempting to get ULV casing, but it doesn't exist!")
        NovaItemList.TestItem01.get(1)
      } else
          GTModHandler.getModItem(
              GoodGenerator.MOD_ID,
              "componentAssemblylineCasing",
              amount.toLong(),
              this.ordinal - 1)

  fun getGlass(amount: Int): ItemStack =
      when (this) {
        ULV,
        LV,
        MV,
        HV -> GTModHandler.getModItem(BartWorks.ID, "BW_GlasBlocks", amount.toLong(), 0)
        UMV,
        UXV,
        MAX -> GTModHandler.getModItem(BartWorks.ID, "BW_GlasBlocks2", amount.toLong())
        else ->
            GTModHandler.getModItem(
                BartWorks.ID, "BW_GlasBlocks", amount.toLong(), this.ordinal - 3)
      }

  fun getBufferCore(amount: Int): ItemStack =
      when (this) {
        UHV,
        UEV,
        UIV,
        UMV,
        UXV,
        MAX -> {
          GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", amount.toLong())
        }
        else -> {
          GTModHandler.getModItem(
              GTPlusPlus.ID, "item.itemBufferCore${this.ordinal + 1}", amount.toLong())
        }
      }
}
