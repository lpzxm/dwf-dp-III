package sv.edu.udb.dto;

import lombok.Data;
import sv.edu.udb.model.User;

@Data
public class UserDto {
    private Integer idUser;
    private String username;
    private String firstname;
    private String lastname;
    private Integer age;

    // Sin campo password!
    // Constructor que toma un User entity
    public UserDto(User user) {
        this.idUser = user.getIdUser();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.age = user.getAge();
    }
    // Getters y setters
}
