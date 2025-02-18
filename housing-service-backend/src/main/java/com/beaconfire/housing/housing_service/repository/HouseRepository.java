package com.beaconfire.housing.housing_service.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beaconfire.housing.housing_service.entity.House;
import com.beaconfire.housing.housing_service.entity.Landlord;

@Repository
public class HouseRepository {
    
    private final SessionFactory sessionFactory;

    @Autowired
    public HouseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public House findById(Integer id) {
        return getCurrentSession().get(House.class, id);
    }

    public House save(House house) {
        getCurrentSession().saveOrUpdate(house);
        return house;
    }

    public void delete(House house) {
        getCurrentSession().delete(house);
    }

    public List<House> findAll() {
        return getCurrentSession()
            .createQuery("FROM House", House.class)
            .list();
    }

    public List<House> findByLandlord(Landlord landlord) {
        return getCurrentSession()
            .createQuery("FROM House h WHERE h.landlord = :landlord", House.class)
            .setParameter("landlord", landlord)
            .list();
    }

    public List<House> findByMaxOccupantGreaterThan(Integer currentOccupants) {
        return getCurrentSession()
            .createQuery("FROM House h WHERE h.maxOccupant > :currentOccupants", House.class)
            .setParameter("currentOccupants", currentOccupants)
            .list();
    }

    public House findByAddress(String address) {
        return getCurrentSession()
            .createQuery("FROM House h WHERE h.address = :address", House.class)
            .setParameter("address", address)
            .uniqueResult();
    }
}