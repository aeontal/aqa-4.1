package ru.netology.web.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverallRegressCardDeliveryTest {

    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.DELETE);
    }

    // ---------- Тесты на проверку поле Город

    // незаполнение всех полей
    @Test
    void shouldGetErrorIfEmptyForm() {
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // заполнение только поля Город
    @Test
    void shouldGetErrorIfOnlyCity() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $(withText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Неверно введена дата"));
    }

    // незаполнение поля Город
    @Test
    void shouldGetErrorWithEmptyCity() {
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

// --------------- Тесты на проверку поле Дата

    // использование неподдерживаемого значения в поле Город
    @Test
    void shouldGetErrorIfIncorrectCity() {
        $("[data-test-id=city] input").setValue("EKB");
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    // заполнение только поля Дата
    @Test
    void shouldGetErrorIfOnlyDate() {
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // незаполнение поля Дата
    void shouldGetErrorWithoutDate() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Неверно введена дата"));
    }

    // --------------- Тесты на проверку поле Имя

    // заполнение только поля Имя

    // использование недопустимого значения поля Дата
    @Test
    void shouldGetErrorIfInvalidDate() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(1));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetErrorIfOnlyName() {
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // незаполнение поля Имя
    @Test
    void shouldGetErrorWithoutName() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // использование недопустимого значения поля Имя
    @Test
    void shouldGetErrorIfInvalidName() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue("Inna");
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

// --------------- Тесты на проверку поле Телефон

    // заполнение только поля Телефон

    // использование недопустимого значения поля Имя 'Ё'
    void shouldGetErrorIfInvalidYeName() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue("Алёна");
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldGetErrorIfOnlyPhone() {
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // использование недопустимого значения поля Телефон

    // незаполнение поля Телефон
    @Test
    void shouldGetErrorWithoutPhone() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

// --------------- Тесты на проверку чекбокса Согласие на обработку персональных данных


// выставление только чекбокса Согласие на обработку персональных данных

    void shouldGetErrorIfInvalidPhone() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue("89530000000");
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldGetErrorIfOnlyAgreement() {
        $("[data-test-id=city] input").click();
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    // невыставление чекбокса Согласие на обработку персональных данных
    @Test
    void shouldGetErrorWithoutPDAgreement() {
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(DataGenerator.setDate(5));
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $(withText("Запланировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text")
                .shouldBe(visible)
                .shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    // Корректное заполнение
    // @Test
    void shouldSuccessSendForm() {
        String date = DataGenerator.setDate(7);
        $("[data-test-id=city] input").setValue(DataGenerator.setCity());
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(DataGenerator.setName());
        $("[data-test-id=phone] input").setValue(DataGenerator.setPhone());
        $("[data-test-id=agreement]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=success-notification]")
                .shouldBe((visible), Duration.ofSeconds(15))
                .shouldHave(text("Успешно!"), Duration.ofSeconds(5));
        String text = $("[data-test-id='success-notification'] .notification__content").text();
        assertEquals("Встреча успешно запланирована на " + date, text);
    }

}