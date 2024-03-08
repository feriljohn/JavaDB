package EntityConstructors;

import java.sql.Date;

public class BookCrud {
    private int bookId;
    private String bookName;
    private Date dop;
    private int authId;
    private int genId;
    private String damageName;
    private String repairStatus;
    private int severity;
    private int repairCost;
    private int damageId;

    public BookCrud(String bookName, Date dop, int authId, int genId) {
        this.bookName = bookName;
        this.dop = dop;
        this.authId = authId;
        this.genId = genId;
    }

    public BookCrud(int bookId, String bookName, Date dop, int authId, int genId) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.dop = dop;
        this.authId = authId;
        this.genId = genId;
    }

    public BookCrud(String bookName, Date dop, int authId, int genId, String damageName, String repairStatus,
            int severity, int repairCost) {
        this.bookName = bookName;
        this.dop = dop;
        this.authId = authId;
        this.genId = genId;
        this.damageName = damageName;
        this.repairStatus = repairStatus;
        this.severity = severity;
        this.repairCost = repairCost;
    }


    public BookCrud(int bookId, String bookName, Date dop, int authId, int genId, String damageName,
            String repairStatus) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.dop = dop;
        this.authId = authId;
        this.genId = genId;
        this.damageName = damageName;
        this.repairStatus = repairStatus;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getDop() {
        return dop;
    }

    public void setDop(Date dop) {
        this.dop = dop;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getGenId() {
        return genId;
    }

    public void setGenId(int genId) {
        this.genId = genId;
    }

    public String getDamageName() {
        return damageName;
    }

    public void setDamageName(String damageName) {
        this.damageName = damageName;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
    }

    public int getDamageId() {
        return damageId;
    }

    public void setDamageId(int damageId) {
        this.damageId = damageId;
    }

    @Override
    public String toString() {
        return "bookId=" + bookId + ", bookName=" + bookName + ", dop=" + dop + ", authId=" + authId + ", genId="
                + genId + ", damageName=" + damageName + ", repairStatus=" + repairStatus + ", severity=" + severity
                + ", repairCost=" + repairCost + ", damageId=" + damageId;
    }
}
