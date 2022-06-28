package com.example.user_regist.controller;

import com.example.user_regist.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
        TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})
class UserControllerTest {

    private static final UUID uuid = UUID.fromString("5af48f3b-468b-4ae0-a065-7d7ac70b37a8");

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        MockedStatic<UUID> mock = Mockito.mockStatic(UUID.class);
        mock.when(UUID::randomUUID).thenReturn(uuid);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("表示失敗。keyなし")
    void signUp01() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/signUp")
                        .param("key", "aaa"))
                .andExpect(view().name("/request_expired"))
                .andReturn();
    }

    @Test
    @DisplayName("表示失敗。keyはあるが24時間over。")
    void signUp02() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/signUp")
                        .param("key", "aaa"))
                .andExpect(view().name("/request_expired"))
                .andReturn();
    }

    @Test
    void regist() {
    }

    @Test
    void toFinish() {
    }
}