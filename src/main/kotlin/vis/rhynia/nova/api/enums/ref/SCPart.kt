package vis.rhynia.nova.api.enums.ref

import gregtech.api.enums.GTValues
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTOreDictUnificator
import kotlin.math.pow
import net.minecraft.item.ItemStack

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
}
