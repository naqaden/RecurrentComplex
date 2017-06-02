/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://ivorius.net
 */

package ivorius.reccomplex.commands.structure.sight;

import ivorius.reccomplex.RCConfig;
import ivorius.reccomplex.commands.RCCommands;
import ivorius.reccomplex.commands.RCTextStyle;
import ivorius.reccomplex.commands.parameters.RCExpect;
import ivorius.reccomplex.commands.parameters.RCParameters;
import ivorius.reccomplex.utils.ServerTranslations;
import ivorius.reccomplex.world.gen.feature.WorldStructureGenerationData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lukas on 25.05.14.
 */
public class CommandSightCheck extends CommandBase
{
    private String name;

    public CommandSightCheck(String name)
    {
        this.name = name;
    }

    @Nonnull
    public static ITextComponent entryTextComponent(WorldStructureGenerationData.Entry entry)
    {
        TextComponentString forget = new TextComponentString("X");
        String uuidString = entry.getUuid().toString();
        forget.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/%s %s id %s", RCCommands.sight.getName(), RCCommands.sight.delete.getName(), uuidString)));
        forget.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                ServerTranslations.format("commands.rcforget.forget", uuidString)));
        forget.getStyle().setColor(TextFormatting.RED);

        ITextComponent structure = entry instanceof WorldStructureGenerationData.StructureEntry
                ? RCTextStyle.structure(entry.description())
                : new TextComponentString(entry.description());

        return new TextComponentTranslation("%s (%s)", structure, forget);
    }

    @Override
    public String getName()
    {
        return name;
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender var1)
    {
        return ServerTranslations.usage("commands.whatisthis.usage");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException
    {
        RCParameters parameters = RCParameters.of(args);
        World world = commandSender.getEntityWorld();

        BlockPos pos = parameters.mc().pos(commandSender.getPosition(), false).require();

        List<WorldStructureGenerationData.Entry> entries = WorldStructureGenerationData.get(world).entriesAt(pos).collect(Collectors.toCollection(ArrayList::new));
        if (entries.size() > 0)
            commandSender.sendMessage(ServerTranslations.format(new ArrayList<ITextComponent>().size() > 1 ? "commands.whatisthis.many" : "commands.whatisthis.one",
                    ServerTranslations.join(entries.stream().map(CommandSightCheck::entryTextComponent).collect(Collectors.toList()))));
        else
            commandSender.sendMessage(ServerTranslations.format("commands.whatisthis.none"));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return RCExpect.startRC()
                .xyz()
                .get(server, sender, args, pos);
    }
}