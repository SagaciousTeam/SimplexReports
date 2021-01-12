package pk7r.simplexreports.commands.subcommands;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pk7r.simplexreports.Main;
import pk7r.simplexreports.commands.CMDBase;
import pk7r.simplexreports.model.Report;

import java.sql.SQLException;

public class ReportCommand implements CMDBase {

	public void onCommand(CommandSender s, String[] args) throws SQLException, ClassNotFoundException {
		Player sender = (Player) s;
		if ( (s instanceof Player) ) {
			if ( args.length < 1 ) {
				sender.spigot().sendMessage(MineDown.parse(Main.usage));
				return;
			}
			if ( args.length < 2 ) {
				sender.spigot().sendMessage(MineDown.parse(Main.errormessage));
				return;
			}
			Player reported = Bukkit.getPlayer(args[0]);
			String motivo = "";


			if ( reported == null ) {
				sender.spigot().sendMessage(MineDown.parse(Main.offlineplayer));
				return;
			}
			if ( reported == sender ) {
				sender.spigot().sendMessage(MineDown.parse(Main.reportme));
				return;

			}
			for (int i = 1; i <= args.length - 1; i++) {
				motivo = motivo + args[i];
				if ( i != args.length - 1 )
					motivo = motivo + " ";
			}

			Report report = new Report(sender.getName(), reported.getName(), motivo);
			report.saveAsync();
			sender.spigot().sendMessage(MineDown.parse(Main.sucessreport
					.replaceAll("%reported%", reported.getName())));
		} else {
			s.sendMessage("[SimplexReports] Console can't create reports. [ERROR]");
		}
	}

	public String name() {return "report";}

	public String info() { return ""; }

	public String[] aliases() { return new String[0]; }

}
