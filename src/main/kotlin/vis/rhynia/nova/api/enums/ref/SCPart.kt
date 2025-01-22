package vis.rhynia.nova.api.enums.ref

import gregtech.api.enums.GTValues
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTOreDictUnificator
import kotlin.math.pow
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.common.material.NovaMaterial

enum class SCPart(private val production: Materials, private val raw: Materials) {
  MV(Materials.SuperconductorMV, Materials.Pentacadmiummagnesiumhexaoxid),
  HV(Materials.SuperconductorHV, Materials.Titaniumonabariumdecacoppereikosaoxid),
  EV(Materials.SuperconductorEV, Materials.Uraniumtriplatinid),
  IV(Materials.SuperconductorIV, Materials.Vanadiumtriindinid),
  LuV(
      Materials.SuperconductorLuV,
      Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid),
  ZPM(Materials.SuperconductorZPM, Materials.Tetranaquadahdiindiumhexaplatiumosminid),
  UV(Materials.SuperconductorUV, Materials.Longasssuperconductornameforuvwire),
  UHV(Materials.SuperconductorUHV, Materials.Longasssuperconductornameforuhvwire),
  UEV(Materials.SuperconductorUEV, Materials.SuperconductorUEVBase),
  UIV(Materials.SuperconductorUIV, Materials.SuperconductorUIVBase),
  UMV(Materials.SuperconductorUMV, Materials.SuperconductorUMVBase),
  ;

  companion object {
    const val TWO: Double = 2.0
  }

  fun getMaterial(raw: Boolean) = if (raw) this.raw else this.production

  fun getMultiplier() = this.ordinal + 1

  fun getPowValue() = 2.0.pow(this.ordinal).toInt()

  fun getVoltage() = GTValues.V[this.ordinal + 2]

  fun getRecipeVoltage() = GTValues.VP[this.ordinal + 2]

  fun getRecipeVoltageLow() = GTValues.V[this.ordinal + 1]

  fun getRecipeVoltageHigh() = GTValues.V[this.ordinal + 3]

  fun getWire(amount: Int): ItemStack =
      GTOreDictUnificator.get(OrePrefixes.wireGt01, production, amount.toLong())

  fun getWire(amount: Int, raw: Boolean): ItemStack =
      GTOreDictUnificator.get(OrePrefixes.wireGt01, getMaterial(raw), amount.toLong())

  fun getWireFine(amount: Int): ItemStack =
      GTOreDictUnificator.get(OrePrefixes.wireFine, raw, amount.toLong())

  fun getFrame(amount: Int): ItemStack =
      GTOreDictUnificator.get(OrePrefixes.frameGt, production, amount.toLong())

  fun getDust(amount: Int): ItemStack = raw.getDust(amount)

  fun getPump(amount: Int): ItemStack =
      Tier.valueOf(this.name).getComponent(Tier.Component.ElectricPump, amount)

  fun getSolenoid(amount: Int): ItemStack =
      ItemList.valueOf("Superconducting_Magnet_Solenoid_${this.name}").get(amount.toLong())

  fun getPrefix(prefix: OrePrefixes, amount: Int): ItemStack = getPrefix(prefix, amount, false)

  fun getPrefix(prefix: OrePrefixes, amount: Int, raw: Boolean): ItemStack =
      GTOreDictUnificator.get(prefix, getMaterial(raw), amount.toLong())

  fun getMolten(amount: Int): FluidStack = getMaterial(true).getMolten(amount.toLong())

  fun getSxEqualFluid(amount: Int, raw: Boolean = false): FluidStack =
      if (raw) NovaMaterial.SuperconductorFlux.getFluidOrGas(TWO.pow(ordinal).toInt() * amount * 4)
      else NovaMaterial.SuperconductorFlux.getFluidOrGas(TWO.pow(ordinal).toInt() * amount)
}
