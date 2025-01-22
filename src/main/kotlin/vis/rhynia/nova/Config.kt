package vis.rhynia.nova

import java.io.File
import net.minecraftforge.common.config.Configuration

object Config {
  const val CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL
  var greeting: String = "Hello World"

  const val CATEGORY_RECIPE = "recipe"
  var loadTstRecipe: Boolean = true
  var loadWirelessHatchRecipe: Boolean = true

  @JvmStatic
  fun syncConfig(configFile: File) {
    Configuration(configFile).run {
      greeting =
          getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?")

      loadTstRecipe =
          getBoolean("loadTstRecipe", CATEGORY_RECIPE, loadTstRecipe, "Load TST recipe?")
      loadWirelessHatchRecipe =
          getBoolean(
              "loadWirelessHatchRecipe",
              CATEGORY_RECIPE,
              loadWirelessHatchRecipe,
              "Load Wireless Hatch recipe?")

      if (hasChanged()) save()
    }
  }
}
