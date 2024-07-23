package com.vterroso.carregistry.service;

import com.vterroso.carregistry.repository.entity.UserEntity;

public interface UserService {

  public UserEntity save(UserEntity newUser);
}
