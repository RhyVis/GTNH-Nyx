package rhynia.nyx

import java.io.File
import net.minecraftforge.common.config.Configuration

object Config {
  private const val CATEGORY_RECIPE = "recipe"

  var loadTstRecipe: Boolean = true
  var loadWirelessHatchRecipe: Boolean = true

  var Recipe_TR_RecipeModifier: Int = 64
  var Recipe_TR_OutputModifier: Int = 250
  var Recipe_TR_CatalystACAModifier: Int = 4000
  var Recipe_TR_CatalystACRModifier: Int = 2000
  var Recipe_OPT_Modifier: Int = 4

  private const val CATEGORY_MACHINE = "machine"

  var MTE_Creator_UseEnergy: Boolean = true

  @JvmStatic
  fun syncConfig(configFile: File) {
    Configuration(configFile).run {
      loadTstRecipe =
          getBoolean("LoadTstRecipe", CATEGORY_RECIPE, loadTstRecipe, "Load TST recipe?")
      loadWirelessHatchRecipe =
          getBoolean(
              "LoadWirelessHatchRecipe",
              CATEGORY_RECIPE,
              loadWirelessHatchRecipe,
              "Load Wireless Hatch recipe?")

      Recipe_TR_RecipeModifier =
          getInt(
              "Recipe_TR_RecipeModifier",
              CATEGORY_RECIPE,
              64,
              1,
              Int.Companion.MAX_VALUE / 144 - 1,
              "TR进行单次合成时的倍率")
      Recipe_TR_OutputModifier =
          getInt(
              "Recipe_TR_OutputModifier",
              CATEGORY_RECIPE,
              250,
              1,
              Int.Companion.MAX_VALUE / 144 - 1,
              "TR进行单次合成时输出催化剂的量，单位L")
      Recipe_TR_CatalystACAModifier =
          getInt(
              "Recipe_TR_CatalystACAModifier",
              CATEGORY_RECIPE,
              4000,
              16,
              Int.Companion.MAX_VALUE / 16 - 1,
              "TR进行单次合成时的ACA催化剂消耗，不受合成倍率影响，单位L")
      Recipe_TR_CatalystACRModifier =
          getInt(
              "Recipe_TR_CatalystACRModifier",
              CATEGORY_RECIPE,
              250,
              1,
              Int.Companion.MAX_VALUE,
              "TR进行单次合成时的ACR催化剂消耗，不受合成倍率影响，单位L")
      Recipe_OPT_Modifier =
          getInt(
              "Recipe_OPT_Modifier",
              CATEGORY_RECIPE,
              4,
              1,
              Int.Companion.MAX_VALUE / 16,
              "Recipe_OPT_Modifier")

      MTE_Creator_UseEnergy =
          getBoolean("MTE_Creator_UseEnergy", CATEGORY_MACHINE, true, "Creator复制物品与流体是否使用能量")

      if (hasChanged()) save()
    }
  }
}
