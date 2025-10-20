package com.cybernauts.backend;

import com.cybernauts.backend.User.User;
import com.cybernauts.backend.service.RelationService;
import com.cybernauts.backend.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RelationService relationService;

    // -----------------------------
    // Test 1: Create and fetch user
    // -----------------------------
    @Test
    public void testCreateAndFetchUser() {
        User user = new User();
        user.setUsername("TestUser");
        user.setAge(25);
        user.setHobbies(Arrays.asList("Reading", "Music"));
        user.setDate(LocalDate.now());

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getId(), "User ID should not be null");

        User fetchedUser = userService.getUser(savedUser.getId());
        assertNotNull(fetchedUser, "Fetched user should exist");
        assertEquals("TestUser", fetchedUser.getUsername(), "Username should match");
        assertEquals(25, fetchedUser.getAge(), "Age should match");
    }

    // -----------------------------------
    // Test 2: Update user and popularity
    // -----------------------------------
    @Test
    public void testUpdateUserAndPopularity() {
        // Create user
        User user = new User();
        user.setUsername("Alice");
        user.setAge(30);
        user.setHobbies(Arrays.asList("Music", "Reading"));
        user.setDate(LocalDate.now());
        userService.saveUser(user);

        ObjectId id = user.getId();

        // Update user
        User update = new User();
        update.setUsername("AliceUpdated");
        update.setAge(31);
        update.setHobbies(Arrays.asList("Music", "Sports"));
        userService.findByID(id).ifPresent(u -> {
            u.setUsername(update.getUsername());
            u.setAge(update.getAge());
            u.setHobbies(update.getHobbies());
            u.setPopularityScore(userService.calculatePopularity(id));
            userService.saveUser(u);
        });

        User updatedUser = userService.getUser(id);
        assertEquals("AliceUpdated", updatedUser.getUsername(), "Username should be updated");
        assertEquals(31, updatedUser.getAge(), "Age should be updated");
        assertEquals(2, updatedUser.getHobbies().size(), "Hobbies should be updated");
        assertEquals(userService.calculatePopularity(id), updatedUser.getPopularityScore(), "Popularity score should be updated");
    }

    // -----------------------------------------
    // Test 3: Delete user with friends prevention
    // -----------------------------------------
    @Test
    public void testDeleteUserWithFriendsBlocked() {
        // Create two users
        User user1 = new User();
        user1.setUsername("User1");
        user1.setDate(LocalDate.now());
        userService.saveUser(user1);

        User user2 = new User();
        user2.setUsername("User2");
        user2.setDate(LocalDate.now());
        userService.saveUser(user2);

        // Link them
        relationService.linkUser(user1.getId(), user2.getId());

        // Attempt to delete user1
        userService.deleteById(user1.getId());

        // Fetch user1 again
        User remainingUser = userService.getUser(user1.getId());
        assertNotNull(remainingUser, "User should still exist after auto-unlink");
        assertTrue(remainingUser.getFriends().isEmpty(), "Friends should be cleared automatically");
    }
}
