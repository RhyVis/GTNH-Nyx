package rhynia.constellation.init

import gregtech.api.util.GTUtility
import rhynia.constellation.api.interfaces.Loader
import rhynia.constellation.common.container.CelWirelessHatchList
import rhynia.constellation.common.tile.hatch.CelHatchWirelessMultiExtended

object WirelessExtraLoader : Loader {
  private const val OFFSET: Int = 17420

  override fun load() {
    CelWirelessHatchList.entries.forEach {
      it.set(
          CelHatchWirelessMultiExtended(
                  OFFSET + it.ordinal,
                  "hatch.$it.tier.${it.tier}",
                  "${it.tierName} ${GTUtility.formatNumbers(it.amp.toLong())}A/t 无线能源仓",
                  it.tier,
                  it.amp)
              .getStackForm(1))
    }
  }
}
