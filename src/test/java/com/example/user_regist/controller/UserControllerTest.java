package com.example.user_regist.controller;

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
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringReplaceUtil.replace("###現在日時###", dtf.format(now));
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
    @DisplayName("表示失敗。keyはあるが24時間over")
    void signUp02() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/signUp")
                        .param("key", "aaa"))
                .andExpect(view().name("/request_expired"))
                .andReturn();
    }

    @Test
    @DisplayName("成功。ユーザー登録画面表示。※登録しておくデータのCSVは都度書き換える必要あり（24時間以内の日付に）")
    @DatabaseSetup("/User/signUp03")
    void signUp03() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/signUp")
                        .param("key", "5af48f3b-468b-4ae0-a065-7d7ac70b37a8"))
                .andExpect(view().name("/regist_user"))
                .andReturn();

        // TODO:セッションにメールアドレスが登録されていることを確認
    }

    @Test
    @DisplayName("ユーザー登録失敗。確認パスワード不一致")
    void regist01() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/user/regist")
                        .param("name", "テスト太郎")
                        .param("nameKana", "テストタロウ")
                        .param("zipCode", "111-1111")
                        .param("address", "新宿区新宿")
                        .param("telephone", "03-3333-3333")
                        .param("password", "abc")
                        .param("confirmPassword", "def")
                ).andExpect(view().name("/regist_user"))
                .andReturn();

        // TODO: エラーメッセージの確認
    }

    @Test
    @DisplayName("ユーザー登録成功")
    @DatabaseSetup("/User/regist02")
    @ExpectedDatabase(value = "/User/regist02/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void regist02() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/user/regist")
                        .param("name", "テスト太郎")
                        .param("nameKana", "テストタロウ")
                        .param("zipCode", "111-1111")
                        .param("address", "新宿区新宿")
                        .param("telephone", "03-3333-3333")
                        .param("password", "abc")
                        .param("confirmPassword", "abc")
                        .sessionAttr("email", "regist-test2@example.com")
                ).andExpect(view().name("redirect:/user/toFinish"))
                .andReturn();

    }

    @Test
    @DisplayName("ユーザー登録成功後、完了画面にリダイレクト")
    void toFinish() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/toFinish")
                ).andExpect(view().name("/regist_user_finish"))
                .andReturn();

    }
}