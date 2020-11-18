package ru.testwork.UserManager.web;

import java.util.List;
import javax.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.testwork.UserManager.model.User;
import ru.testwork.UserManager.repository.UserRepository;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

  static final String REST_URL = "/rest/user";

  private final UserRepository repository;

  public UserRestController(UserRepository repository) {
    this.repository = repository;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String login) {
    checkNotFoundWithId(repository.delete(login), login);
  }

  @GetMapping("/")
  public List<User> getAll() {
    return repository.getAll();
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public JSONObject update(@RequestBody @Valid User user, @PathVariable String login) {
    checkLogin(login);
    repository.update(user, login);
    return null;
  }

  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public JSONObject add(@RequestBody @Valid User user) {
    checkNew(user);
    repository.add(user);
    return null;
  }

  private void checkNew(User user) {
    if (repository.get(user.getLogin()) != null) {
      throw new DoubleException("Already exists with this login");
    }
  }

  private void checkLogin(String login) {
    if (repository.get(login) == null) {
      throw new NotFoundException("login = " + login);
    }
  }

  private void checkNotFoundWithId(boolean found, String login) {
    if (!found) {
      throw new NotFoundException("login = " + login);
    }
  }
}