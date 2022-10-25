package com.tma.vlhau.ecommercebackend.setting.repository;

import com.tma.vlhau.ecommercecommon.entity.setting.Setting;
import com.tma.vlhau.ecommercecommon.entity.setting.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findAllBySettingCategory(SettingCategory settingCategory);


}
