package roles.permissions.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private String name;
    private Set<Permissions> permissions;

    public RoleDTO(String name) {
        this.name = name;
    }
}
