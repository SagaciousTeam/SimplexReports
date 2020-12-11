package pk7r.simplexreports.commands;

import java.util.ArrayList;
import pk7r.simplexreports.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pk7r.simplexreports.commands.subcommands.RInfoCmd;
import pk7r.simplexreports.commands.subcommands.ReportCommand;
import pk7r.simplexreports.commands.subcommands.ReportsCmd;

public class CMDManager implements CommandExecutor{

    private ArrayList<CMDBase> commands = new ArrayList<>();
    private Main plugin = Main.getMain();

    public void setup() {
        plugin.getCommand("report").setExecutor(this);
        plugin.getCommand("reports").setExecutor(this);
        plugin.getCommand("rinfo").setExecutor(this);
        this.commands.add(new ReportCommand());
        this.commands.add(new ReportsCmd());
        this.commands.add(new RInfoCmd());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CMDBase cmd = getCommand(command.getName());
        if (cmd == null) {
            return true;
        }
        try {
            cmd.onCommand(sender, args);
        } catch (Exception e){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUm erro ocorreu ao executar este comando! Por favor, contate um administrador!"));
            e.printStackTrace();
        }
        return true;
    }

    private CMDBase getCommand(String name) {
        for (CMDBase cmds : this.commands) {
            if (cmds.name().equalsIgnoreCase(name)) {
                return cmds;
            }
            String[] aliases;
            int length = (aliases = cmds.aliases()).length;
            for (int var = 0; var < length; ++var) {
                String alias = aliases[var];
                if (name.equalsIgnoreCase(alias)) {
                    return cmds;
                }
            }
        }
        return null;
    }

}
