package com.example.user_regist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.user_regist.util.CsvDataSetLoader;
import com.example.user_regist.util.StringReplaceUtil;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
        TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})
class RegistRequestControllerTest {

    private static final UUID uuid = UUID.fromString("5af48f3b-468b-4ae0-a065-7d7ac70b37a8");


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        MockedStatic<UUID> mock = Mockito.mockStatic(UUID.class);
        mock.when(UUID::randomUUID).thenReturn(uuid);
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringReplaceUtil.replace("###現在日時###", dtf.format(now));
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    @DisplayName("リクエスト画面表示")
    void toRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/request"))
                .andExpect(view().name("/regist_request"))
                .andReturn();
    }

    @Test
    @DisplayName("入力値チェック、メールアドレス空")
    void send01() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                )
                .andExpect(view().name("/regist_request"))
                .andReturn();
    }

    @Test
    @DisplayName("入力値チェック、メールアドレス形式不正")
    void send02() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                        .param("email", "aaa"))
                .andExpect(view().name("/regist_request"))
                .andReturn();
    }

    @Test
    @DisplayName("入力値チェック、既に依頼済み。※CSVの日付書き換える必要あり")
    @DatabaseSetup("/RegistRequest/send03")
    void send03() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                        .param("email", "regist-test@example.com"))
                .andExpect(view().name("/regist_request"))
                .andReturn();
        // TODO: エラーメッセージチェック
    }

    @Test
    @DisplayName("完了画面にリダイレクト")
    void toFinish() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/request/toFinish"))
                .andExpect(view().name("/regist_request_finish"))
                .andReturn();
    }

    @Test
    @DisplayName("リクエスト成功。レコードはあるけど24時間over")
    @DatabaseSetup("/RegistRequest/send04")
    @ExpectedDatabase(value = "/RegistRequest/send04/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void send04() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                        .param("email", "regist-test@example.com"))
                .andExpect(view().name("redirect:/request/toFinish"))
                .andReturn();
    }

    @Test
    @DisplayName("リクエスト成功。レコードなし")
    @DatabaseSetup("/RegistRequest/send05")
    @ExpectedDatabase(value = "/RegistRequest/send05/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void send05() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                        .param("email", "regist-test@example.com"))
                .andExpect(view().name("redirect:/request/toFinish"))
                .andReturn();
    }

    @Test
    @DisplayName("リクエスト失敗。既にユーザー登録済み")
    @DatabaseSetup("/RegistRequest/send06")
    void send06() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/request/send")
                        .param("email", "regist-test@example.com"))
                .andExpect(view().name("redirect:/request/toFinish"))
                .andReturn();
    }



}