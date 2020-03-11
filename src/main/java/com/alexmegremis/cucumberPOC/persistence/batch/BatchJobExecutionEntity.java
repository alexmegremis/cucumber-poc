package com.alexmegremis.cucumberPOC.persistence.batch;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "BATCH_JOB_EXECUTION")
public class BatchJobExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BatchJobExecutionEntityGenerator")
    @GenericGenerator (name = "BatchJobExecutionEntityGenerator", strategy = "increment")
    @Column (name = "JOB_EXECUTION_ID")
    private Long      jobExecutionId;
    @Basic
    @Column (name = "JOB_INSTANCE_ID")
    private Long      jobInstanceId;
    @Basic
    @Column (name = "CREATE_TIME")
    private Timestamp createTime;
    @Basic
    @Column (name = "END_TIME")
    private Timestamp endTime;
    @Basic
    @Column (name = "EXIT_CODE")
    private String    exitCode;
    @Basic
    @Column (name = "EXIT_MESSAGE")
    private String    exitMessage;
    @Basic
    @Column (name = "JOB_CONFIGURATION_LOCATION")
    private String    jobConfigurationLocation;
    @Basic
    @Column (name = "LAST_UPDATED")
    private Timestamp lastUpdated;
    @Basic
    @Column (name = "START_TIME")
    private Timestamp startTime;
    @Basic
    @Column (name = "STATUS")
    private String    status;
    @Basic
    @Column (name = "VERSION")
    private Long      version;

    @Override
    public String toString() {
        return "BatchJobExecutionEntity{" + "jobExecutionId=" + jobExecutionId + ", jobInstanceId=" + jobInstanceId + ", createTime=" + createTime + ", endTime=" + endTime +
                       ", exitCode='" + exitCode + '\'' + ", exitMessage='" + exitMessage + '\'' + ", jobConfigurationLocation='" + jobConfigurationLocation + '\'' +
                       ", lastUpdated=" + lastUpdated + ", startTime=" + startTime + ", status='" + status + '\'' + ", version=" + version + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BatchJobExecutionEntity that = (BatchJobExecutionEntity) o;
        return getJobExecutionId().equals(that.getJobExecutionId()) && Objects.equals(getCreateTime(), that.getCreateTime()) && Objects.equals(getEndTime(), that.getEndTime()) &&
                       Objects.equals(getExitCode(), that.getExitCode()) && Objects.equals(getExitMessage(), that.getExitMessage()) &&
                       Objects.equals(getJobConfigurationLocation(), that.getJobConfigurationLocation()) && Objects.equals(getLastUpdated(), that.getLastUpdated()) &&
                       Objects.equals(getStartTime(), that.getStartTime()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobExecutionId(), getCreateTime(), getEndTime(), getExitCode(), getExitMessage(), getJobConfigurationLocation(), getLastUpdated(), getStartTime(),
                            getStatus(), getVersion());
    }
}
