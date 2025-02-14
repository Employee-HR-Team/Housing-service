package com.beaconfire.housing.housing_service.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beaconfire.housing.housing_service.entity.Facility;
import com.beaconfire.housing.housing_service.entity.FacilityReport;

@Repository
public class FacilityReportRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public FacilityReport findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(FacilityReport.class, id);
    }
    
    public FacilityReport save(FacilityReport report) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(report);
        return report;
    }
    
    public void delete(FacilityReport report) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(report);
    }
    
    public List<FacilityReport> findByFacility(Facility facility) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.facility = :facility",
            FacilityReport.class)
            .setParameter("facility", facility)
            .list();
    }
    
    public List<FacilityReport> findByFacility(Facility facility, int firstResult, int maxResults) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.facility = :facility",
            FacilityReport.class)
            .setParameter("facility", facility)
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .list();
    }
    
    public List<FacilityReport> findByEmployeeId(Integer employeeId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.employeeId = :employeeId",
            FacilityReport.class)
            .setParameter("employeeId", employeeId)
            .list();
    }

    public List<FacilityReport> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr ORDER BY fr.createDate DESC",
            FacilityReport.class)
            .list();
    }
    
    public List<FacilityReport> findByStatus(FacilityReport.ReportStatus status) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.status = :status",
            FacilityReport.class)
            .setParameter("status", status)
            .list();
    }
    
    public List<FacilityReport> findByFacilityAndStatus(Facility facility, FacilityReport.ReportStatus status) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.facility = :facility AND fr.status = :status",
            FacilityReport.class)
            .setParameter("facility", facility)
            .setParameter("status", status)
            .list();
    }
    
    public List<FacilityReport> findByFacilityOrderByCreateDateDesc(Facility facility) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReport fr WHERE fr.facility = :facility ORDER BY fr.createDate DESC",
            FacilityReport.class)
            .setParameter("facility", facility)
            .list();
    }
    
    public Long countByFacility(Facility facility) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "SELECT COUNT(fr) FROM FacilityReport fr WHERE fr.facility = :facility",
            Long.class)
            .setParameter("facility", facility)
            .uniqueResult();
    }
}