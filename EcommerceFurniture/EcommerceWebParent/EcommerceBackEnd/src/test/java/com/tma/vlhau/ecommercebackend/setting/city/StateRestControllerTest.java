package com.tma.vlhau.ecommercebackend.setting.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.vlhau.ecommercebackend.setting.country.CountryRepository;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.City;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository stateRepository;

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testListCountry() throws Exception {
        Integer countryId = 242;
        String url = "/states/list_by_country/" + countryId;
        MvcResult mvcResult = mockMvc.perform(get(url))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        City[] states = objectMapper.readValue(jsonResponse, City[].class);

        Assertions.assertThat(states).hasSizeGreaterThan(1);
    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testSaveState() throws Exception {
        String url = "/states/save";
        Integer countryId = 2;

        Country country = countryRepository.findById(countryId).get();
        City state = new City("Madison", country);

        MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(jsonResponse);
        Optional<City> savedStated = stateRepository.findById(stateId);

        Assertions.assertThat(savedStated).isPresent();
    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testUpdateState() throws Exception {
        String url = "/states/save";
        String stateName = "Gonzalo";
        Integer stateId = 307;

        City state = stateRepository.findById(stateId).get();
        state.setName(stateName);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().string(String.valueOf(stateId)));

        Optional<City> savedState = stateRepository.findById(stateId);

        Assertions.assertThat(savedState).isPresent();
    }

    @Test
    @WithMockUser(username = "hau@gmail.com", password = "123456hau", roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId = 307;
        String url = "/states/delete/" + stateId;

        mockMvc.perform(get(url).with(csrf())).andExpect(status().isOk());

        Optional<City> savedState = stateRepository.findById(stateId);

        Assertions.assertThat(savedState).isNotPresent();
    }

}
