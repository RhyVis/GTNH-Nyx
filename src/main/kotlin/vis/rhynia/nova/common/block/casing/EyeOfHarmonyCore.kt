package vis.rhynia.nova.common.block.casing

import vis.rhynia.nova.common.block.base.NovaMetaMachine

class EyeOfHarmonyCore() : NovaMetaMachine("EyeOfHarmonyCore") {
  init {
    setHardness(9.0f)
    setResistance(5.0f)
    setHarvestLevel("wrench", 1)
  }
}
