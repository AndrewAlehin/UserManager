package ru.testwork.UserManager.web;

import com.fasterxml.jackson.annotation.JsonView;
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
import ru.testwork.UserManager.View;
import ru.testwork.UserManager.model.User;
import ru.testwork.UserManager.repository.UserRepository;
import ru.testwork.UserManager.util.DoubleException;
import ru.testwork.UserManager.util.NotFoundException;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

  public static final String REST_URL = "/rest/user";

  private final UserRepository repository;

  public UserRestController(UserRepository repository) {
    this.repository = repository;
  }

  @DeleteMapping("/{login}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String login) {
    if (!repository.delete(login)) {
      throw new NotFoundException("login = " + login);
    }
  }

  @GetMapping("/")
  @JsonView(View.GetAll.class)
  public List<User> getAll() {
    return repository.getAll();
  }

  @GetMapping("/{login}")
  public User get(@PathVariable String login) {
    User user = repository.get(login);
    if (user == null) {
      throw new NotFoundException("login = " + login);
    }
    return user;
  }

  @PutMapping(value = "/{login}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public JSONObject update(@Valid @RequestBody User user, @PathVariable String login) {
    User checkUser = repository.get(login);
    if (checkUser == null) {
      throw new NotFoundException("login = " + login);
    }
    String checkLogin = checkUser.getLogin();
    if (!user.getLogin().equals(checkLogin) && repository.get(checkLogin) != null) {
      throw new DoubleException("Already exists with this login");
    }
    repository.update(user, login);
    return goodRequest();
  }

  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public JSONObject add(@Valid @RequestBody User user) {
    if (repository.get(user.getLogin()) != null) {
      throw new DoubleException("Already exists with this login");
    }
    repository.add(user);
    return goodRequest();
  }

  private JSONObject goodRequest() {
    JSONObject entity = new JSONObject();
    entity.put("success", "true");
    return entity;
  }
}