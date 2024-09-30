package com.PCA.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean save(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return false;
        } else {
            userRepository.save(user);
            return true;
        }
    }

    public List<User> findAllList() {
        return userRepository.findAll();
    }


    public User findById(Long userId) {
        for (User user: userRepository.findAll()){
            if (user.getId().equals(userId)){
                return user;
            }
        }
        return null;
    }
}
