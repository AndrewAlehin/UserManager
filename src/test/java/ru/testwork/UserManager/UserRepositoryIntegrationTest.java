package ru.testwork.UserManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.testwork.UserManager.model.User;
import ru.testwork.UserManager.repository.UserRepository;

@DataJpaTest
@Import(UserRepository.class)
@Sql(value = "/dataTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldFindAllUsers() {
    List<User> users = userRepository.getAll();
    assertThat(users).hasSize(3);
  }

  @Test
  public void shouldFindByLogin() {
    User alex = new User("Alex", "Kotkin", "Pass12", null);
    entityManager.persistAndFlush(alex);

    User found = userRepository.get(alex.getLogin());

    assertThat(found.getLogin()).isEqualTo(alex.getLogin());
  }

  @Test
  void shouldAddUser() {
    User nikolay = new User("Nikolay", "Popov", "Pass2", null);
    userRepository.add(nikolay);

    User found = entityManager.find(User.class, nikolay.getLogin());

    assertThat(found.getLogin()).isEqualTo(nikolay.getLogin());
  }

  @Test
  void shouldUpdateUser() {
    User userUpdate = new User("Nikolay", "Ivanov", "PassUpdate2", null);
    userRepository.update(userUpdate, "Ivanov");

    User found = entityManager.find(User.class, userUpdate.getLogin());

    assertThat(found.getLogin()).isEqualTo(userUpdate.getLogin());
  }

  @Test
  void shouldDeleteUser() {
    User user = entityManager.find(User.class, "Petrov");
    assertThat(user).isNotNull();
    userRepository.delete("Petrov");

    entityManager.clear();

    user = entityManager.find(User.class, "Petrov");
    assertThat(user).isNull();
  }
}
