package com.myairlines.airlinetest.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;

public class AirlinesHomePage {

    private WebDriver driver;
    private String applicationUrl;

    public AirlinesHomePage(WebDriver driver, String applicationUrl) {
        this.driver = driver;
        this.applicationUrl = applicationUrl;
    }

    public void open() {
        driver.get(applicationUrl);
    }

    public void selectCountry(String country) {
        driver.findElement(By.xpath("//input[@id='autosuggest']")).sendKeys(country.substring(0, 3));
        driver.findElement(By.linkText(country)).click();
    }

    public String getCountry() {
        return driver.findElement(By.xpath("//input[@id='autosuggest']")).getAttribute("value");
    }


    public void clickSearchButton() {
        driver.findElement(By.id("ctl00_mainContent_btn_FindFlights")).click();

    }

    public String getDepartureCityErrorMessage() {
        return driver.findElement(By.id("view-origin-station")).getText();
    }

    public String getArrivalCityErrorMessage() {
        return driver.findElement(By.id("view-destination-station")).getText();
    }

    public boolean checkIfOneWaySelected() {
        return driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_0")).isSelected();
    }

    public boolean selectTwoWay() {
        driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_1")).click();
        return driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_1")).isSelected();
    }

    public String selectDepartureCity(String departureCity) {
        WebElement departureCityElement = driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT"));
        departureCityElement.click();
        driver.findElement(By.xpath("//div[@id='ctl00_mainContent_ddl_originStation1_CTNR']//a[normalize-space()='" + departureCity + "']")).click();
        return departureCityElement.getAttribute("value");
    }

    public String selectArrivalCity(String arrivalCity) {
        WebElement departureCityElement = driver.findElement(By.id("ctl00_mainContent_ddl_destinationStation1_CTXT"));
        departureCityElement.click();
        driver.findElement(By.xpath("//div[@id='ctl00_mainContent_ddl_destinationStation1_CTNR']//a[normalize-space()='" + arrivalCity + "']")).click();
        return departureCityElement.getAttribute("value");
    }

    public String selectDepartureDate(LocalDate departureDate) {
        WebElement departureDateElement = driver.findElement(By.id("ctl00_mainContent_view_date1"));
        departureDateElement.click();
        String month = departureDate.getMonth().toString();
        int day = departureDate.getDayOfMonth();
        WebElement firstCard = driver.findElement(By.className("ui-datepicker-group-first"));
        WebElement monthElementInFirstCard = firstCard.findElement(By.className("ui-datepicker-month"));
        String monthInFirstCard = monthElementInFirstCard.getText();
        while (!monthInFirstCard.equalsIgnoreCase(month)) {
            driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();
            monthInFirstCard = driver.findElement(By.className("ui-datepicker-group-first")).findElement(By.className("ui-datepicker-month")).getText();
        }
        driver.findElement(By.className("ui-datepicker-group-first")).findElement(By.linkText(String.valueOf(day))).click();
        return departureDateElement.getAttribute("value");
    }
    public String selectArrivalDate(LocalDate arrivalDate) {

        WebElement arrivalDateElement = driver.findElement(By.id("ctl00_mainContent_view_date2"));
        arrivalDateElement.click();
        String month = arrivalDate.getMonth().toString();
        int day = arrivalDate.getDayOfMonth();
        WebElement firstCard = driver.findElement(By.className("ui-datepicker-group-first"));
        WebElement monthElementInFirstCard = firstCard.findElement(By.className("ui-datepicker-month"));
        String monthInFirstCard = monthElementInFirstCard.getText();
        while (!monthInFirstCard.equalsIgnoreCase(month)) {
            driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();
            monthInFirstCard = driver.findElement(By.className("ui-datepicker-group-first")).findElement(By.className("ui-datepicker-month")).getText();
        }
        driver.findElement(By.className("ui-datepicker-group-first")).findElement(By.linkText(String.valueOf(day))).click();
        return arrivalDateElement.getAttribute("value");

    }

    public boolean checkIfReturnDateDisabled() {
        return driver.findElement(By.className("picker-second")).getAttribute("style").contains("opacity: 0.5");
    }

    public String selectPassengers(int adults, int children, int infants) {
        driver.findElement(By.id("divpaxinfo")).click();
        for (int counter = 0; counter < adults - 1; counter++) {
            driver.findElement(By.id("hrefIncAdt")).click();
        }
        driver.findElement(By.id("hrefIncAdt")).click();
        driver.findElement(By.id("hrefDecAdt")).click();
        for (int counter = 0; counter < children; counter++) {
            driver.findElement(By.id("hrefIncChd")).click();
        }
        driver.findElement(By.id("hrefIncChd")).click();
        driver.findElement(By.id("hrefDecChd")).click();
        for (int counter = 0; counter < infants; counter++) {
            driver.findElement(By.id("hrefIncInf")).click();
        }
        driver.findElement(By.id("hrefIncInf")).click();
        driver.findElement(By.id("hrefDecInf")).click();

        driver.findElement(By.id("btnclosepaxoption")).click();

        System.out.println("passenger info " + driver.findElement(By.id("divpaxinfo")).getText());

        return driver.findElement(By.id("divpaxinfo")).getText();
    }

    public String selectCurrency(String currency) {
        Select currencySelect = new Select(driver.findElement(By.id("ctl00_mainContent_DropDownListCurrency")));
        currencySelect.selectByVisibleText(currency);
        return currencySelect.getFirstSelectedOption().getText();
    }



}
