package org.minecraftshire.auth.repositories;


import org.springframework.stereotype.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@org.springframework.stereotype.Repository
public class ModificationRepository extends org.minecraftshire.auth.repositories.Repository {


    @Transactional
    public void update(String modelName, String keyName, Object id) {
        jdbc.update("UPDATE " + modelName + " SET last_modified = now() WHERE " + keyName + " = ?", id);
    }


    public void updateUser(String username) {
        this.update("Users", "username", username);
    }

}
