package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import extentlisteners.ExtentListeners;
import utilities.DbManager;
import utilities.ExcelReader;
import utilities.MonitoringMail;

public class BaseTest {

	/*
	 * 
	 * WebDriver
	 * TestNG
	 * Keywords
	 * Screenshot
	 * Implicit vs Explicit
	 * Extent Reports
	 * Excel
	 * PRoperties
	 * Log4j
	 * JavaMail
	 * JDBC
	 * 
	*/
	
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static ExcelReader excel = new ExcelReader("./src/test/resources/excel/testdata.xlsx");
	private static Properties OR = new Properties(); 
	private static Properties config = new Properties();
	private static FileInputStream fis;
	private static Logger log = Logger.getLogger(BaseTest.class);
	private static MonitoringMail mail = new MonitoringMail();
	
	
	
	public void click(String locatorKey) {
		try {
		if (locatorKey.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locatorKey))).click();
		} else if (locatorKey.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locatorKey))).click();
		} else if (locatorKey.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locatorKey))).click();
		}
		log.info("Clicking on an element " + locatorKey);
		ExtentListeners.test.info("Clicking on an element " + locatorKey);
		}catch(Throwable t) {
			ExtentListeners.test.fail("Failed while clicking on an Element: "+locatorKey);
		
			Assert.fail(t.getMessage());
		}
	}

	public void type(String locatorKey, String value) {

		try {
		if (locatorKey.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locatorKey))).sendKeys(value);
		} else if (locatorKey.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locatorKey))).sendKeys(value);
		} else if (locatorKey.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locatorKey))).sendKeys(value);
		}

		log.info("Typing in " + locatorKey + " and entered the value as " + value);
		ExtentListeners.test.info("Typing in " + locatorKey + " and entered the value as " + value);
		}catch(Throwable t) {
			ExtentListeners.test.fail("Failed while Typing an Element: "+locatorKey);
		
			Assert.fail(t.getMessage());
		}
	}
	
	
	public boolean isElementPresent(String locatorKey) {

		try {
			if (locatorKey.endsWith("_ID")) {
				driver.findElement(By.id(OR.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_XPATH")) {
				driver.findElement(By.xpath(OR.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_CSS")) {
				driver.findElement(By.cssSelector(OR.getProperty(locatorKey)));
			}
			log.info("Finding an element " + locatorKey);
			ExtentListeners.test.info("Finding an element " + locatorKey);
			
			return true;
		} catch (Throwable t) {
			log.info("Error while Finding an element " + locatorKey);
			ExtentListeners.test.info("Error while Finding an element " + locatorKey);
		
			return false;
		}
	}
	
	
	
	
	@BeforeSuite
	public void setUp() {
		
		PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");
		
		
		log.info("---Test Execution started---");
		
		try {
			fis = new FileInputStream("./src/test/resources/properties/config.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			config.load(fis);
			log.info("config property file loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			fis = new FileInputStream("./src/test/resources/properties/OR.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			OR.load(fis);
			log.info("OR property file loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(config.getProperty("browser").equals("chrome")) {
			
			
			driver = new ChromeDriver();
			log.info("Chrome browser launched");
			
		}else if(config.getProperty("browser").equals("firefox")) {
			
			
			driver = new FirefoxDriver();
			log.info("Firefox browser launched");
			
		}
		
		driver.get(config.getProperty("testsiteurl"));
		log.info("Navigated to : "+config.getProperty("testsiteurl"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(config.getProperty("implicit.wait"))));
		
		wait = new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(config.getProperty("explicit.wait"))));
		
		try {
			DbManager.setMysqlDbConnection();
			log.info("DB Connection established");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@AfterSuite
	public void tearDown() {
		
		driver.quit();
		log.info("Test execution completed !!!");
	}
	
	
	
	
	
	
	
	
	
	
	
}
