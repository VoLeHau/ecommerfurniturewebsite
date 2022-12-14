package com.tma.vlhau.ecommercebackend.setting.service;


import com.tma.vlhau.ecommercebackend.setting.GeneralSettingBag;
import com.tma.vlhau.ecommercebackend.setting.repository.SettingRepository;
import com.tma.vlhau.ecommercecommon.entity.setting.Setting;
import com.tma.vlhau.ecommercecommon.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    SettingRepository settingRepository;

    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    public void saveAll(Iterable<Setting> listSettings) {
        settingRepository.saveAll(listSettings);
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> generalSettings =  new ArrayList<>();
        List<Setting> generalSettingCategory = settingRepository.findAllBySettingCategory(SettingCategory.GENERAL);
        List<Setting> currencySettingCategory = settingRepository.findAllBySettingCategory(SettingCategory.CURRENCY);

        generalSettings.addAll(generalSettingCategory);
        generalSettings.addAll(currencySettingCategory);

        return new GeneralSettingBag(generalSettings);
    }

    public List<Setting> getMailServerSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.MAIL_TEMPLATE);
    }


	public List<Setting> getCurrencySettings() {
		return settingRepository.findAllBySettingCategory(SettingCategory.CURRENCY);
	}


    public List<Setting> getPaymentSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.PAYMENT);
    }


}
