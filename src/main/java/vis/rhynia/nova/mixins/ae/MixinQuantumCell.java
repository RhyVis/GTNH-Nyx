package vis.rhynia.nova.mixins.ae;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import appeng.core.api.definitions.ApiItems;
import appeng.items.storage.ItemExtremeStorageCell;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = ApiItems.class, remap = false)
public class MixinQuantumCell {

    @Redirect(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "(Ljava/lang/String;JIID)Lappeng/items/storage/ItemExtremeStorageCell;",
            ordinal = 1))
    private ItemExtremeStorageCell replaceQuantumCellRegistry(String name, long bytes, int types, int perType,
        double drain) {
        return new ItemExtremeStorageCell("Quantum", Integer.MAX_VALUE / 16, 63, 8, 1000);
    }
}
