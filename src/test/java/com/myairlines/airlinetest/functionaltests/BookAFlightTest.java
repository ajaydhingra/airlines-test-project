package com.myairlines.airlinetest.functionaltests;

import com.myairlines.airlinetest.extensions.MyReportingExtension;
import com.myairlines.airlinetest.pageobjects.AirlinesHomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({MyReportingExtension.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookAFlightTest {

    public WebDriver driver;

    private String applicationURL;

    @Value("${airlines.url}")
    public void setApplicationURL(String applicationURL) {
        this.applicationURL = applicationURL;
    }

    @BeforeAll
    public void beforeAll() {
        driver = getWebDriver();

    }

    @AfterAll
    public void afterAll() {
        driver.quit();
    }

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {

    }

    private WebDriver getWebDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    //@Disabled
    @Test
    @DisplayName("Test_Departure_Arrival_City_Error_Message")
    //@Order(1)
    void testDepartureArrivalCityErrorMessage() {
        AirlinesHomePage airlinesHomePage = new AirlinesHomePage(driver, applicationURL);
        airlinesHomePage.open();
        airlinesHomePage.clickSearchButton();
        String departureCityErrorMessage = airlinesHomePage.getDepartureCityErrorMessage();
        assertThat(departureCityErrorMessage, equalTo("Select Departure City"));
        String arrivalCityErrorMessage = airlinesHomePage.getArrivalCityErrorMessage();
        assertThat(arrivalCityErrorMessage, equalTo("Select Arrival City"));
    }


    //@Disabled
    @ParameterizedTest(name = "{1} To {2} One Way")
    @MethodSource("getArgumentsForTestingOneWayFlightBooking")
    //@Order(2)
    void testOneWayFlightBooking(String country, String departureCity, String arrivalCity, int adults, int children, int infants, String currency) {
        AirlinesHomePage airlinesHomePage = new AirlinesHomePage(driver, applicationURL);
        airlinesHomePage.open();
        airlinesHomePage.selectCountry(country);
        assertThat(airlinesHomePage.getCountry(), equalTo(country));
        boolean isOneWaySelected = airlinesHomePage.checkIfOneWaySelected();
        assertThat(isOneWaySelected, is(true));
        String departureCityValue = airlinesHomePage.selectDepartureCity(departureCity);
        assertThat(departureCityValue, equalTo(departureCity));
        String arrivalCityValue = airlinesHomePage.selectArrivalCity(arrivalCity);
        assertThat(arrivalCityValue, equalTo(arrivalCity));
        LocalDate departureDate = LocalDate.now().plusDays(70);
        String selectedDepartureDate = airlinesHomePage.selectDepartureDate(departureDate);
        assertThat(selectedDepartureDate, equalTo(DateTimeFormatter.ofPattern("dd/MM").format(departureDate)));
        boolean isReturnDateDisabled = airlinesHomePage.checkIfReturnDateDisabled();
        assertThat(isReturnDateDisabled, is(true));
        String passengerInfo = airlinesHomePage.selectPassengers(adults, children, infants);
        String expectedPassengerInfo = adults + " Adult";
        if (children > 0) {
            expectedPassengerInfo = expectedPassengerInfo + ", " + children + " Child";
        }
        if (infants > 0) {
            expectedPassengerInfo = expectedPassengerInfo + ", " + infants + " Infant";
        }


        assertThat(passengerInfo, equalTo(expectedPassengerInfo));
        String selectedCurrency = airlinesHomePage.selectCurrency(currency);
        assertThat(selectedCurrency, equalTo(currency));
        airlinesHomePage.clickSearchButton();

    }


    public Stream<Arguments> getArgumentsForTestingOneWayFlightBooking() {
        return Stream.of(Arguments.of("India", "Chennai (MAA)", "Bengaluru (BLR)", 3, 1, 2, "INR"),
                Arguments.of("United States (USA)", "Bengaluru (BLR)", "Chennai (MAA)", 2, 3, 0, "USD"),
                Arguments.of("United States (USA)", "Bangkok (BKK)", "Kabul (KBL)", 1, 1, 1, "USD"));
    }

    @ParameterizedTest(name = "{1} To {2} Two Way")
    @MethodSource("getArgumentsForTestingTwoWayFlightBooking")
    //@Order(2)
    void testTwoWayFlightBooking(String country, String departureCity, String arrivalCity, int adults, int children, int infants, String currency) {
        AirlinesHomePage airlinesHomePage = new AirlinesHomePage(driver, applicationURL);
        airlinesHomePage.open();
        airlinesHomePage.selectCountry(country);
        assertThat(airlinesHomePage.getCountry(), equalTo(country));
        boolean isOneTwoWaySelected = airlinesHomePage.selectTwoWay();
        assertThat(isOneTwoWaySelected, is(true));
        String departureCityValue = airlinesHomePage.selectDepartureCity(departureCity);
        assertThat(departureCityValue, equalTo(departureCity));
        String arrivalCityValue = airlinesHomePage.selectArrivalCity(arrivalCity);
        assertThat(arrivalCityValue, equalTo(arrivalCity));
        LocalDate departureDate = LocalDate.now().plusDays(70);
        String selectedDepartureDate = airlinesHomePage.selectDepartureDate(departureDate);
        assertThat(selectedDepartureDate, equalTo(DateTimeFormatter.ofPattern("dd/MM").format(departureDate)));
        LocalDate arrivalDate = LocalDate.now().plusDays(90);
        String selectedArrivalDate = airlinesHomePage.selectArrivalDate(arrivalDate);
        assertThat(selectedArrivalDate, equalTo(DateTimeFormatter.ofPattern("dd/MM").format(arrivalDate)));
        String passengerInfo = airlinesHomePage.selectPassengers(adults, children, infants);
        String expectedPassengerInfo = adults + " Adult";
        if (children > 0) {
            expectedPassengerInfo = expectedPassengerInfo + ", " + children + " Child";
        }
        if (infants > 0) {
            expectedPassengerInfo = expectedPassengerInfo + ", " + infants + " Infant";
        }


        assertThat(passengerInfo, equalTo(expectedPassengerInfo));
        String selectedCurrency = airlinesHomePage.selectCurrency(currency);
        assertThat(selectedCurrency, equalTo(currency));
        airlinesHomePage.clickSearchButton();

    }


    public Stream<Arguments> getArgumentsForTestingTwoWayFlightBooking() {
        return Stream.of(Arguments.of("India", "Chennai (MAA)", "Bengaluru (BLR)", 3, 1, 2, "INR"),
                Arguments.of("United States (USA)", "Bengaluru (BLR)", "Chennai (MAA)", 2, 3, 0, "USD"),
                Arguments.of("United States (USA)", "Bangkok (BKK)", "Kabul (KBL)", 1, 1, 1, "USD"));
    }
}
