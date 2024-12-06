package ru.netology.bank.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bank.data.DataHelper;
import ru.netology.bank.data.SQLHelper;
import ru.netology.bank.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.bank.data.SQLHelper.clearAuthCode;
import static ru.netology.bank.data.SQLHelper.clearDatabase;

public class TestLoginBank {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        clearAuthCode();
    }
    @AfterAll
    static void tearDownAll() {
        clearDatabase();
    }
    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }
    @Test
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verificationPageVisible();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldGetErrorNotificationLoginRandomUserWithoutAddToBase() {
        var authInfo = DataHelper.generateUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    void shouldGetErrorNotificationLoginExistsUserAndRandomCode() {
        var authInfo = DataHelper.getTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verificationPageVisible();
        var verificationCode = DataHelper.generateCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verificationError("Ошибка! Неверно указан код! \nПопробуйте ещё раз.");
    }
}
