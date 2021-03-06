package world.avatarhorizon.spigot.lands.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import world.avatarhorizon.spigot.lands.commands.implementations.*;
import world.avatarhorizon.spigot.lands.controllers.LandsManager;
import world.avatarhorizon.spigot.lands.exceptions.LandCommandException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LandCommandExecutor implements CommandExecutor
{
    private ResourceBundle messages;
    private List<LandSubCommand> subCommands;

    public LandCommandExecutor(Logger logger, LandsManager landsManager, WorldEditPlugin worldEditPlugin)
    {
        messages = ResourceBundle.getBundle("messages/commands");
        subCommands = new LinkedList<>();

        subCommands.add(new CreateCommand(messages, logger, landsManager));
        subCommands.add(new DescriptionCommand(messages, logger, landsManager));
        subCommands.add(new RenameCommand(messages, logger, landsManager));
        subCommands.add(new AddChunksCommand(messages, logger, landsManager, worldEditPlugin));
        subCommands.add(new RemoveChunksCommand(messages, logger, landsManager, worldEditPlugin));
        subCommands.add(new SetTeleportCommand(messages, logger, landsManager));
        subCommands.add(new TeleportCommand(messages, logger, landsManager));
        subCommands.add(new DeleteCommand(messages, logger, landsManager));
        subCommands.add(new ListCommand(messages, logger, landsManager));
        subCommands.add(new HelpCommand(messages, logger, landsManager, subCommands));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        List<String> argsList = new LinkedList<>(Arrays.asList(args));

        String sub = argsList.remove(0).toLowerCase();

        for (LandSubCommand subCommand : subCommands)
        {
            if (subCommand.isCommand(sub))
            {
                try
                {
                    subCommand.execute(sender, argsList);
                    return true;
                }
                catch (LandCommandException ex)
                {
                    sender.sendMessage(ex.getMessage());
                    return true;
                }
            }
        }
        return false;
    }
}
