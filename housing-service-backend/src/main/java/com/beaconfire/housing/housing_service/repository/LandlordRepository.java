package com.beaconfire.housing.housing_service.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beaconfire.housing.housing_service.entity.Landlord;

@Repository
public class LandlordRepository {
    @Autowired
    private SessionFactory sessionFactory;
    
    public Landlord findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Landlord.class, id);
    }
    
    public Landlord findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM Landlord l WHERE l.email = :email",
            Landlord.class)
            .setParameter("email", email)
            .uniqueResult();
    }
    
    public Landlord findByCellPhone(String cellPhone) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
            "FROM Landlord l WHERE l.cellPhone = :cellPhone",
            Landlord.class)
            .setParameter("cellPhone", cellPhone)
            .uniqueResult();
    }
    
    public Landlord save(Landlord landlord) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(landlord);
        return landlord;
    }
    
    public void delete(Landlord landlord) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(landlord);
    }
    
    public List<Landlord> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Landlord", Landlord.class).list();
    }
}