package com.tma.vlhau.ecommercebackend.setting.city;

import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.City;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

    @Autowired
    private CityRepository stateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateState() {
        Integer countryId = 2;
        Country VNCountry = entityManager.find(Country.class, countryId);
        City state1 = stateRepository.save(new City("Nha Trang", VNCountry));
        City state2 = stateRepository.save(new City("Can Tho", VNCountry));
        City state3 = stateRepository.save(new City("An Giang", VNCountry));
        City state4 = stateRepository.save(new City("Kien Giang", VNCountry));

        Assertions.assertThat(state1).isNotNull();
        Assertions.assertThat(state1.getId()).isGreaterThan(0);

    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 2;
        Country VNCountry = entityManager.find(Country.class, countryId);

        List<City> listStates = stateRepository.findByCountryOrderByNameAsc(VNCountry);

        listStates.forEach(System.out::println);

        Assertions.assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState() {
        Integer stateId = 3;
        String stateName = "Ha Giang";

        City state = stateRepository.findById(stateId).get();
        state.setName(stateName);

        City updatedState = stateRepository.save(state);

        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);

    }

    @Test
    public void testGetState() {
        Integer stateId = 3;
        City state = stateRepository.findById(stateId).get();

        Assertions.assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testDeleteState() {
        Integer stateId = 4;
        stateRepository.deleteById(4);

        Optional<City> deletedState = stateRepository.findById(stateId);

        Assertions.assertThat(deletedState).isNotPresent();
    }

}
