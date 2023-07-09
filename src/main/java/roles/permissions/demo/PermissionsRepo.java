package roles.permissions.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepo extends JpaRepository<Permissions, Integer> {

    Permissions findByName(String name);
}
