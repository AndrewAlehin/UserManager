package ru.testwork.UserManager.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import ru.testwork.UserManager.View;
import ru.testwork.UserManager.util.ValidPassword;

@Entity
@Table(name = "users")
public class User {

  @Column(name = "name")
  @NotBlank(message = "Name may not be null")
  @JsonView(View.GetAll.class)
  private String name;

  @Id
  @Basic(optional = false)
  @Column(name = "login", unique = true)
  @NotBlank(message = "Login may not be null")
  @JsonView(View.GetAll.class)
  private String login;

  @Column(name = "password")
  @NotBlank(message = "Password may not be null")
  @ValidPassword
  @JsonView(View.GetAll.class)
  private String password;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "login"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  public User(String name, String login, String password, Set<Role> roles) {
    this.name = name;
    this.login = login;
    this.password = password;
    this.roles = roles;
  }

  public User() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(name, user.name) &&
        Objects.equals(login, user.login) &&
        Objects.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, login, password);
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", login='" + login + '\'' +
        ", password='" + password + '\'' +
        ", roles=" + roles +
        '}';
  }
}
