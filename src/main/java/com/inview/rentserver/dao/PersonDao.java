package com.inview.rentserver.dao;

import com.inview.rentserver.iface.DBBase;
import org.springframework.stereotype.Component;
import pojo.PersonDetails;

@Component
public class PersonDao extends DBBase<PersonDetails> {

    public PersonDetails findByID(int manID) {
        for (PersonDetails person : getDB()) {
            if (person.getPrimary_id() == manID) {
                return person;
            }
        }
        return null;
    }
}
