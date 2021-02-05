package pk7r.simplexreports.commands.subcommands;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pk7r.simplexreports.Main;
import pk7r.simplexreports.commands.CMDBase;
import pk7r.simplexreports.model.Report;
import pk7r.simplexreports.model.ReportGetterType;
import pk7r.simplexreports.model.ReportManager;

import java.util.List;


public class RInfoCmd implements CMDBase {

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if ( (player instanceof Player) ) {
			if ( !player.hasPermission("reports.admin") ) {
				player.spigot().sendMessage(MineDown.parse(Main.no_permission));
				return;
			}
			if ( args.length < 1 ) {
				player.spigot().sendMessage(MineDown.parse(Main.rinfo_usage));
				return;
			}

			Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {
				Report reportOp = ReportManager.getReport(args[0], ReportGetterType.ID);


				if ( reportOp == null ) {
					player.spigot().sendMessage(MineDown.parse(Main.unkown_report));
					return;
				}

				String data = reportOp.getData().getDayOfMonth() + "§7/§f" + reportOp.getData().getMonthValue();
				String horario = reportOp.getData().getHour() + "§7:§f" + reportOp.getData().getMinute();
				List<String> list = Main.getMain().messages.getStringList("rinfo_message");
				for (String line : list) {
					if (player.hasPermission("reports.admin")) {
						player.spigot().sendMessage(MineDown.parse(line
								.replaceAll("%id%", String.valueOf(reportOp.getID()))
								.replaceAll("%reporter%", reportOp.getReporter())
								.replaceAll("%reported%", reportOp.getReportado())
								.replaceAll("%message%", reportOp.getMensagem())
								.replaceAll("%date%", data)
								.replaceAll("%time%", horario)));
					}
				}
			});
		} else {
			sender.sendMessage("[SimplexReports] Console can't view report details. [ERROR]");
		}
	}

	@Override
	public String name() {
		return "rinfo";
	}

	@Override
	public String info() {
		return "";
	}

	@Override
	public String[] aliases() {
		return new String[0];
	}
}