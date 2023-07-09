package roles.permissions.demo;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Builder
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private PermissionsRepo permissionsRepo;

    @Autowired
    private RolesAndPermissionsService rolesAndPermissionsService;

    @GetMapping("/all-users")
    public List<Users> findAllUsers(){
        List<Users> users = userRepo.findAll();
        return users;
    }

    @PostMapping("/save-role")
    public ResponseEntity<String> saveRoleWithPermissions(@RequestBody RoleDTO roleWithPermissionsDTO) {
        try {
            Roles role = new Roles(roleWithPermissionsDTO.getName());
            rolesAndPermissionsService.saveRole(role, roleWithPermissionsDTO.getPermissions());
            return ResponseEntity.status(HttpStatus.CREATED).body("Role saved successfully with permissions.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save role with permissions. " +e.getMessage());
        }
    }

    @GetMapping("/get-roles")
    public List<Roles> getRoles(){
        return rolesRepo.findAll();
    }

    @PostMapping("/update-permissions/{id}")
    public ResponseEntity<MessageResponse> addPermissonsToRole(@RequestBody Set<Permissions> permissions, @PathVariable("id") int id){
        return ResponseEntity.ok(rolesAndPermissionsService.addPermissonsToRole(id, permissions));
    }
    @PostMapping("/add-permissions/{id}")
    public MessageResponse addPermissionsToRole(@PathVariable("id") int id, @RequestBody List<Permissions> permissions){

        Optional<Roles> role = rolesRepo.findById(id);

        if (role.isEmpty()){
            return new MessageResponse("Roles with the given id does not exist", false);
        }
        else {
            role.get().getPermissions().addAll(permissions);
            rolesRepo.save(role.get());
            return new MessageResponse("Permission(s) added successfully", true);
        }


    }

    @GetMapping("/get-permissions")
    public List<Permissions> getAllPermissions(){
        List<Permissions> permissions = permissionsRepo.findAll();
        return permissions;
    }

    @PostMapping("/create-user")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UsersDTO usersDTO){

        return ResponseEntity.ok(rolesAndPermissionsService.createUser(usersDTO));
    }

//    @PutMapping("/update-user/{id}")
//    public ResponseEntity<MessageResponse> updateUser(@RequestBody UsersDTO usersDTO, @PathVariable int id){
//        return ResponseEntity.status(200).body(rolesAndPermissionsService.updateUser(usersDTO, id));
//    }
}
