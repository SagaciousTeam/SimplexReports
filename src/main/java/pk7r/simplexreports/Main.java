package pk7r.simplexreports;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pk7r.simplexreports.commands.CMDManager;
import pk7r.simplexreports.db.Database;
import pk7r.simplexreports.db.MySQL;
import pk7r.simplexreports.db.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    public File messages1;
    public YamlConfiguration messages;
    public static String noperm;
    public static String noperm2;
    public static String noperm3;
    public static String usage;
    public static String errormessage;
    public static String sucessreport;
    public static String offlineplayer;
    public static String reportme;
    public static String admin_warn;
    public static String rinfo_message;
    public static String unkown_report;
    public static Integer reports;
    public static String reports_header;
    public static String reports_footer;

    public String host, database, username, password, table;

    public int port;

    private Database sql;

    private static Main main;

    private CMDManager cmdManager;

    private CommandSender cs = (CommandSender) Bukkit.getConsoleSender();

    public void onEnable() {

        setMain(this);

        this.cmdManager = new CMDManager();
        this.cmdManager.setup();

        getConfig().options().copyDefaults(true);

        saveDefaultConfig();

        if (!setupSQL()) {
            cs.sendMessage(ChatColor.RED + "[SimplexReports] Error connecting to SQL! Plugin disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        cs.sendMessage(String.format("[SimplexReports] Plugin enabled"));
        final File messages = new File(this.getDataFolder(), "messages.yml");
        if (!messages.exists()) {
            this.saveResource("messages.yml", false);
        }
        this.messages1 = new File(this.getDataFolder(), "messages.yml");
        this.messages = YamlConfiguration.loadConfiguration(this.messages1);
        noperm = getMain().messages.getString("no_permission").replace("&","§");
        noperm2 = getMain().messages.getString("rinfo_usage").replace("&","§");
        noperm3 = getMain().messages.getString("reports_message").replace("&","§");
        reports = Integer.valueOf(getMain().getConfig().getString("reports_list")).replace("&","§");
        reports_header  = getMain().messages.getString("reports_header").replace("&","§");
        reports_footer  = getMain().messages.getString("reports_footer").replace("&","§");
        usage  = getMain().messages.getString("correct_usage").replace("&","§");
        sucessreport  = getMain().messages.getString("sucess_report").replace("&","§");
        rinfo_message  = getMain().messages.getString("rinfo_message").replace("&","§");
        errormessage  = getMain().messages.getString("report_missing_message").replace("&","§");
        admin_warn  = getMain().messages.getString("admin_warn").replace("&","§");
        offlineplayer  = getMain().messages.getString("unkown_player").replace("&","§");
        reportme  = getMain().messages.getString("report_me").replace("&","§");
        unkown_report  = getMain().messages.getString("unkown_report").replace("&","§");

    }

    public void onDisable() {
        cs.sendMessage(String.format("[SimplexReports] Plugin disabled!"));
        Bukkit.getPluginManager().disablePlugin(this);
    }
    
    private boolean setupSQL() {
        if (getConfig().getBoolean("mysql.enabled")) {
            String host, port, database, user, pass;
            host = getConfig().getString("mysql.host");
            port = getConfig().getString("mysql.port");
            database = getConfig().getString("mysql.database");
            user = getConfig().getString("mysql.user");
            pass = getConfig().getString("mysql.passwd");
            sql = new MySQL(host, port, database, user, pass);
            cs.sendMessage("[SimplexReports] Using MySQL database!");
        } else {
            String dbLoc = getConfig().getString("sqlite.database");
            sql = new SQLite(dbLoc);
            cs.sendMessage("[SimplexReports] Using SQLite database!");
        }
        try { 
        	sql.updateSQL("CREATE TABLE IF NOT EXISTS `simplexreports` (`id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT, `sender` VARCHAR(16) NOT NULL, `reported` VARCHAR(16) NOT NULL, `message` TEXT NOT NULL, `date` DATETIME NOT NULL);");
        	}
        catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }

        return sql != null;
    }
    public void save() {
        try {
            this.messages.save(this.messages1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Database getSQL() {
        return sql;
    }

    private static void setMain(Main main) {
        Main.main = main;
    }

    public static Main getMain() {
        return Main.main;
    }
}
