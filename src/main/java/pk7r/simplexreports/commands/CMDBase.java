package pk7r.simplexreports.commands;

import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public interface CMDBase {

    void onCommand(CommandSender sender, String[] args) throws SQLException, ClassNotFoundException;

    String name();

    String info();

    String[] aliases();
}