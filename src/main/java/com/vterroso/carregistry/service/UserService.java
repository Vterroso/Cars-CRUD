package com.vterroso.carregistry.service;

import com.vterroso.carregistry.repository.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

  UserEntity save(UserEntity newUser);
  UserDetailsService userDetailsService();
  void addUserImage(Long id, MultipartFile imageFile) throws IOException;
  byte[] getUserImage(Long id);
}
