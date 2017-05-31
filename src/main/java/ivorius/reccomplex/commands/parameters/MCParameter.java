/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://ivorius.net
 */

package ivorius.reccomplex.commands.parameters;

import ivorius.reccomplex.utils.ServerTranslations;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by lukas on 31.05.17.
 */
public class MCParameter extends Parameter
{
    public MCParameter(Parameter other)
    {
        super(other);
    }

    // Since CommandBase's version requires a sender
    public static BlockPos parseBlockPos(BlockPos blockpos, String[] args, int startIndex, boolean centerBlock) throws NumberInvalidException
    {
        return new BlockPos(CommandBase.parseDouble((double) blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), CommandBase.parseDouble((double) blockpos.getY(), args[startIndex + 1], 0, 256, false), CommandBase.parseDouble((double) blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
    }

    @Override
    public MCParameter move(int idx)
    {
        return new MCParameter(super.move(idx));
    }

    public Result<BlockPos> pos(BlockPos ref, boolean centerBlock)
    {
        return at(0).failable().flatMap(x -> at(1).flatMap(y -> at(2).map(z ->
                parseBlockPos(ref, new String[]{x, y, z}, 0, centerBlock))))
                .orElse(() -> ref);
    }

    public Result<Biome> biome()
    {
        return at(0).map(ResourceLocation::new)
                .map(Biome.REGISTRY::getObject, t -> ServerTranslations.commandException("commands.rc.nobiome"));
    }

    public Result<WorldServer> dimension(ICommandSender commandSender)
    {
        return at(0).filter(d -> !d.equals("~"), null).failable()
                .map(CommandBase::parseInt).map(DimensionManager::getWorld, t -> ServerTranslations.commandException("commands.rc.nodimension"))
                .orElse(() -> (WorldServer) commandSender.getEntityWorld());
    }

    public Result<Block> block(ICommandSender commandSender)
    {
        return at(0).map(s -> CommandBase.getBlockByText(commandSender, s));
    }
}
