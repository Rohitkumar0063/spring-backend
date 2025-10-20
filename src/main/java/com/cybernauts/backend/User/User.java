package com.cybernauts.backend.User;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")

public class User {

  private ObjectId id;
  private String username;
  private int age;
  private List<String> hobbies = new ArrayList<>();
  private List<ObjectId> friends = new ArrayList<>();
  private LocalDate date;
  private double popularityScore;

  // Getters and setters
  public ObjectId getId() { return id; }
  public void setId(ObjectId id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }

  public List<String> getHobbies() { return hobbies; }
  public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }

  public List<ObjectId> getFriends() { return friends; }
  public void setFriends(List<ObjectId> friends) { this.friends = friends; }

  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }

  public double getPopularityScore() { return popularityScore; }
  public void setPopularityScore(double popularityScore) { this.popularityScore = popularityScore; }

  public String getStringId() {
    return id != null ? id.toHexString() : null;
  }

}
