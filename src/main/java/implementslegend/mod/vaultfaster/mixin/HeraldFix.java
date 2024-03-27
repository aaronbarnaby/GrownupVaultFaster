package implementslegend.mod.vaultfaster.mixin;

import implementslegend.mod.vaultfaster.IndexedBlock;
import iskallia.vault.block.HeraldControllerBlock;
import iskallia.vault.core.world.data.entity.PartialCompoundNbt;
import iskallia.vault.core.world.data.tile.PartialBlockState;
import iskallia.vault.core.world.data.tile.PartialTile;
import iskallia.vault.core.world.template.StructureTemplate;
import iskallia.vault.init.ModBlocks;
import kotlin.jvm.internal.Ref;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.ref.Reference;
import java.util.List;

@Mixin(StructureTemplate.class)
public class HeraldFix {

    @Shadow private String path;

    @Inject(method = "loadPalette",at= @At(value = "INVOKE", target = "Liskallia/vault/core/world/template/StructureTemplate;getOrderedTiles(Ljava/util/List;Ljava/util/List;Ljava/util/List;)Ljava/util/List;"),locals = LocalCapture.CAPTURE_FAILHARD,remap = false)
    private void herald(ListTag paletteNBT, ListTag blocksNBT, CallbackInfo ci, List<PartialTile> normal, List<PartialTile> withNBT, List<PartialTile> withSpecialShape){
        if(path.equals("config/the_vault/gen/1.0/structures/vault/starts/herald.nbt")) {
            var pos = new BlockPos.MutableBlockPos(0,0,0);
            var found = new Ref.BooleanRef();
            withNBT.forEach((it) -> {
                if(((IndexedBlock)it.getState().getBlock()).getRegistryIndex()== ((IndexedBlock)ModBlocks.PLACEHOLDER).getRegistryIndex()){
                    it.setState(PartialBlockState.of(ModBlocks.HERALD_CONTROLLER.defaultBlockState().setValue(HeraldControllerBlock.HALF, DoubleBlockHalf.LOWER)));
                    it.setEntity(PartialCompoundNbt.of(new CompoundTag()));
                    pos.set(it.getPos()).move(Direction.UP);
                }

            });
            withNBT.forEach((it) -> {
                if(pos.equals(it.getPos())){
                    it.setState(PartialBlockState.of(ModBlocks.HERALD_CONTROLLER.defaultBlockState().setValue(HeraldControllerBlock.HALF, DoubleBlockHalf.UPPER)));
                    it.setEntity(PartialCompoundNbt.of(new CompoundTag()));
                    found.element=true;
                }
            });
            if(!found.element){
                withNBT.add(PartialTile.of(
                        PartialBlockState.of(ModBlocks.HERALD_CONTROLLER.defaultBlockState().setValue(HeraldControllerBlock.HALF, DoubleBlockHalf.UPPER)),
                PartialCompoundNbt.of(new CompoundTag()),pos));
            }
        }
    }
}
