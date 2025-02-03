package vis.rhynia.nova.common.loader

import gregtech.api.util.GTUtility
import vis.rhynia.nova.api.interfaces.Loader
import vis.rhynia.nova.common.loader.container.NovaWirelessHatchList
import vis.rhynia.nova.common.tile.hatch.MTEHatchWirelessMultiExtended

object WirelessExtraLoader : Loader {
  private const val OFFSET: Int = 17420

  override fun load() {
    NovaWirelessHatchList.entries.forEach {
      it.set(
          MTEHatchWirelessMultiExtended(
                  OFFSET + it.ordinal,
                  "hatch.$it.tier.${it.tier}",
                  "${it.tierName} ${GTUtility.formatNumbers(it.amp.toLong())}A/t 无线能源仓",
                  it.tier,
                  it.amp)
              .getStackForm(1))
    }
  }
}
