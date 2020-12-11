package pk7r.simplexreports.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pk7r.simplexreports.Main;
import pk7r.simplexreports.commands.CMDBase;
import pk7r.simplexreports.model.Report;

import java.sql.SQLException;

public class ReportCommand implements CMDBase {

	public void onCommand(CommandSender sender, String[] args) throws SQLException, ClassNotFoundException {

		if (args.length < 1) {
			sender.sendMessage(Main.usage);
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(Main.errormessage);
			return;
		}
		Player reported = Bukkit.getPlayer(args[0]);
		String motivo = "";


		if (reported == null) {
			sender.sendMessage(Main.offlineplayer);
			return;
		}
		if (reported == sender) {
			sender.sendMessage(Main.reportme);
			return;

		}
		for (int i = 1; i <= args.length - 1; i++) {
			motivo = motivo + args[i];
			if (i != args.length - 1)
				motivo = motivo + " ";
		}

		Report report = new Report(sender.getName(), reported.getName(), motivo);
		report.saveAsync();
		report.adminWarn();
		sender.sendMessage(Main.sucessreport
				.replaceAll("%reported%",reported.getName()));
	}

	public String name() {return "report";}

	public String info() { return ""; }

	public String[] aliases() { return new String[0]; }

}
