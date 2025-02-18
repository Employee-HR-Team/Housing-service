package com.beaconfire.housing.housing_service.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beaconfire.housing.housing_service.entity.FacilityReport;
import com.beaconfire.housing.housing_service.entity.FacilityReportDetail;

@Repository
public class FacilityReportDetailRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public FacilityReportDetail findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(FacilityReportDetail.class, id);
    }
    
    public FacilityReportDetail save(FacilityReportDetail detail) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(detail);
        return detail;
    }
    
    public void delete(FacilityReportDetail detail) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(detail);
    }
    
    public List<FacilityReportDetail> findByFacilityReport(FacilityReport facilityReport) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReportDetail frd WHERE frd.facilityReport = :report", 
            FacilityReportDetail.class)
            .setParameter("report", facilityReport)
            .list();
    }
    
    public List<FacilityReportDetail> findByFacilityReportOrderByCreateDateDesc(FacilityReport facilityReport) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReportDetail frd WHERE frd.facilityReport = :report ORDER BY frd.createDate DESC", 
            FacilityReportDetail.class)
            .setParameter("report", facilityReport)
            .list();
    }
    
    public List<FacilityReportDetail> findByEmployeeId(Integer employeeId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReportDetail frd WHERE frd.employeeId = :employeeId", 
            FacilityReportDetail.class)
            .setParameter("employeeId", employeeId)
            .list();
    }
    
    public List<FacilityReportDetail> findByFacilityReportAndEmployeeId(FacilityReport facilityReport, Integer employeeId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM FacilityReportDetail frd WHERE frd.facilityReport = :report AND frd.employeeId = :employeeId", 
            FacilityReportDetail.class)
            .setParameter("report", facilityReport)
            .setParameter("employeeId", employeeId)
            .list();
    }
}