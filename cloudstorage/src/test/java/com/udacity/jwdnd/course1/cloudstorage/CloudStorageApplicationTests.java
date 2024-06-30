package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.edgedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new EdgeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

		// Redirect to login page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkLogin")));
		WebElement login = driver.findElement(By.id("linkLogin"));
		login.click();
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertTrue(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void doCrudFile(){
		// Create a test account
		doMockSignUp("Crud File","Test","CrudFile","123");
		doLogIn("CrudFile", "123");
		String fileName = "cat.jpg";

		// Access file page
		driver.get("http://localhost:" + this.port + "/home/file/list");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		// Upload file
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());
		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		Assertions.assertTrue(driver.getPageSource().contains("cat.jpg"));

		// Delete file
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonDeleteFile")));
		WebElement buttonDeleteFile = driver.findElement(By.id("buttonDeleteFile"));
		buttonDeleteFile.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmDeleteFile")));
		WebElement buttonConfirmDeleteFile = driver.findElement(By.id("confirmDeleteFile"));
		buttonConfirmDeleteFile.click();
		Assertions.assertFalse(driver.getPageSource().contains("cat.jpg"));

		// Upload file again to view
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());
		uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		Assertions.assertTrue(driver.getPageSource().contains("cat.jpg"));

		// Download file
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("downloadFile")));
		WebElement downloadFile = driver.findElement(By.id("downloadFile"));
		downloadFile.click();

		// View file
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonViewFile")));
		WebElement buttonViewFile = driver.findElement(By.id("buttonViewFile"));
		buttonViewFile.click();
		Assertions.assertTrue(driver.getPageSource().contains("cat.jpg"));
	}

	@Test
	public void doCrudNote(){
		doMockSignUp("Crud Note","Test","CrudNote","123");
		doLogIn("CrudNote", "123");

		// Access file page
		driver.get("http://localhost:" + this.port + "/home/note/list");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		// Create note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteCreateButton")));
		WebElement noteCreateButton = driver.findElement(By.id("noteCreateButton"));
		noteCreateButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("test note title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("test note description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSaveNote")));
		WebElement saveNoteButton = driver.findElement(By.id("buttonSaveNote"));
		saveNoteButton.click();

		Assertions.assertTrue(driver.getPageSource().contains("test note description"));

		// Edit note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteUpdating")));
		WebElement noteEditButton = driver.findElement(By.id("buttonNoteUpdating"));
		noteEditButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("test note description update");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSaveNote")));
		saveNoteButton = driver.findElement(By.id("buttonSaveNote"));
		saveNoteButton.click();

		Assertions.assertTrue(driver.getPageSource().contains("test note description update"));

		// Delete note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteDeleting")));
		WebElement noteDeleteButton = driver.findElement(By.id("buttonNoteDeleting"));
		noteDeleteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmDeleteNote")));
		WebElement confirmDeleteNoteButton = driver.findElement(By.id("confirmDeleteNote"));
		confirmDeleteNoteButton.click();
		Assertions.assertFalse(driver.getPageSource().contains("test note title"));
	}

	@Test
	public void doCrudCredential(){
		doMockSignUp("Crud Credential","Test","CrudCredential","123");
		doLogIn("CrudCredential", "123");

		// Access file page
		driver.get("http://localhost:" + this.port + "/home/credential/list");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		// Create credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialCreateButton")));
		WebElement noteCreateButton = driver.findElement(By.id("credentialCreateButton"));
		noteCreateButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.click();
		credentialUrl.sendKeys("http://localhost:8080/");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.click();
		credentialUsername.sendKeys("testCredentialUsername");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.click();
		credentialPassword.sendKeys("testCredentialPassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSaveCredential")));
		WebElement buttonSaveCredential = driver.findElement(By.id("buttonSaveCredential"));
		buttonSaveCredential.click();

		Assertions.assertTrue(driver.getPageSource().contains("testCredentialUsername"));

		// Edit credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialUpdating")));
		WebElement credentialEditButton = driver.findElement(By.id("buttonCredentialUpdating"));
		credentialEditButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.click();
		credentialUsername.sendKeys("Update");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSaveCredential")));
		noteCreateButton = driver.findElement(By.id("buttonSaveCredential"));
		noteCreateButton.click();

		Assertions.assertTrue(driver.getPageSource().contains("testCredentialUsernameUpdate"));

		// Delete credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialDeleting")));
		WebElement buttonCredentialDeleting = driver.findElement(By.id("buttonCredentialDeleting"));
		buttonCredentialDeleting.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmDeleteCredential")));
		WebElement confirmDeleteNoteButton = driver.findElement(By.id("confirmDeleteCredential"));
		confirmDeleteNoteButton.click();
		Assertions.assertFalse(driver.getPageSource().contains("testCredentialUsernameUpdate"));
	}
}
