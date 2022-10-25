package com.tma.vlhau.ecommercefrontend.setting;

import com.tma.vlhau.ecommercecommon.entity.setting.Setting;
import com.tma.vlhau.ecommercecommon.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findAllBySettingCategory(SettingCategory settingCategory);

    @Query("SELECT s FROM Setting s WHERE s.settingCategory=?1 OR s.settingCategory=?2")
    List<Setting> findByTwoCategories(SettingCategory category1, SettingCategory category2);

    Setting findByKey(String key);

}
