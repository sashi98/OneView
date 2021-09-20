package oneview.ui.screens.jobtrigger.table;

import java.util.Objects;

public class ErrorLogInfo {
    private String info;
    private String log;

    public ErrorLogInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorLogInfo)) return false;
        ErrorLogInfo that = (ErrorLogInfo) o;
        return getInfo() == that.getInfo() &&
                getLog().equals(that.getLog());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInfo(), getLog());
    }

    @Override
    public String toString() {
        return "ErrorLogInfo{" +
                "info=" + info +
                ", log='" + log + '\'' +
                '}';
    }
}
