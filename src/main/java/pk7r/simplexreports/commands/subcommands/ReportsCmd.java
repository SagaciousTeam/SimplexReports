package pk7r.simplexreports.commands.subcommands;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pk7r.simplexreports.Main;
import pk7r.simplexreports.commands.CMDBase;
import pk7r.simplexreports.model.Report;
import pk7r.simplexreports.model.ReportManager;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReportsCmd implements CMDBase {

	@Override
	public void onCommand(CommandSender s, String[] args) throws SQLException, ClassNotFoundException {
		if ( !s.hasPermission("reports.admin") ) {
			s.sendMessage(Main.noperm);
			return;
		}
		Player p = (Player) s;
		/*
		 * Por se tratar de uma consulta no banco de dádos, é sempre recomendado realizar de maneira assíncrona
		 * para evitar travadas no servidor (Minecraft possúi apenas uma thread).
		 */
		Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {

			// Lista com os valores do banco de dados
			LinkedList<Report> reports = ReportManager.getLastReports();

			/*
			 * Construtor da mensagem
			 * Mesmo que não tenham 10 valores no banco, não dará erro
			 * Pois executa um forEach (Para cada)
			 */
			List<String> list = Main.getMain().messages.getStringList("reports_header");
			for (String line : list) {
				p.spigot().sendMessage(MineDown.parse(line));
			}
			reports.stream().forEachOrdered(r -> {
				p.spigot().sendMessage(MineDown.parse(Main.noperm3
				.replaceAll("%id%", String.valueOf(r.getID()))
				.replaceAll("%reporter%", r.getReporter())
				.replaceAll("%reported%", r.getReportado())
				.replaceAll("%date%",  r.getData().getDayOfMonth()+"§7/§f"+r.getData().getMonthValue())
				.replaceAll("%time%", r.getData().getHour()+"§7:§f"+r.getData().getMinute())
				.replaceAll("%message%", r.getMensagem())));
			});
			List<String> list2 = Main.getMain().messages.getStringList("reports_footer");
			for (String line2 : list2) {
				p.spigot().sendMessage(MineDown.parse(line2));
			}
		});
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "reports";
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