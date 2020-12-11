package pk7r.simplexreports.commands.subcommands;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pk7r.simplexreports.Main;
import pk7r.simplexreports.commands.CMDBase;
import pk7r.simplexreports.model.Report;
import pk7r.simplexreports.model.ReportGetterType;
import pk7r.simplexreports.model.ReportManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class RInfoCmd implements CMDBase {

	@Override
	public void onCommand(CommandSender s, String[] args) throws SQLException, ClassNotFoundException {
		if (!s.hasPermission("reports.admin")) {
			s.sendMessage(Main.noperm);
			return;
		}
		if (args.length<1) {
			s.sendMessage(Main.noperm2);
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {
			Report reportOp = ReportManager.getReport(args[0], ReportGetterType.ID);


			if (reportOp == null) {
				s.sendMessage(Main.unkown_report);
				return;
			}
			Report r = reportOp;
			String data = r.getData().getDayOfMonth()+"§7/§f"+r.getData().getMonthValue();
			String horario = r.getData().getHour()+"§7:§f"+r.getData().getMinute();
			List<String> list = Main.getMain().messages.getStringList("rinfo_message");
			for (String line : list) {
				Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("reports.admin")).forEach(p -> {
					p.spigot().sendMessage(MineDown.parse(line
							.replaceAll("%id%", String.valueOf(r.getID()))
							.replaceAll("%reporter%", r.getReporter())
							.replaceAll("%reported%", r.getReportado())
							.replaceAll("%message%", r.getMensagem())
							.replaceAll("%date%", data)
							.replaceAll("%time%", horario)));
				});
			}
		});
		return;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "rinfo";
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String[] aliases() {
		// TODO Auto-generated method stub
		return new String[0];
	}
}