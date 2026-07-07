package vn.edu.nlu.fit.musicweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.nlu.fit.musicweb.model.User;
import vn.edu.nlu.fit.musicweb.repository.UserRepository;

@Service
public class UsersService {
    
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
