package frameworkSandbox;

import framework.Commands;
import framework.Drivers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class BrowserTest {

    public static WebDriver d;

    @BeforeTest
    public static void beforeTest(){
        DefaultConfig.appAddress = "http://sl-test.herokuapp.com/guinea_pig/file_upload";
        d = DefaultConfig.defaultConfig();
//        d = Drivers.safariDriver(d, "http://sl-test.herokuapp.com/guinea_pig/file_upload");
//        d = Drivers.firefoxDriver(d, "http://sl-test.herokuapp.com/guinea_pig/file_upload");
//        d = Drivers.chromeDriver(null, "http://sl-test.herokuapp.com/guinea_pig/file_upload");
        d.manage().window().maximize();
    }

    @Test
    public void testSauce() throws Exception {
        WebElement upload = d.findElement(By.id("myfile"));
        Commands.printStep("thisStep", "do this thing");
        upload.sendKeys("/Users/darrinwhitley/Downloads/057_original_print.jpg");
        d.findElement(By.id("submit")).click();
        d.findElement(By.tagName("img"));
        Assert.assertEquals("057_original_print.jpg (image/jpeg)", d.findElement(By.tagName("p")).getText());
    }

    @Test
    public void testSauce1() throws Exception {
        WebElement upload = d.findElement(By.id("myfile"));
        Commands.printStep("thisStep", "do this thing");
        upload.sendKeys("/Users/darrinwhitley/Downloads/057_original_print.jpg");
        d.findElement(By.id("submit")).click();
        d.findElement(By.tagName("img"));
        Assert.assertEquals("057_original_print.jpg (image/jpeg)", d.findElement(By.tagName("p")).getText());
    }
}
