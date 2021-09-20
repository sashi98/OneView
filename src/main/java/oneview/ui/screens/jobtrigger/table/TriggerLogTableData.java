package oneview.ui.screens.jobtrigger.table;

import oneview.ui.screens.common.table.TableData;

import java.util.Objects;

import static oneview.ui.constants.GuiConstants.*;

public class TriggerLogTableData implements TableData {
    private String jobName;
    private String triggeredAt;
    private String startDate;
    private String endDate;
    private String status;
    private ErrorLogInfo info;


    public TriggerLogTableData() {
        this.status = BUSY_SPIN;
        this.info = new ErrorLogInfo(OK);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(String triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ErrorLogInfo getInfo() {
        return info;
    }

    public void setInfo(ErrorLogInfo info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TriggerLogTableData)) return false;
        TriggerLogTableData that = (TriggerLogTableData) o;
        return getStatus() == that.getStatus() &&
                getJobName().equals(that.getJobName()) &&
                getTriggeredAt().equals(that.getTriggeredAt()) &&
                getStartDate().equals(that.getStartDate()) &&
                getEndDate().equals(that.getEndDate()) &&
                getInfo().equals(that.getInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobName(), getTriggeredAt(), getStartDate(), getEndDate(), getStatus(), getInfo());
    }

    @Override
    public String toString() {
        return "TriggerLogTableData{" +
                "jobName='" + jobName + '\'' +
                ", triggeredAt='" + triggeredAt + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status=" + status +
                ", info=" + info +
                '}';
    }
}
