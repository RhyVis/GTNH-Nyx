package vis.rhynia.nova.common.loader

import gregtech.api.util.GTUtility
import vis.rhynia.nova.common.loader.container.NovaWirelessHatchList
import vis.rhynia.nova.common.tile.hatch.MTEHatchWirelessMultiExtended

object WirelessExtraLoader {
  private const val OFFSET: Int = 17420

  private fun tierName(tier: Int): String {
    return when (tier) {
      5 -> "IV"
      6 -> "LuV"
      7 -> "ZPM"
      8 -> "UV"
      9 -> "UHV"
      10 -> "UEV"
      11 -> "UIV"
      12 -> "UMV"
      else -> "?"
    }
  }

  fun doRegister() {
    for (extW in NovaWirelessHatchList.entries) {
      extW.set(
          MTEHatchWirelessMultiExtended(
                  OFFSET + extW.ordinal,
                  "hatch.$extW.tier.${extW.getTier()}",
                  "${tierName(extW.getTier())} ${GTUtility.formatNumbers(extW.getAmp().toLong())}A/t 无线能源仓",
                  extW.getTier(),
                  extW.getAmp())
              .getStackForm(1))
    }
  }
}
