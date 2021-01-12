package pk7r.simplexreports.model;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;
import pk7r.simplexreports.Main;

import java.time.LocalDateTime;
import java.util.List;

public class Report {

    int id;
    final String reporterName;
    final String reportadoName;
    String mensagem;
    LocalDateTime data;

    /**
     * Cria um novo report sem o ID.
     * A data é atribuida ao momento atual.<br>
     *
     * @param reporterName
     *          Quem reportou
     * @param reportadoName
     *          Quem foi reportado
     * @param mensagem
     *          O texto do reporte enviado pelo jogador
     */
    public Report(String reporterName, String reportadoName, String mensagem) {
        this.reporterName = reporterName;
        this.reportadoName = reportadoName;
        this.mensagem = mensagem;
        this.data = LocalDateTime.now();
    }

    /**
     * Cria um novo report completo.<br>
     *
     * @param id
     *          ID do report no banco de dados
     * @param reporterName
     *          Quem reportou
     * @param reportadoName
     *          Quem foi reportado
     * @param mensagem
     *          O texto do report enviado pelo jogador
     * @param data
     *          Data em que o reporte foi enviado
     */
    public Report(int id, String reporterName, String reportadoName, String mensagem, LocalDateTime data) {
        this.id = id;
        this.reporterName = reporterName;
        this.reportadoName = reportadoName;
        this.mensagem = mensagem;
        this.data = data;
    }

    /**
     * Salva o reporte no banco de dados assíncronamente.
     */
    public void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {
          this.id = ReportManager.saveReport(this);
          adminWarn();
        });
    }

    /**
     * Envia uma mensagem a todos os administradores online sobre o reporte.
     * Chamado após o salvamento no banco de dados ser concluído e o ID for obtível.
     */
    public void adminWarn() {
        List<String> list = Main.getMain().messages.getStringList("admin_warn");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {
            for (String line : list) {
                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("reports.admin")).forEach(p -> {
                    p.spigot().sendMessage(MineDown.parse(line
                            .replaceAll("%id%", "" + id)
                            .replaceAll("%reporter%", reporterName)
                            .replaceAll("%reported%", reportadoName)
                            .replaceAll("%message%", mensagem)
                            .replaceAll("%day%", String.valueOf(data.getDayOfMonth()))
                            .replaceAll("%month%", String.valueOf(data.getMonthValue()))
                            .replaceAll("%year%", String.valueOf(data.getYear()))
                            .replaceAll("%hour%", String.valueOf(data.getHour()))
                            .replaceAll("%minute%", String.valueOf(data.getMinute()))
                            .replaceAll("%second%", String.valueOf(data.getSecond()))));
                });
            }
        });
    }
    
    /**
     * Coleta o ID do reporte.
     * 
     * @return int
     */
    public int getID() {
        return this.id;
    }
    /**
     * Define um novo ID ao reporte.
     * 
     * @deprecated
     * @param id Novo ID do reporte
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do jogador que fez o reporte.
     * 
     * @return String
     */
    public String getReporter() {
        return this.reporterName;
    }
    
    /**
     * Retorna o jogador que foi reportado
     * 
     * @return String
     */
    public String getReportado() {
        return this.reportadoName;
    }
    
    /**
     * Texto enviado pelo jogador ao reportar.
     * 
     * @return String
     */
    public String getMensagem() {
        return this.mensagem;
    }

    /**
     * Data e hora na qual o reporte foi feito.
     * 
     * @return LocalDateTime
     */
    public LocalDateTime getData() {
        return this.data;
    }
    /**
     * Define uma nova data ao reporte.
     * 
     * @param data Nova data a ser atribuída ao reporte
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
