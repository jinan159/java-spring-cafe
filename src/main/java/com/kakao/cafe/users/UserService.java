package com.kakao.cafe.users;

import com.kakao.cafe.users.domain.User;
import com.kakao.cafe.users.domain.UserRepository;
import com.kakao.cafe.users.exception.UserDuplicatedException;
import com.kakao.cafe.users.exception.UserNotFountException;
import com.kakao.cafe.users.exception.UserUnsavedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public Long join(User user) {
        validateDuplicateUser(user);
        return userRepository.save(user)
                .orElseThrow(UserUnsavedException::new);
    }

    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFountException::new);
    }

    public List<User> findUsers() {
        return userRepository.findAll()
                .orElse(Collections.emptyList());
    }

    private void validateDuplicateUser(User user) throws UserDuplicatedException {
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            throw new UserDuplicatedException("이미 존재하는 회원입니다.");
        }
    }
}
