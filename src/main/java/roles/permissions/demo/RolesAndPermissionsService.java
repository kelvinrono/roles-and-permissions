package roles.permissions.demo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.LabelUI;
import java.util.*;

@AllArgsConstructor
@Service
public class RolesAndPermissionsService {
    private final RolesRepo rolesRepo;
    private final PermissionsRepo permissionsRepo;
    private final UserRepository userRepository;
    public MessageResponse saveRole(Roles roles, Set<Permissions> permissions) {

        // Save the role first to generate its ID
        rolesRepo.save(roles);

        // Fetch existing permission objects based on IDs
        Set<Permissions> existingPermissions = new HashSet<>();
        for (Permissions permission : permissions) {
            Permissions existingPermission = permissionsRepo.findById(permission.getPermissionsId())
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permission.getPermissionsId()));
            existingPermissions.add(existingPermission);
        }
        // Update the role's permissions
        roles.setPermissions(existingPermissions);
        rolesRepo.save(roles);
        return new MessageResponse("Role saved successfully", true);
    }
    public MessageResponse addPermissonsToRole(int id, Set<Permissions> permissions){
        Optional<Roles> existingRole = rolesRepo.findById(id);

        if(existingRole.isEmpty()){
            return new MessageResponse("Role with the given id does not exist", false);
        }
        else {
            Set<Permissions> _permissions = new HashSet<>();
            for(Permissions permission: permissions){
                Permissions existingPermission = permissionsRepo.findById(permission.getPermissionsId())
                        .orElseThrow(() -> new RuntimeException("Permission not found: " + permission.getPermissionsId()));
                _permissions.add(existingPermission);
            }
            existingRole.get().getPermissions().addAll(_permissions);
            rolesRepo.save(existingRole.get());
        }
        return new MessageResponse("Roles Updated successfully", true);
    }
    public MessageResponse createUser(UsersDTO usersDTO){
       Users existingUser = userRepository.findUsersByName(usersDTO.getName());
       Set<Roles> roles = usersDTO.getRoles();
       if(existingUser!=null){
           return new MessageResponse("User Already exist", false);
       } else {
           //Roles role  = rolesRepo.findById(usersDTO.getRole()).orElseThrow(()-> new IllegalArgumentException("Role with the id does not exist"));
           Set<Roles> _roles = new HashSet<>();
           for (Roles roles1: roles){
               Optional<Roles> existingRole = rolesRepo.findById(roles1.getRoleId());
               if(existingRole.isEmpty()){
                   return  new MessageResponse("Role with the ID "+ roles1.getRoleId()+" does not exist", false);
               }
               else{
                   Roles roleWithId = rolesRepo.findById(existingRole.get().getRoleId()).get();
                   _roles.add(roleWithId);
               }
           }
           Users user = Users.builder()
                   .userId(usersDTO.getUserId())
                   .name(usersDTO.getName())
                   .role(_roles)
                   .build();
           userRepository.save(user);
           return new MessageResponse("User registered Successfully", true);
       }
    }

}
