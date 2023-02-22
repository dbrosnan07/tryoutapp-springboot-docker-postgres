package com.example.tryoutapp.controller;

import com.example.tryoutapp.model.User;
import com.example.tryoutapp.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController 
{

    private UserRespository userRespository;

    @Autowired
    public UserController(UserRespository userRespository) 
    {
        this.userRespository = userRespository;
    }

    @GetMapping("/user/all")
    Iterable<User> all() 
    {
        return userRespository.findAll();
    }

    @GetMapping("/user/{id}")
    User userById(@PathVariable Long id) 
    {
        return userRespository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user/save")
    User save(@RequestBody User user) 
    {
        return userRespository.save(user);
    }

}
