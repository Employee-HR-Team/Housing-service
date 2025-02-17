package com.beaconfire.housing.housing_service.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FacilityReport")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityID", nullable = false)
    private Facility facility;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeId;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private ReportStatus status = ReportStatus.Open;

    @Column(name = "CreateDate", nullable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "facilityReport", 
              fetch = FetchType.LAZY, 
              cascade = CascadeType.ALL)
    private List<FacilityReportDetail> reportDetails;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    public enum ReportStatus {
        Open, InProgress, Closed
    }
}