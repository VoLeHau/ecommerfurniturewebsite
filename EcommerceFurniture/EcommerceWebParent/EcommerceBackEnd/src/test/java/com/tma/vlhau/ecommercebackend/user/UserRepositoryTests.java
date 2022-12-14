package com.tma.vlhau.ecommercebackend.user;


import com.tma.vlhau.ecommercebackend.user.repository.UserRepository;
import com.tma.vlhau.ecommercecommon.entity.Role;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static void accept(User user) {
        System.out.println();
    }

    @Test
    public void testCreateNewUserWithOneRole(){
        Role adminRole = entityManager.find(Role.class, 1);
        User userNMinhThang = new User("nminhthang@tma.com.vn", "123456", "Thang", "Nguyen Minh");
        userNMinhThang.addRole(adminRole);

        User savedUser = userRepository.save(userNMinhThang);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles(){
        User userDTrungKien = new User("dtkien@gmail.com", "123456", "Kien", "Dang Trung");
        Role editorRole = new Role(3);
        Role assistantRole = new Role(5);

        userDTrungKien.addRole(editorRole);
        userDTrungKien.addRole(assistantRole);

        User savedUser = userRepository.save(userDTrungKien);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> userList = userRepository.findAll();
        userList.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User userNMinhThang = userRepository.findById(1).get();

        assertThat(userNMinhThang).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userNMinhThang = userRepository.findById(1).get();

        userNMinhThang.setEnabled(true);
        userNMinhThang.setEmail("nminhthang1705@tma.com.vn");

        userRepository.save(userNMinhThang);
    }

    @Test
    public void testUpdateUserRoles(){
        User userDTKien = userRepository.findById(2).get();

        Role editorRole = new Role(3);
        Role salesRole = new Role(2);

        userDTKien.getRoles().remove(editorRole);
        userDTKien.addRole(salesRole);

        userRepository.save(userDTKien);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "hau@gmail.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountUserById(){
        Integer id = 10;
        Long userId = userRepository.countById(id);

        assertThat(userId).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 4;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 1;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;//first-page = 0
        int pageSize = 2;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> usersFirstPage = userRepository.findAll(pageable);
        List<User> listUsers = usersFirstPage.getContent();

        listUsers.forEach(System.out::println);

        assertThat(usersFirstPage).size().isGreaterThan(0);
    }

    @Test
    public void testSearchUsers(){
        String keyword = "gmail";
        int pageNumber = 0;//first-page = 0
        int pageSize = 2;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> usersFirstPage = userRepository.findAll(keyword, pageable);
        List<User> listUsers = usersFirstPage.getContent();

        listUsers.forEach(System.out::println);

        assertThat(usersFirstPage).size().isGreaterThan(0);
    }

}
