package ru.testwork.UserManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.testwork.UserManager.model.User;

@Component
@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {

  @Transactional
  @Modifying
  @Query("DELETE FROM User u WHERE u.login=:login")
  int delete(@Param("login") String login);

  User getByLogin(String login);
}
