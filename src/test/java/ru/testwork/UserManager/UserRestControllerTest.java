package ru.testwork.UserManager;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.testwork.UserManager.model.Role;
import ru.testwork.UserManager.model.User;
import ru.testwork.UserManager.web.UserRestController;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "/dataTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRestControllerTest {

  public static final User USER = new User("Name", "Login", "Pass123",
      null);
  public static final User USER_DOUBLE_LOGIN = new User("Name", "Petrov",
      "Pass123", null);
  public static final User USER_EXIST = new User("Name", "Kolosov", "Pass123",
      null);
  public static final String LOGIN_NO_EXIST = "Adam";
  public static final String LOGIN_KOLOSOV = "Kolosov";

  private static final String REST_URL = UserRestController.REST_URL + '/';

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getAllUser() throws Exception {
    this.mockMvc.perform(get(REST_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getUser() throws Exception {
    this.mockMvc.perform(get(REST_URL + "Petrov"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getUserNoFound() throws Exception {
    this.mockMvc.perform(get(REST_URL + LOGIN_NO_EXIST))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void addUser() throws Exception {
    Set<Role> role = new HashSet<>();
    role.add(new Role("Сантехник"));
    User user = new User("Name", "Login", "Pass123", role);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("{\"success\":\"true\"}")));
  }

  @Test
  public void addUser_NoName() throws Exception {
    User user = new User(null, "Login", "Pass123", null);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(
            "{\"success\":\"false\",\"errors\":{\"name\":\"Name may not be null\"}}")));
  }

  @Test
  public void addUser_NoLogin() throws Exception {
    User user = new User("Name", null, "Pass123", null);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("{\"success\":\"false\",\"errors\":"
            + "{\"login\":\"Login may not be null\"}}")));
  }

  @Test
  public void addUser_NoPassword() throws Exception {
    User user = new User("Name", "Login1", "", null);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void addUser_NoValidPassword_NoUppercase() throws Exception {
    User user = new User("Name", "Login1", "ass1", null);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("{\"success\":\"false\",\"errors\":"
            + "{\"password\":\"Password must contain at least 1 uppercase characters.\"}}")));
  }

  @Test
  public void addUser_NoValidPassword_NoDigit() throws Exception {
    User user = new User("Name", "Login1", "Pass", null);
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("{\"success\":\"false\",\"errors\":"
            + "{\"password\":\"Password must contain at least 1 digit characters.\"}}")));
  }

  @Test
  public void addExistUser() throws Exception {
    this.mockMvc.perform(post(REST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(USER_EXIST)))
        .andExpect(status().isConflict());
  }

  @Test
  public void updateUser() throws Exception {
    User user = new User("Name", LOGIN_KOLOSOV, "Password123", null);
    this.mockMvc.perform(put(REST_URL + LOGIN_KOLOSOV)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("{\"success\":\"true\"}")));
  }

  @Test
  public void updateNoExistUser() throws Exception {
    this.mockMvc.perform(put(REST_URL + LOGIN_NO_EXIST)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(USER)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateUserDoubleLogin() throws Exception {
    this.mockMvc.perform(put(REST_URL + LOGIN_KOLOSOV)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(USER_DOUBLE_LOGIN)))
        .andExpect(status().isConflict());
  }

  @Test
  public void deleteUser() throws Exception {
    this.mockMvc.perform(delete(REST_URL + "Ivanov"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  public void deleteNoExistUser() throws Exception {
    this.mockMvc.perform(delete(REST_URL + LOGIN_NO_EXIST))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
