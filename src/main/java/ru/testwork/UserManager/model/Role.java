package ru.testwork.UserManager.model;


import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
  private Set<User> users;

  @Column(name = "role")
  private String name;

  public Role(String name) {
    this.name = name;
  }

  public Role() {
  }

  public void setUser(Set<User> users) {
    this.users = users;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Role{" +
        "name='" + name + '\'' +
        '}';
  }
}
