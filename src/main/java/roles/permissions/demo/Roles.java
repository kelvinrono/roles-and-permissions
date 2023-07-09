package roles.permissions.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.service.annotation.GetExchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    private String name;

    @ManyToMany(
                fetch = FetchType.EAGER,
                cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "role_permission_map",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
        private Set<Permissions> permissions = new HashSet<>();

    public Roles(String name, Set<Permissions> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
    public Roles(String name) {
        this.name = name;
    }
}



