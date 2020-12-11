package pk7r.simplexreports.model;

public enum ReportGetterType {

    ID("SELECT * FROM simplexreports WHERE id=?;"),
    SENDER("SELECT * FROM simplexreports WHERE sender=?;"),
    REPORTED("SELECT * FROM simplexreports WHERE reported=?;"),
    MESSAGE("SELECT * FROM simplexreports WHERE message=?;");

    private String sql;

    ReportGetterType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
