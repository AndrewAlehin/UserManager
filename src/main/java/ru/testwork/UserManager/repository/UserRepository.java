package ru.testwork.UserManager.repository;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;
import ru.testwork.UserManager.model.Role;
import ru.testwork.UserManager.model.User;

@Repository
public class UserRepository {

  private final CrudUserRepository userRepository;
  private final CrudRoleRepository roleRepository;

  public UserRepository(CrudUserRepository crudRepository,
      CrudRoleRepository roleRepository) {
    this.userRepository = crudRepository;
    this.roleRepository = roleRepository;
  }

  public User add(User user) {
    Set<Role> roles = user.getRoles();
    if (roles != null) {
      for (Role role : roles) {
        if (roleRepository.getByName(role.getName()) == null) {
          roleRepository.save(role);
        }
      }
    }
    return userRepository.save(user);
  }

  public User update(User user, String login) {
    User userUpdate = userRepository.getByLogin(login);
    userUpdate.setName(user.getName());
    userUpdate.setLogin(user.getLogin());
    userUpdate.setPassword(user.getPassword());
//        userUpdate.setRoles(user.getRoles());
    return userRepository.save(userUpdate);
  }

  public boolean delete(String login) {
    return userRepository.delete(login) != 0;
  }

  public User get(String login) {
    return userRepository.getByLogin(login);
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }
}