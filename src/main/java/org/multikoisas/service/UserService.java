package org.multikoisas.service;

import org.multikoisas.dto.UpdateUserRequest;
import org.multikoisas.dto.UserRequest;
import org.multikoisas.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getUsers(Integer id, String username);
    List<UserResponse> findAll();
    UserResponse findById(Integer id);
    //UserResponse findByUsername(String username);
    UserResponse save(UserRequest request);
    UserResponse delete(String username);
    UserResponse update(String username, UpdateUserRequest request);

}
