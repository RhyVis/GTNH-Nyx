package vis.rhynia.nova.common.tile.multi.creation

import com.google.common.collect.ImmutableList
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.CheckRecipeResultRegistry
import gregtech.api.recipe.check.SimpleCheckRecipeResult
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap
import gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser
import java.util.*
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack
import org.apache.commons.lang3.tuple.Pair as ApachePair
import tectech.TecTech.eyeOfHarmonyRecipeStorage
import tectech.recipe.EyeOfHarmonyRecipe
import tectech.thing.CustomItemList
import tectech.util.FluidStackLong
import tectech.util.ItemStackLong
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.common.block.BlockRecord
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.tile.base.NovaMTECubeBase

class NovaMTEEyeOfUltimate : NovaMTECubeBase<NovaMTEEyeOfUltimate> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTEEyeOfUltimate(this.mName)
  }

  private var pRecipeTime: Byte = 0
  private var pMultiplier: Long = 0
  private var pBaseA = 0
  private var pBaseB = 0
  private var pSpacetimeCompressionFieldMetadata = -1
  private var pDisplayName: String? = ""
  private var pUUID: UUID? = null
  private var pCurrentRecipe: EyeOfHarmonyRecipe? = null
  private var outputItems: MutableList<ItemStackLong> = mutableListOf()
  private var outputFluids: MutableList<FluidStackLong> = mutableListOf()

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      pRecipeTime = ((pRecipeTime + 1) % 6).toByte()
      GTUtility.sendChatToPlayer(aPlayer, "合成时间: " + (this.pRecipeTime + 1) + "s")
    }
  }

  // region Processing
  override fun createProcessingLogic(): ProcessingLogic? = null

  override fun checkProcessing(): CheckRecipeResult {
    resetState()
    val tempStack = controllerSlot
    if (tempStack == null) {
      return SimpleCheckRecipeResult.ofFailure("no_planet_block")
    }
    pCurrentRecipe = eyeOfHarmonyRecipeStorage.recipeLookUp(tempStack)
    if (pCurrentRecipe == null) {
      return CheckRecipeResultRegistry.NO_RECIPE
    }

    pDisplayName = tempStack.getDisplayName()

    val result = processRecipe(pCurrentRecipe!!)

    if (result.wasSuccessful()) {
      return result
    }

    pCurrentRecipe = null

    return CheckRecipeResultRegistry.NO_RECIPE
  }

  private fun processRecipe(recipe: EyeOfHarmonyRecipe): CheckRecipeResult {
    getIndex()

    if (pSpacetimeCompressionFieldMetadata == -1) {
      return CheckRecipeResultRegistry.insufficientMachineTier(
          recipe.spacetimeCasingTierRequired.toInt())
    }
    if ((pSpacetimeCompressionFieldMetadata + 1) < recipe.spacetimeCasingTierRequired) {
      return CheckRecipeResultRegistry.insufficientMachineTier(
          recipe.spacetimeCasingTierRequired.toInt())
    }

    if (!addEUToGlobalEnergyMap(pUUID, 1000000000)) {
      return SimpleCheckRecipeResult.ofFailure("insufficient_power_no_val")
    }

    outputItems = recipe.getOutputItems()
    outputFluids = recipe.getOutputFluids()

    outputItems.forEach {
      it.stackSize *= pMultiplier
      outputItemToAENetwork(it.itemStack, it.stackSize)
    }
    outputFluids.forEach {
      it.amount *= pMultiplier
      outputFluidToAENetwork(it.fluidStack, it.amount)
    }

    outputItems = ArrayList<ItemStackLong>()
    outputFluids = ArrayList<FluidStackLong>()

    return CheckRecipeResultRegistry.SUCCESSFUL
  }

  private fun getIndex() {
    mInputBusses.forEach { bus ->
      bus.realInventory.forEach { itemStack ->
        itemStack?.let {
          if (it.isItemEqual(CustomItemList.astralArrayFabricator.get(1))) pBaseA += it.stackSize
          else if (it.isItemEqual(NovaItemList.AstriumInfinityComplex.get(1)))
              pBaseB += it.stackSize
        }
      }
    }
    pMultiplier = 8L * pBaseA + 2L * pBaseB + 1L
  }

  private fun resetState() {
    mMaxProgresstime = 20 * (pRecipeTime + 1)
    mEfficiencyIncrease = 10000
    pBaseA = 0
    pBaseB = 0
    pMultiplier = 0
    pCurrentRecipe = null
    pDisplayName = "NONE"
  }

  override fun onPreTick(aBaseMetaTileEntity: IGregTechTileEntity?, aTick: Long) {
    super.onPreTick(aBaseMetaTileEntity, aTick)
    if (aTick == 1L) strongCheckOrAddUser(baseMetaTileEntity.ownerUuid)
  }

  override fun supportsVoidProtection(): Boolean = false

  override fun supportsInputSeparation(): Boolean = false

  override fun supportsBatchMode(): Boolean = false

  override fun supportsSingleRecipeLocking(): Boolean = false
  // endregion

  // region Structure
  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings1 to 12

  override val sControllerIcon: kotlin.Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_OFF to OVERLAY_DTPF_OFF

  override val sControllerIconActive: kotlin.Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_ON to OVERLAY_DTPF_ON

  override fun genStructureDefinition(): IStructureDefinition<NovaMTEEyeOfUltimate> =
      StructureDefinition.builder<NovaMTEEyeOfUltimate>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement(
              'B',
              StructureUtility.ofBlocksTiered<NovaMTEEyeOfUltimate, Int>(
                  { block: Block, meta: Int ->
                    if (block === BlockRecord.EyeOfHarmonyCoreCasing) meta else null
                  },
                  ImmutableList.of<ApachePair<Block, Int>>(
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 0),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 1),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 2),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 3),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 4),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 5),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 6),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 7),
                      ApachePair.of(BlockRecord.EyeOfHarmonyCoreCasing, 8)),
                  -1,
                  { t: NovaMTEEyeOfUltimate, meta: Int ->
                    t.pSpacetimeCompressionFieldMetadata = meta
                  },
                  { it.pSpacetimeCompressionFieldMetadata }))
          .addElement(
              'C',
              HatchElementBuilder.builder<NovaMTEEyeOfUltimate>()
                  .atLeast(InputBus, OutputBus, OutputHatch)
                  .adder(NovaMTEEyeOfUltimate::addToMachineList)
                  .dot(1)
                  .casingIndex(sCasingIndex)
                  .buildAndChain(sCasingBlock.first, sCasingBlock.second))
          .build()

  override fun checkMachine(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aStack: ItemStack?
  ): Boolean {
    pSpacetimeCompressionFieldMetadata = -1
    return super.checkMachine(aBaseMetaTileEntity, aStack)
  }
  // endregion

  // region Info
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("终极之眼")
          .addInfo("终极之眼的控制器")
          .addInfo("执行鸿蒙之眼配方.")
          .addInfo("每个星阵提供8并行, 每个星矩提供2并行.")
          .addInfo("不产出能量, 直接从无线电网获取所需能量.")
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaGigaFac)

  override fun getInfoDataExtra(): Array<String> =
      arrayOf("${AQUA}执行并行: ${GOLD}${GTUtility.formatNumbers(pMultiplier)}")

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
    val tag = accessor!!.nbtData!!

    if (tag.getString("pDisplayNameW") != "NONE") {
      currentTip!!.plus("${WHITE}执行配方: ${AQUA}${tag.getString("pDisplayNameW")}")
    }
  }

  override fun getWailaNBTData(
      player: EntityPlayerMP?,
      tile: TileEntity?,
      tag: NBTTagCompound?,
      world: World?,
      x: Int,
      y: Int,
      z: Int
  ) {
    super.getWailaNBTData(player, tile, tag, world, x, y, z)
    val tileEntity = baseMetaTileEntity
    if (tileEntity?.isActive == true) {
      tag!!.setString("pDisplayNameW", pDisplayName)
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      it.setLong("pMultiplier", pMultiplier)
      it.setInteger("pBaseA", pBaseA)
      it.setInteger("pBaseB", pBaseB)
      it.setByte("pRecipeTime", pRecipeTime)
      it.setInteger("pSpacetimeCompressionFieldMetadata", pSpacetimeCompressionFieldMetadata)
      it.setString("pDisplayName", pDisplayName)

      val itemStackListNBTTag = NBTTagCompound()
      itemStackListNBTTag.setLong("EOUItems", outputItems.size.toLong())
      var indexItems = 0
      for (itemStackLong in outputItems) {
        itemStackListNBTTag.setLong("${indexItems}stackSize", itemStackLong.stackSize)
        it.setTag("${indexItems}itemStack", itemStackLong.itemStack.writeToNBT(NBTTagCompound()))
        indexItems++
      }
      it.setTag("EOUItemsTag", itemStackListNBTTag)

      val fluidStackListNBTTag = NBTTagCompound()
      fluidStackListNBTTag.setLong("EOUFluids", outputFluids.size.toLong())
      var indexFluids = 0
      for (fluidStackLong in outputFluids) {
        fluidStackListNBTTag.setLong("${indexFluids}amount", fluidStackLong.amount)
        aNBT.setTag(
            "${indexFluids}fluidStack", fluidStackLong.fluidStack.writeToNBT(NBTTagCompound()))
        indexFluids++
      }
      it.setTag("EOUFluidsTag", fluidStackListNBTTag)
    }
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      pMultiplier = it.getLong("pMultiplier")
      pBaseA = it.getInteger("pBaseA")
      pBaseB = it.getInteger("pBaseB")
      pRecipeTime = it.getByte("pRecipeTime")
      pSpacetimeCompressionFieldMetadata = it.getInteger("pSpacetimeCompressionFieldMetadata")
      pDisplayName = it.getString("pDisplayName")

      val tempItemTag = it.getCompoundTag("EOUItemsTag")
      for (indexItems in 0..tempItemTag.getInteger("EOUItems")) {
        val stackSize = tempItemTag.getLong("${indexItems}stackSize")
        val itemStack = ItemStack.loadItemStackFromNBT(it.getCompoundTag("${indexItems}itemStack"))
        outputItems.add(ItemStackLong(itemStack, stackSize))
      }

      val tempFluidTag = it.getCompoundTag("EOUFluidsTag")
      for (indexFluids in 0..tempFluidTag.getInteger("EOUFluids")) {
        val fluidAmount = tempFluidTag.getLong("${indexFluids}amount")
        val fluidStack =
            FluidStack.loadFluidStackFromNBT(it.getCompoundTag("${indexFluids}fluidStack"))
        outputFluids.add(FluidStackLong(fluidStack, fluidAmount))
      }
    }

    super.loadNBTData(aNBT)
  }
  // endregion
}
