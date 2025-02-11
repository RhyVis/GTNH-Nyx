package rhynia.nyx.init

import gregtech.api.util.GTUtility
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.container.NyxWirelessHatchList
import rhynia.nyx.common.tile.hatch.NyxHatchWirelessMultiExtended

object WirelessExtraLoader : Loader {
  private const val OFFSET: Int = 17420

  override fun load() {
    NyxWirelessHatchList.entries.forEach {
      it.set(
          NyxHatchWirelessMultiExtended(
                  OFFSET + it.ordinal,
                  "hatch.$it.tier.${it.tier}",
                  "${it.tierName} ${GTUtility.formatNumbers(it.amp.toLong())}A/t 无线能源仓",
                  it.tier,
                  it.amp)
              .getStackForm(1))
    }
  }
}
