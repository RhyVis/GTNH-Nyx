package rhynia.nyx.common.mte;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import rhynia.nyx.common.mte.base.NyxMTEBase;
import rhynia.nyx.common.mte.prod.NyxProxy;

import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;

public class TestMTE extends MTEExtendedPowerMultiBlockBase<TestMTE> {
    public TestMTE(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TestMTE(String aName) {
        super(aName);
    }

    private NyxMTEBase.ModeContainer<RecipeMap<?>> modeContainer;

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (modeContainer == null) {
            return null;
        }
        return modeContainer.getCurrent();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {
            @Override
            public @NotNull CheckRecipeResult process() {
                if (getControllerSlot() != null) {
                    modeContainer = NyxProxy.Companion.getRecipeMap(getControllerSlot());
                }
                return super.process();
            }
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("Main", stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public IStructureDefinition<TestMTE> getStructureDefinition() {
        return StructureDefinition
            .<TestMTE>builder()
            .addShape("Main", StructureUtility.transpose(
                new String[][]{
                    new String[]{
                        "CCC",
                        "CCC",
                        "CCC",
                    },
                    new String[]{
                        "C~C",
                        "C C",
                        "CCC",
                    },
                    new String[]{
                        "CCC",
                        "CCC",
                        "CCC",
                    },
                }
            )).addElement('C', HatchElementBuilder.<TestMTE>builder().atLeast(InputBus, OutputBus, Energy.or(ExoticEnergy)).adder(this::addM).dot(1).casingIndex(index).buildAndChain(StructureUtility.ofBlock(GregTechAPI.sBlockCasings2, 0)))
            .build();
    }

    private boolean addM(TestMTE testMTE, IGregTechTileEntity iGregTechTileEntity, Short aShort) {
        return addToMachineList(iGregTechTileEntity, aShort);
    }

    private static int index = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0);

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo("What?").toolTipFinisher("Just a test MTE");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece("Main", 1, 1, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TestMTE(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        if (side != facing) {
            return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(index)};
        } else if (active) {
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(index),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE).extFacing().glow().build()
            };
        } else {
            return new ITexture[]{
                Textures.BlockIcons.getCasingTextureForId(index),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE).extFacing().glow().build()
            };
        }
    }
}
