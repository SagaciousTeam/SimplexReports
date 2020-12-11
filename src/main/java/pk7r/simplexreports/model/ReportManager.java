package pk7r.simplexreports.model;

import org.bukkit.Bukkit;
import pk7r.simplexreports.Main;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;

public class ReportManager {

    public static Report getReport(String key, ReportGetterType tipo) {
        return get(key, tipo.getSql());
    }
    private static Report get(String key, String sql) {
        try {
            ResultSet rs = Main.getMain().getSQL().querySQL(sql.replace("?", key));
            if (!rs.next()) {
                return null;
            }
            do {
                int id = rs.getInt("id");
                String reporter = rs.getString("sender");
                String reported = rs.getString("reported");
                String text = rs.getString("message");
                LocalDateTime data = rs.getTimestamp("date").toLocalDateTime();

                return new Report(id, reporter, reported, text, data);
            } while (rs.next());
        } catch (Throwable t) {
            Bukkit.getConsoleSender().sendMessage("[SimplexReports] Failed to load data!");
        	t.printStackTrace();
        }
        return null;
    }
    
    public static LinkedList<Report> getLastReports() {
    	LinkedList<Report> reportes = new LinkedList<Report>();
    	try {
    		ResultSet rs = Main.getMain().getSQL().querySQL("SELECT * FROM simplexreports ORDER BY id DESC LIMIT " + Main.reports);
    		if (!rs.next()) {
                Bukkit.getConsoleSender().sendMessage("[SimplexReports] Failed to load data, empty table!");
    			return reportes;
    		}
    		do {
    			int id = rs.getInt("id");
                String reporter = rs.getString("sender");
                String reported = rs.getString("reported");
                String text = rs.getString("message");
                LocalDateTime data = rs.getTimestamp("date").toLocalDateTime();

                reportes.add(new Report(id, reporter, reported, text, data));
    		} while(rs.next());
    	} catch(Throwable t) {
            Bukkit.getConsoleSender().sendMessage("[SimplexReports] Failed to load data!");
    		t.printStackTrace();
    	}
    	return reportes;
    }
    
    public static int saveReport(Report report) {
        try {
            String sql = "INSERT INTO simplexreports(sender,reported,message,date) VALUES('"+report.getReporter()+"','"+report.getReportado()+"','"+
                    report.getMensagem()+"','"+report.getData().toString()+"');";

            Main.getMain().getSQL().updateSQL(sql);
            ResultSet rs = Main.getMain().getSQL().querySQL("SELECT id FROM simplexreports ORDER BY id DESC LIMIT 1;");
            if (!rs.next()) {
            	return 0;
            } do {
            	return rs.getInt(1);
            }while(rs.next());
        }catch(Throwable t) {
            Bukkit.getConsoleSender().sendMessage("[SimplexReports] Failed to load data!");
            t.printStackTrace();
            return 0;
        }
    }
}
