package vis.rhynia.nova.api.enums.ref

import gregtech.api.enums.GTValues
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTOreDictUnificator
import net.minecraft.item.ItemStack
import vis.rhynia.nova.Log

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

  enum class Component {
    ElectricMotor,
    ElectricPiston,
    ElectricPump,
    RobotArm,
    ConveyorModule,
    Emitter,
    Sensor,
    FieldGenerator;

    override fun toString(): String = super.toString() + "_"
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
}
