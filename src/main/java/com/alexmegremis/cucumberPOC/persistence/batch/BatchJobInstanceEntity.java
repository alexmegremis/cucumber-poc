package com.alexmegremis.cucumberPOC.persistence.batch;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "BATCH_JOB_INSTANCE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobInstanceEntity {

    @Id
    @Column (name = "JOB_INSTANCE_ID")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long   jobInstanceId;
    @Basic
    @Column (name = "JOB_KEY", nullable = false, length = 32)
    private String jobKey;
    @Basic
    @Column (name = "JOB_NAME", nullable = false, length = 100)
    private String jobName;
    @Basic
    @Column (name = "VERSION", nullable = true)
    private Long   version;

    @Override
    public String toString() {
        return "BatchJobInstanceEntity{" + "jobKey='" + jobKey + '\'' + ", jobName='" + jobName + '\'' + ", version=" + version + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BatchJobInstanceEntity that = (BatchJobInstanceEntity) o;
        return jobKey.equals(that.jobKey) && jobName.equals(that.jobName) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobKey, jobName, version);
    }
}
