package com.cybernauts.backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cybernauts.backend.User.User;

import java.util.List;

public interface Repository extends MongoRepository<User, ObjectId>{


}
