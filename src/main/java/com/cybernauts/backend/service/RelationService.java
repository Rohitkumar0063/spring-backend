package com.cybernauts.backend.service;

import com.cybernauts.backend.User.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.stereotype.Service;

@Service
public class RelationService {


    @Autowired
    private UserService userService;


    public String linkUser( ObjectId id1, ObjectId id2){
        if(id1.equals(id2)){
            System.out.println("Linking to themself.....error ");
        }
        User user1=  userService.findByID(id1).orElseThrow();
        User user2 = userService.findByID(id2).orElseThrow();

        if (user1.getFriends().contains(user2.getId())
            && user2.getFriends().contains(user1.getId())) {
            System.out.println("Already in relation");
        }

        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        userService.saveUser(user1);
        userService.saveUser(user2);

        userService.calculatePopularity(id1);
        userService.calculatePopularity(id2);

        System.out.println("created link ");

        return "LinkingDone";
    }

    //Unlinking method
    public String unlinking(ObjectId id1 ,ObjectId id2){
        User user1= (User) userService.findByID(id1).orElseThrow();
        User user2= (User) userService.findByID(id2).orElseThrow();

//        if(id1!=null){
//            if(user1.getFriends().contains(user2.getId()))
//            {
//                user1.getFriends().remove(id2);
//                user2.getFriends().remove(id1);
//            }
//
//        }
//        return  "Unlinked relation successfully";
//    }
        boolean removed = false;

        if(user1.getFriends().contains(user2.getId())){
            user1.getFriends().remove(user2.getId());
            removed = true;
        }
        if(user2.getFriends().contains(user1.getId())){
            user2.getFriends().remove(user1.getId());
            removed = true;
        }

        userService.saveUser(user1);
        userService.saveUser(user2);

        userService.calculatePopularity(id1);
        userService.calculatePopularity(id2);

        if(removed){
            return "Successfully unlinked users";
        } else {
            return "No relationship existed";
        }
    }


}


