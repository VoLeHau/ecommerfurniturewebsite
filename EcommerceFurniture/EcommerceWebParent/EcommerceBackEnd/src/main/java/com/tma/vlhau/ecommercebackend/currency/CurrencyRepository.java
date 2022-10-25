package com.tma.vlhau.ecommercebackend.currency;

import com.tma.vlhau.ecommercecommon.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();

}
