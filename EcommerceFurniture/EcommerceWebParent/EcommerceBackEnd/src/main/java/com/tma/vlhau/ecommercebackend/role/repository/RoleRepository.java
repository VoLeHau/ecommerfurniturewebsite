package com.tma.vlhau.ecommercebackend.role.repository;

import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Category;
import com.tma.vlhau.ecommercecommon.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

    @Query("SELECT r FROM Role r")
    Page<Role> findAllBy(Pageable pageable);

    @Query("SELECT r FROM Role r WHERE CONCAT(r.id, ' ', r.name) LIKE %?1%")
    Page<Role> findAllBy(String keyword, Pageable pageable);

    Long countById(Integer id);

    Role findByName(String name);


}