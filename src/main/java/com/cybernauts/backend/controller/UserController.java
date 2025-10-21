package com.cybernauts.backend.controller;


import com.cybernauts.backend.User.User;
import com.cybernauts.backend.service.RelationService;
import com.cybernauts.backend.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("/users")
public class UserController {

    //1st
    @Autowired
    private UserService service;

    @Autowired
    private RelationService relationService;

    @GetMapping({"","/"})
    public List<User> fetchAllUser() {
        return service.findAlll();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
        User user = service.getUser(id);
        if (user!= null){
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

   //2nd
   @PostMapping("/saveUser")
    public ResponseEntity<User> registerNewUser(@RequestBody User user){
        try{
            user.setDate(LocalDate.now());
            service.saveUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
   }

   //3rd
   @PutMapping("/updated/{id}")
   public ResponseEntity<User> updateUser(@PathVariable ObjectId id, @RequestBody User newEntry){
       User old = service.findByID(id).orElse(null);
       if(old == null || newEntry == null) {  // FIXED LOGIC
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

            if (newEntry.getUsername() != null && !newEntry.getUsername().isEmpty()) {
                old.setUsername(newEntry.getUsername());
                }

            if (newEntry.getAge() != 0) {
                old.setAge(newEntry.getAge());
            }

            if (newEntry.getHobbies() != null) {
                old.setHobbies(newEntry.getHobbies());
                }

            if (newEntry.getFriends() != null) {
                old.setFriends(newEntry.getFriends());
                }
            old.setPopularityScore(service.calculatePopularity(id));

            service.saveUser(old);

            return new ResponseEntity<>(old,HttpStatus.OK);

   }

   //4th
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteUser(@PathVariable ObjectId id){
       User user = service.getUser(id);
       if (user == null) {
           return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
       }

       if (!user.getFriends().isEmpty()) {
           return new ResponseEntity<>("Cannot delete user with friends. Unlink first.", HttpStatus.BAD_REQUEST);
       }
       service.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.ACCEPTED);
   }

   //5th
    @PostMapping("/relation/{id}")
    public ResponseEntity<String>  createRelation(@PathVariable ObjectId id,@RequestBody ObjectId friendId){
        String result= relationService.linkUser(id,friendId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //6th
    @DeleteMapping("/relation/{id}")
    public ResponseEntity<String> deleteRelation(@PathVariable ObjectId id,@RequestBody ObjectId friendId){
        String result= relationService.unlinking(id,friendId);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }
}
