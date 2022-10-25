package com.tma.vlhau.ecommercebackend.role.service;

import com.tma.vlhau.ecommercebackend.role.exception.RoleNameAlreadyExistsException;
import com.tma.vlhau.ecommercebackend.role.exception.RoleNotFoundException;
import com.tma.vlhau.ecommercebackend.role.repository.RoleRepository;
import com.tma.vlhau.ecommercecommon.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public static final int ROLES_PER_PAGE = 5;

    public List<Role> listAll() {
        Sort sort = Sort.by("name").ascending();

        return (List<Role>) roleRepository.findAll(sort);
    }

    public Page<Role> listByPage(int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROLES_PER_PAGE, sort);

        if (keyword != null) {
            return roleRepository.findAllBy(keyword, pageable);
        } else {
            return roleRepository.findAllBy(pageable);
        }

    }

    public Role save(Role role){

        return roleRepository.save(role);
    }

    public Role get(Integer id) throws RoleNotFoundException {
        try {
            return roleRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new RoleNotFoundException("Could not find any role with id = " + id);
        }
    }

    public void delete(Integer id) throws RoleNotFoundException {
        Long roleCountById = roleRepository.countById(id);

        if (roleCountById == null || roleCountById == 0)
            throw new RoleNotFoundException("Could not find any role with id = " + id);

        roleRepository.deleteById(id);
    }

    public String checkRoleUnique(Integer id, String name) {
        Role roleByName = roleRepository.findByName(name);

        boolean isCreatingNew = (id == null || id == 0);

        if (isCreatingNew) {
            if (roleByName != null)
                return "Duplicate role name";
        } else {
            if (roleByName != null && roleByName.getId() != id)
                return "Duplicate role name";
        }

        return "OK";
    }



}
