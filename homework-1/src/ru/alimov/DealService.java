package ru.alimov;

import static ru.alimov.TestAnnotations.Test;
import static ru.alimov.TestAnnotations.BeforeSuite;
import static ru.alimov.TestAnnotations.AfterSuite;
import static ru.alimov.TestAnnotations.BeforeTest;
import static ru.alimov.TestAnnotations.AfterTest;
import static ru.alimov.TestAnnotations.CsvSource;
public class DealService {
    @Test(priority = 4)
    @CsvSource(source = "NEW")
    public void insertDeal(String status) {
        System.out.printf("DealService.insertDeal executed. Status=%s\n", status);
    }

    @Test(priority = 3)
    @CsvSource(source = "20, PROCESS, 245")
    public void updateDeal(long dealId, String status, long userId) {
        System.out.printf("DealService.updateDeal. dealId=%s, status=%s, userId=%s\n", dealId, status, userId);
    }

    @CsvSource(source = "20")
    @Test(priority = 2)
    public void deleteDeal(long dealId) {
        System.out.printf("DealService.deleteDeal executed. dealId=%s\n", dealId);
    }

    @Test
    private void prepareDeal() {
        System.out.println("DealService.prepareDeal executed.");
    }

    @BeforeTest()
    public void beforeFunc() {
        System.out.println("DealService.beforeFunc executed");
    }

    @AfterTest()
    public static void afterFunc() {
        System.out.println("DealService.afterFunc executed");
    }

    @BeforeSuite
    public static void initDb() {
        System.out.println("DealService.initDb executed");
    }

    @AfterSuite
    public static void clearDb() {
        System.out.println("DealService.clearDb executed");
    }
}
