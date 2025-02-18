package com.beaconfire.housing.housing_service.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beaconfire.housing.housing_service.entity.Facility;
import com.beaconfire.housing.housing_service.entity.House;

@Repository
public class FacilityRepository {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public Facility findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Facility.class, id);
    }
    
    public Facility save(Facility facility) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(facility);
        return facility;
    }
    
    public void delete(Facility facility) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(facility);
    }
    
    public List<Facility> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Facility", Facility.class).list();
    }
    
    public List<Facility> findByHouse(House house) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM Facility f WHERE f.house = :house",
            Facility.class)
            .setParameter("house", house)
            .list();
    }
    
    public List<Facility> findByTypeAndHouse(String type, House house) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM Facility f WHERE f.type = :type AND f.house = :house",
            Facility.class)
            .setParameter("type", type)
            .setParameter("house", house)
            .list();
    }
    
    public List<Facility> findByHouseOrderByCreateDateDesc(House house) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM Facility f WHERE f.house = :house ORDER BY f.createDate DESC",
            Facility.class)
            .setParameter("house", house)
            .list();
    }
}