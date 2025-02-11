package rhynia.nyx.common.tile.multi.process

import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTUtility
import gregtech.api.util.MultiblockTooltipBuilder
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import kotlin.math.pow
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.DARK_RED
import net.minecraft.util.EnumChatFormatting.RED
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import rhynia.nyx.api.enums.NyxValues.CommonStrings.NyxNuclear
import rhynia.nyx.common.tile.base.NyxMTECubeBase

class NyxMTEKelvinTransformField : NyxMTECubeBase<NyxMTEKelvinTransformField> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NyxMTEKelvinTransformField(mName)
  }

  // endregion

  // region Processing Logic
  private var mRecipeMode: Byte = 0 // 0-sVacuumRecipes,1-?
  private val pRecipeMode =
      ModeContainer.of(RecipeMaps.vacuumFreezerRecipes, GTPPRecipeMaps.advancedFreezerRecipes)

  override val rMaxParallel: Int
    get() = if (pRecipeMode.index == 0) 2048 else 64 + 16 * GTUtility.getTier(this.maxInputVoltage)

  override val rDurationModifier: Double
    get() =
        if (pRecipeMode.index == 0) 0.95.pow(GTUtility.getTier(this.maxInputVoltage).toDouble())
        else 1.0

  override fun getRecipeMap(): RecipeMap<*>? = pRecipeMode.current

  override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>> = pRecipeMode.all

  override fun getRecipeCatalystPriority(): Int = -10

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      pRecipeMode.next()
      GTUtility.sendChatToPlayer(
          aPlayer,
          StatCollector.translateToLocal(
              "nyx.KelvinTransformField.pRecipeMode." + this.mRecipeMode))
    }
  }
  // endregion

  // region Structure

  override val sCasingHatch: Array<IHatchElement<in NyxMTEKelvinTransformField>>
    get() = arrayOf(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))

  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings2 to 1

  override val sCoreBlock: Pair<Block, Int>?
    get() = GregTechAPI.sBlockCasings4 to 7

  override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_VACUUM_FREEZER to OVERLAY_FRONT_VACUUM_FREEZER_GLOW

  override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE to OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW

  // endregion

  // region Overrides
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("真空冷冻机 | 热动力学解析")
          .addInfo("开尔文变换场的控制器")
          .addInfo("${RED}万物都有${DARK_RED}终结${RED}，我不过是定义了它的到来.")
          .addInfo("指挥粒子做它该做的热运动.")
          .addInfo("真空冷冻机模式下，最大并行为2048.")
          .addInfo("且电压每提高1级, 降低5%配方耗时(叠乘计算).")
          .addInfo("热动力学解析模式下，基础最大并行为64.")
          .addInfo("且电压每提高1级, 增加16并行.")
          .addChangeModeByScrewdriver()
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus()
          .addInputHatch()
          .addOutputBus()
          .addOutputHatch()
          .addEnergyHatch()
          .toolTipFinisher(NyxNuclear)

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    if (aNBT == null) return
    pRecipeMode.saveNBTData(aNBT, "pRecipeMode")
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    if (aNBT == null) return
    pRecipeMode.loadNBTData(aNBT, "pRecipeMode")
  }
  // endregion
}
