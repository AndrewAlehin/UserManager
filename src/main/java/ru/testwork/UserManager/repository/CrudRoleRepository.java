package ru.testwork.UserManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.testwork.UserManager.model.Role;

@Component
@Transactional(readOnly = true)
public interface CrudRoleRepository extends JpaRepository<Role, Integer> {

  Role getByName(String name);
}
