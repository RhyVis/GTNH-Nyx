package vis.rhynia.nova

import java.io.File
import net.minecraftforge.common.config.Configuration

object Config {
  var greeting: String = "Hello World"

  @JvmStatic
  fun syncConfig(configFile: File) {
    Configuration(configFile).run {
      greeting =
          getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?")

      if (hasChanged()) save()
    }
  }
}
