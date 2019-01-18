package com.nibrasco.aghnam.Utils.RoomData.DAO;

import android.arch.persistence.room.*;
import com.nibrasco.aghnam.Model.User;

import static android.arch.persistence.room.OnConflictStrategy.*;

public interface UserDAO {
    @Insert(onConflict = REPLACE)
    void insert(User word);

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * from User " +
            "WHERE User.Phone == :phone")
    User getUser(String phone);
}
