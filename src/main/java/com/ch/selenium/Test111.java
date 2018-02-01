package com.ch.selenium;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Test111 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8081/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test111() throws Exception {
    driver.get(baseUrl + "/NIFDC_SL/");
    driver.findElement(By.id("search")).clear();
    driver.findElement(By.id("search")).sendKeys("5F413778DD01266A");
    driver.findElement(By.id("captcha")).clear();
    driver.findElement(By.id("captcha")).sendKeys("1111");
    driver.findElement(By.xpath("//*[@id='searchhdmBtn']")).click();
    System.out.println(driver.findElement(By.xpath("//html/body/table/tbody/tr[2]/td/table/tbody/tr/td/span")).getText().matches("仿制药质量和疗效一致性评价申请表*"));
    assertTrue(driver.findElement(By.xpath("//html/body/table/tbody/tr[2]/td/table/tbody/tr/td/span")).getText().matches("仿制药质量和疗效一致性评价申请表（一致性评价）"));
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
  
public static void main(String args[]) throws Exception{
	Test111 test111=new Test111();
	test111.setUp();
	test111.test111();
}
}
