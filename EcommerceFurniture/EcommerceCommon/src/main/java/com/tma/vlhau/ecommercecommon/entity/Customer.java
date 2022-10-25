package com.tma.vlhau.ecommercecommon.entity;

import com.tma.vlhau.ecommercecommon.entity.validate.Password.ValidPassword;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "customers")
public class Customer extends IdBasedEntity {

    @Column(length = 45, nullable = false, unique = true)
    @NotBlank(message = "Please enter email")
    @Email(message = "Incorrect email format. Example : johnmet@gmail.com")
    private String email;

    @Column(length = 64, nullable = false)
    @NotBlank(message = "Please enter password")
    @ValidPassword
    private String password;

    @Column(length = 45, nullable = false, name = "first_name")
    @NotBlank(message = "Please enter first name")
    @Size(min = 2, max = 25 , message = "Size must be between 2 and 25")
    private String firstName;

    @Column(length = 45, nullable = false, name = "last_name")
    @NotBlank(message = "Please enter last name")
    @Size(min = 2, max = 100 , message = "Size must be between 2 and 100")
    private String lastName;

    @Column(length = 15, nullable = true, name = "phone_number")
    private String phoneNumber;

    @Column(length = 128, nullable = true,name = "photo")
    private String photos;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @Column(name = "created_time")
    private Date createTime;

    @Enumerated(EnumType.STRING)
    @Column(name= "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    public String getFullName() {
        return firstName + " " + lastName;
    }

	public Customer(Integer id) {
		this.id = id;
	}

    @OneToMany(mappedBy = "customer")
    Set<Address> address = new HashSet<>();

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", enabled=" + enabled +
                ", createTime=" + createTime +
                ", authenticationType=" + authenticationType +
                ", resetPasswordToken='" + resetPasswordToken + '\'' +
                '}';
    }

    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/default-customer.png";
        return "/customer-photos/" + this.id + "/" + this.photos;
    }

    

}
