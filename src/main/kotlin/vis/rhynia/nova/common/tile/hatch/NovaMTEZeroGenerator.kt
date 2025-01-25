package vis.rhynia.nova.common.tile.hatch

import com.gtnewhorizons.modularui.api.drawable.IDrawable
import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.api.widget.Widget
import com.gtnewhorizons.modularui.api.widget.Widget.ClickData
import com.gtnewhorizons.modularui.common.widget.ButtonWidget
import com.gtnewhorizons.modularui.common.widget.DrawableWidget
import com.gtnewhorizons.modularui.common.widget.TextWidget
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.enums.GTValues.VN
import gregtech.api.enums.Textures
import gregtech.api.gui.modularui.GTUIInfos
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.modularui.IAddGregtechLogo
import gregtech.api.interfaces.modularui.IAddUIWidgets
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock
import gregtech.api.objects.GTRenderedTexture
import gregtech.api.util.GTUtility
import gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap
import java.util.UUID
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.math.abs
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT
import tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_LASER_TT
import tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT
import tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT
import tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_POWER_TT
import tectech.util.CommonValues.TRANSFER_AT
import tectech.util.TTUtility
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.enums.NovaValues.TextureSets.NovaLogo32

@Suppress("unused", "deprecated")
class NovaMTEZeroGenerator : MTETieredMachineBlock, IAddUIWidgets, IAddGregtechLogo {
  private var wireless = false
  private var ownerUUID: UUID? = null
  var eUT: Int = 0
  var aMP: Int = 0
  var producing: Boolean = true

  constructor(
      aID: Int,
      aName: String?,
      aNameRegional: String?,
      aTier: Int
  ) : super(
      aID,
      aName,
      aNameRegional,
      aTier,
      0,
      arrayOf<String?>(
          "从虚空中抽取能量",
          "使用方式与Debug发电机无异",
          "使用螺丝刀切换至无线模式",
          "直接注入能量到无线网络!",
          NovaValues.CommonStrings.AddByNova)) {
    TTUtility.setTier(aTier, this)
  }

  constructor(
      aName: String?,
      aTier: Int,
      aDescription: Array<String?>?,
      aTextures: Array<Array<Array<ITexture?>?>?>?
  ) : super(aName, aTier, 0, aDescription, aTextures) {
    TTUtility.setTier(aTier, this)
  }

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity {
    return NovaMTEZeroGenerator(mName, mTier.toInt(), mDescriptionArray, mTextures)
  }

  @Deprecated("Deprecated in Java")
  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    wireless = !wireless
    if (wireless) {
      GTUtility.sendChatToPlayer(aPlayer, "无线模式:启动")
    } else {
      GTUtility.sendChatToPlayer(aPlayer, "无线模式:关闭")
    }
  }

  @SideOnly(Side.CLIENT)
  override fun registerIcons(aBlockIconRegister: IIconRegister?) {
    super.registerIcons(aBlockIconRegister)
    GENNY = GTRenderedTexture(Textures.BlockIcons.CustomIcon("iconsets/GENNY"))
  }

  override fun getTexture(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      side: ForgeDirection?,
      facing: ForgeDirection?,
      colorIndex: Int,
      aActive: Boolean,
      aRedstone: Boolean
  ): Array<ITexture?> {
    return arrayOf<ITexture?>(
        MACHINE_CASINGS_TT[mTier.toInt()][colorIndex + 1],
        if (side != facing)
            if (wireless)
                (if (aActive) OVERLAYS_ENERGY_OUT_LASER_TT[mTier.toInt()]
                else OVERLAYS_ENERGY_IN_LASER_TT[mTier.toInt()])
            else
                (if (aActive) OVERLAYS_ENERGY_OUT_POWER_TT[mTier.toInt()]
                else OVERLAYS_ENERGY_IN_POWER_TT[mTier.toInt()])
        else GENNY)
  }

  override fun getTextureSet(aTextures: Array<ITexture?>?): Array<Array<Array<ITexture?>?>?>? {
    return null
  }

  override fun allowPutStack(
      iGregTechTileEntity: IGregTechTileEntity?,
      i: Int,
      side: ForgeDirection?,
      itemStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun allowPullStack(
      iGregTechTileEntity: IGregTechTileEntity?,
      i: Int,
      side: ForgeDirection?,
      itemStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun saveNBTData(aNBT: NBTTagCompound) {
    aNBT.setInteger("eEUT", this.eUT)
    aNBT.setInteger("eAMP", this.aMP)
    aNBT.setBoolean("eWireless", wireless)
  }

  override fun loadNBTData(aNBT: NBTTagCompound) {
    this.eUT = aNBT.getInteger("eEUT")
    this.aMP = aNBT.getInteger("eAMP")
    wireless = aNBT.getBoolean("eWireless")
    producing = aMP.toLong() * this.eUT >= 0
    baseMetaTileEntity.isActive = producing
  }

  override fun isSimpleMachine(): Boolean = false

  override fun onPostTick(aBaseMetaTileEntity: IGregTechTileEntity, aTick: Long) {
    ownerUUID = aBaseMetaTileEntity.ownerUuid
    if (aBaseMetaTileEntity.isServerSide) {
      aBaseMetaTileEntity.isActive = producing
      if (!wireless) {
        euVar =
            if (aBaseMetaTileEntity.isActive) {
              maxEUStore()
            } else {
              0
            }
      } else {
        val t = (aTick % 20).toByte()
        if (aBaseMetaTileEntity.isActive && TRANSFER_AT == t) {
          euVar = maxEUStore()
          addEUToGlobalEnergyMap(ownerUUID, abs(euVar))
        } else if (TRANSFER_AT == t) {
          euVar = 0
        }
      }
    }
  }

  override fun onRightclick(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aPlayer: EntityPlayer?
  ): Boolean {
    GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer)
    return true
  }

  override fun isFacingValid(facing: ForgeDirection?): Boolean {
    return true
  }

  override fun isAccessAllowed(aPlayer: EntityPlayer?): Boolean {
    return true
  }

  override fun isElectric(): Boolean = true

  override fun isEnetOutput(): Boolean = !wireless

  override fun isEnetInput(): Boolean = !wireless

  override fun isInputFacing(side: ForgeDirection?): Boolean {
    return !producing && side != baseMetaTileEntity.frontFacing
  }

  override fun isOutputFacing(side: ForgeDirection?): Boolean {
    return producing && side != baseMetaTileEntity.frontFacing
  }

  override fun maxAmperesIn(): Long {
    return (if (producing) 0 else abs(aMP.toDouble()).toInt()).toLong()
  }

  override fun maxAmperesOut(): Long {
    return (if (producing) abs(aMP.toDouble()).toInt() else 0).toLong()
  }

  override fun maxEUInput(): Long {
    return (if (producing) 0 else Int.Companion.MAX_VALUE).toLong()
  }

  override fun maxEUOutput(): Long {
    return (if (producing) abs(eUT.toDouble()).toInt() else 0).toLong()
  }

  override fun maxEUStore(): Long {
    return if (wireless) abs((eUT.toLong() * this.aMP * 24).toDouble()).toLong()
    else (abs((eUT.toLong() * this.aMP).toDouble()).toInt() shl 2).toLong()
  }

  fun minimumStoredEU(): Long = abs((eUT.toLong() * this.aMP).toDouble()).toLong()

  fun progresstime(): Int = baseMetaTileEntity.universalEnergyStored.toInt()

  override fun maxProgresstime(): Int {
    return baseMetaTileEntity.universalEnergyCapacity.toInt()
  }

  fun useModularUI(): Boolean {
    return true
  }

  override fun addGregTechLogo(builder: ModularWindow.Builder) {
    builder.widget(DrawableWidget().setDrawable(NovaLogo32).setSize(17, 17).setPos(113, 56))
  }

  override fun addUIWidgets(builder: ModularWindow.Builder, buildContext: UIBuildContext?) {
    builder
        .widget(
            DrawableWidget()
                .setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
        .widget(
            TextWidget.dynamicString(
                    Supplier {
                      "TIER: " + VN[GTUtility.getTier(abs(eUT.toDouble()).toLong()).toInt()]
                    })
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 22))
        .widget(
            TextWidget.dynamicString(Supplier { "SUM: " + aMP.toLong() * this.eUT })
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 46))

    addLabelledIntegerTextField(
        builder,
        "EUT: ",
        24,
        Supplier { this.eUT },
        Consumer { EUT: Int? -> this.eUT = EUT!! },
        46,
        8)
    addLabelledIntegerTextField(
        builder,
        "AMP: ",
        24,
        Supplier { this.aMP },
        Consumer { AMP: Int? -> this.aMP = AMP!! },
        46,
        34)

    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, { it -> eUT -= it!! }, 512, 64, 7, 4)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, { it -> eUT /= it!! }, 512, 64, 7, 22)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, { it -> aMP -= it!! }, 512, 64, 7, 40)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, { it -> aMP /= it!! }, 512, 64, 7, 58)

    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, { it -> eUT -= it!! }, 16, 1, 25, 4)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, { it -> eUT /= it!! }, 16, 2, 25, 22)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, { it -> aMP -= it!! }, 16, 1, 25, 40)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, { it -> aMP /= it!! }, 16, 2, 25, 58)

    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, { it -> eUT += it!! }, 16, 1, 133, 4)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, { it -> eUT *= it!! }, 16, 2, 133, 22)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, { it -> aMP += it!! }, 16, 1, 133, 40)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, { it -> aMP *= it!! }, 16, 2, 133, 58)

    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, { it -> eUT += it!! }, 512, 64, 151, 4)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, { it -> eUT *= it!! }, 512, 64, 151, 22)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, { it -> aMP += it!! }, 512, 64, 151, 40)
    addChangeNumberButton(
        builder, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE, { it -> aMP *= it!! }, 512, 64, 151, 58)
  }

  private fun addLabelledIntegerTextField(
      builder: ModularWindow.Builder,
      label: String,
      labelWidth: Int,
      getter: Supplier<Int?>?,
      setter: Consumer<Int?>?,
      xPos: Int,
      yPos: Int
  ) {
    val itfw = TextFieldWidget()
    val ltw = TextWidget(label)
    builder
        .widget(ltw.setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(xPos, yPos))
        .widget(
            itfw
                .setSetterInt(setter)
                .setGetterInt(getter)
                .setTextColor(COLOR_TEXT_WHITE.get())
                .setBackground(
                    GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(
                        (-1).toFloat(), (-1).toFloat(), (2).toFloat(), (2).toFloat()))
                .setPos(xPos + labelWidth, yPos - 1)
                .setSize(56, 10))
  }

  private fun addChangeNumberButton(
      builder: ModularWindow.Builder,
      overlay: IDrawable?,
      setter: Consumer<Int?>,
      changeNumberShift: Int,
      changeNumber: Int,
      xPos: Int,
      yPos: Int
  ) {
    builder.widget(
        ButtonWidget()
            .setOnClick(
                BiConsumer { clickData: ClickData?, widget: Widget? ->
                  setter.accept(if (clickData!!.shift) changeNumberShift else changeNumber)
                  producing = aMP.toLong() * this.eUT >= 0
                })
            .setBackground(GTUITextures.BUTTON_STANDARD, overlay)
            .setSize(18, 18)
            .setPos(xPos, yPos))
  }

  companion object {
    var GENNY: GTRenderedTexture? = null
  }
}
