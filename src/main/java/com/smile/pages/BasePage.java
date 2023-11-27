package com.smile.pages;

import com.github.javafaker.Faker;
import com.simon.core.utils.TimeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Locale;

public abstract class BasePage implements IPage {

    private static final long DEFAULT_STEP_WAIT_MILLS = 500L;
    private static final long DEFAULT_MAX_WAIT_MILLS = 30_000L;
    private static final int DEFAULT_LOAD_COMPLETE_COUNT = 3;
    protected WebDriver driver;
    protected Faker faker = Faker.instance(Locale.CHINA);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Whether the form displays the specific error item
     *
     * @param errorItem the specific error item
     * @return Whether the form displays the specific error item
     */
    public boolean hasErrorItem(String errorItem) {
        By errorItemBy = By.xpath(String.format("//div[@class='el-form-item__error' and normalize-space()='%s']", errorItem));
        return !driver.findElements(errorItemBy).isEmpty();
    }

    /**
     * Whether the page displays the specific error message
     *
     * @param errorMsg the specific error message
     * @return Whether the page displays the specific error message
     */
    public boolean hasErrorMessage(String errorMsg) {
        By errorItemBy = By.xpath(String.format("//p[@class='el-message__content' and normalize-space()='%s']", errorMsg));
        return !driver.findElements(errorItemBy).isEmpty();
    }

    public void waitPageLoadReady() {
        waitPageLoadReady(DEFAULT_STEP_WAIT_MILLS, DEFAULT_MAX_WAIT_MILLS, DEFAULT_LOAD_COMPLETE_COUNT);
    }

    public void waitPageLoadReady(long stepWaitMillis, long maxWaitMills, int loadCompleteCount) {
        int sameSourcePageCount = 0;
        long waitTotal = 0L;
        String sourcePage = driver.getPageSource();
        while (waitTotal < maxWaitMills && sameSourcePageCount < loadCompleteCount) {
            TimeUtils.sleep(stepWaitMillis);
            waitTotal += stepWaitMillis;
            String currentSourcePage = driver.getPageSource();
            if (sourcePage.equals(currentSourcePage)) {
                sameSourcePageCount++;
            } else {
                sourcePage = currentSourcePage;
            }
        }
    }
}
