package com.cybernauts.backend.service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;


import com.cybernauts.backend.User.User;
import com.cybernauts.backend.repository.Repository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  @Autowired
  private Repository repository;
  
  public User saveUser(User user)
  {
    repository.save(user);
    return user;
  }

  public User getUser(@NotNull ObjectId id){
    return repository.findById(id).orElse(null);
  }

  public List<User> findAlll() {

    return repository.findAll();
  }

  public void deleteById(ObjectId id) {
    User user = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));;
    if(!user.getFriends().isEmpty()){
      System.out.println("First need to unlink it .....unlinking");
      for(ObjectId friendId: user.getFriends()){
        User friend = repository.findById(friendId).orElse(null);//storing the value after looping through for each loop
        if(friend != null){
          friend.getFriends().remove(id);
          repository.save(friend);
          }
        }
      user.getFriends().clear();
      repository.save(user);
    }
    else {
      repository.deleteById(id);//currently empty
    }
  }

  public void createRelation(ObjectId userId, ObjectId friendId) {
    User user = repository.findById(userId).orElseThrow();
    User friend = repository.findById(friendId).orElseThrow();

    // Prevent duplicate friendship
    if (!user.getFriends().contains(friendId)) {
      user.getFriends().add(friendId);

    }
    if (!friend.getFriends().contains(userId)) {
      friend.getFriends().add(userId);
    }

    repository.save(user);
    repository.save(friend);

    calculatePopularity(userId);
    calculatePopularity(friendId);
  }

  public void deleteRelation(ObjectId id,ObjectId friendId) {
    User user = repository.findById(id).orElseThrow();
    User friend=repository.findById(friendId).orElseThrow();
    user.getFriends().remove(friend.getId());
    friend.getFriends().remove(id);
    repository.save(user);
    repository.save(friend);
  }


  public int countSharedHobbies(User user){
    int sharedHobbies=0;
    for (ObjectId friendId : user.getFriends()) {
      User friend = repository.findById(friendId).orElse(null);
      if (friend != null) {
        Set<String> userHobbies = new HashSet<>(user.getHobbies());
        HashSet<String> friendHobbies = new HashSet<>(friend.getHobbies());
        userHobbies.retainAll(friendHobbies);
        sharedHobbies+= userHobbies.size();
      }
    }

    return sharedHobbies;


  }

  public double calculatePopularity(ObjectId id) {

    User user=repository.findById(id).orElseThrow();
    int numOfFriends= user.getFriends().size();
    int sharedHobbies = countSharedHobbies(user);
    double popScore= numOfFriends +(sharedHobbies*0.5);

    user.setPopularityScore(popScore);
    repository.save(user);
    return popScore;
  }

  public Optional<User> findByID(ObjectId id) {
      return repository.findById(id);
  }
}
