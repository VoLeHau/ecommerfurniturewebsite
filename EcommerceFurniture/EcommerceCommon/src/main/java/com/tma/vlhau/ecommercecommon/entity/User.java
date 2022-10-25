package com.tma.vlhau.ecommercecommon.entity;

import com.tma.vlhau.ecommercecommon.entity.validate.Image.ValidImage;
import com.tma.vlhau.ecommercecommon.entity.validate.Password.ValidPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends IdBasedEntity {

    @Column(length = 128, nullable = false, unique = true)
    @NotBlank(message = "Please enter email")
    @Email(message = "Incorrect email format. Example : johnmet@gmail.com")
    private String email;

    @Column(length = 64, nullable = false)
    @NotBlank(message = "Please enter password")
    @ValidPassword()
    private String password;

    @Column(length = 45, nullable = false, name = "first_name")
    @NotBlank(message = "Please enter first name")
    @Size(min = 2, max = 25 , message = "Size must be between 2 and 25")
    private String firstName;

    @Column(length = 45, nullable = false, name = "last_name")
    @NotBlank(message = "Please enter last name")
    @Size(min = 2, max = 100 , message = "Size must be between 2 and 100")
    private String lastName;

    @Column(length = 128, nullable = false)
    private String photos;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/default-user.png";
        return "/user-photos/" + this.id + "/" + this.photos;
    }

    public String idToString(){
        return id.toString();
    }
    
    @Transient
    public String getFullName() {
    	return firstName + " " + lastName;
    }

    public boolean hasRole(String roleName) {
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getName().equals(roleName)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAdmin(){
        for(Role role : roles){
            if(role.getName().equals("Admin")){
                return true;
            }
        }
        return false;
    }

}
