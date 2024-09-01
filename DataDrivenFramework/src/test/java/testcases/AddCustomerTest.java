package testcases;

import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import utilities.DataUtil;

public class AddCustomerTest extends BaseTest{

	@Test(dataProviderClass = DataUtil.class, dataProvider = "dp")
	public void addCustomer(String firstName, String lastName, String postCode) {
		
		click("addCustBtn_CSS");
		type("firstname_CSS",firstName);
		type("lastname_CSS",lastName);
		type("postcode_CSS",postCode);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		click("addcustomer_CSS");
		
		Alert alert = driver.switchTo().alert();
		Assert.assertTrue(alert.getText().contains("Customer added successfully"),"Customer not added successfully");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.accept();
	}
	
	
	
}
