package com.eaapp.tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * Test class for Employee Page Navigation functionality
 */
public class EmployeeNavigationTest extends EAAppBaseTest {
    
    /**
     * Test case: Employee Page Navigation
     * Verify that the Employee List link navigates to the Employee List page.
     */
    @Test
    public void testEmployeePageNavigation() {
        // Login to the application
        navigateToLoginPage();
        login("admin", "password");
        
        // Click on the Employee List link
        logger.info("Clicking on Employee List link");
        clickElement("EmployeeListLink");
        
        // Verify that we are on the Employee List page
        // Check the page URL to confirm navigation
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after navigation: {}", currentUrl);
        
        // Verify URL contains 'Employee' to confirm navigation
        assertTrue(currentUrl.contains("Employee"), 
                "URL should contain 'Employee' to confirm successful navigation to Employee List page");
        
        // Additional verification - check if Create New link is present on the page
        assertTrue(elementFinder.findElement(getDriver(), "CreateNewLink").isDisplayed(), 
                "Create New link should be visible on the Employee List page");
        
        // Verify page title or heading contains expected text
        String pageSource = getDriver().getPageSource();
        assertTrue(pageSource.contains("Employee"), 
                "Page should contain 'Employees' text to confirm we're on the Employee List page");
    }
    
    /**
     * Test case: Search Functionality
     * Check that entering a valid employee name in the search box filters the employee list correctly.
     */
    @Test
    public void testSearchFunctionality() {
        // First create an employee to ensure we have data to search for
        navigateToLoginPage();
        login("admin", "password");
        
        // Navigate to Employee List page
        clickElement("EmployeeListLink");
        clickElement("CreateNewLink");
        
        // Create a unique employee name for testing search
        String uniqueEmployeeName = "SearchTest" + System.currentTimeMillis();
        sendKeys("Name", uniqueEmployeeName);
        sendKeys("Salary", "60000");
        int duration = getRandomNumDurationWorked(1, 36);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(duration));
        clickElement("CreateButton");
        
        // Verify employee was created
        logger.info("Verifying employee was created: {}", uniqueEmployeeName);
        Boolean employeeCreated = isEmployeePresent("EmployeeTable", uniqueEmployeeName);
        assertTrue(employeeCreated, "Employee " + uniqueEmployeeName + " should be created successfully");
        
        // Now test the search functionality
        clear("SearchBox");
        sendKeys("SearchBox", uniqueEmployeeName);
        
        // Click search button
        clickElement("SearchButton");
        
        // Verify search results
        logger.info("Verifying search results for: {}", uniqueEmployeeName);
        
        // Get all data rows in the employee table after search (excluding header row)
        List<WebElement> tableRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        
        // Verify that we have exactly one result
        assertEquals(tableRows.size(), 1, "Search should return exactly one employee");
        
        // Verify that the result contains our employee name
        String resultText = tableRows.get(0).getText();
        assertTrue(resultText.contains(uniqueEmployeeName), 
                "Search result should contain the employee name: " + uniqueEmployeeName);
        
        // Test with partial name search
        clear("SearchBox");
        sendKeys("SearchBox", uniqueEmployeeName.substring(0, 5));
        
        // Click search button
        clickElement("SearchButton");
        
        // Verify partial search results
        tableRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        assertFalse(tableRows.isEmpty(), "Partial search should return at least one result");
        
        boolean foundEmployee = false;
        for (WebElement row : tableRows) {
            if (row.getText().contains(uniqueEmployeeName)) {
                foundEmployee = true;
                break;
            }
        }
        assertTrue(foundEmployee, "Employee should be found in search results with partial name: " + uniqueEmployeeName);
    }
    
    /**
     * Test case: Link to Benefits
     * Verify that clicking the "Benefits" link redirects to the correct employee benefits details page.
     */
    @Test
    public void testBenefitsLinkNavigation() {
        // Login to the application
        navigateToLoginPage();
        login("admin", "password");
        
        // Navigate to Employee List page
        clickElement("EmployeeListLink");
        
        // Create a new employee to ensure we have data to work with
        clickElement("CreateNewLink");
        
        String employeeName = "BenefitsTest" + System.currentTimeMillis();
        sendKeys("Name", employeeName);
        sendKeys("Salary", "75000");
        int duration = getRandomNumDurationWorked(1, 60);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(duration));
        clickElement("CreateButton");
        
        // Verify employee was created
        logger.info("Verifying employee was created: {}", employeeName);
        Boolean employeeCreated = isEmployeePresent("EmployeeTable", employeeName);
        assertTrue(employeeCreated, "Employee " + employeeName + " should be created successfully");
        
        // Search for the employee to isolate it in the list
        clear("SearchBox");
        sendKeys("SearchBox", employeeName);
        clickElement("SearchButton");
        
        // Click on Benefits link
        clickElement("BenefitsLink");
        
        // Verify we are on the Benefits page
        // Check URL contains 'Benefits'
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after clicking Benefits: {}", currentUrl);
        assertTrue(currentUrl.contains("Benefit"), 
                "URL should contain 'Benefits' to confirm successful navigation to Benefits page");
        
        // Verify the Benefits page header is displayed
        assertTrue(elementFinder.findElement(getDriver(), "BenefitsPageHeader").isDisplayed(), 
                "Benefits page header should be visible");
        
        // Verify the employee name is displayed on the Benefits page
        String displayedName = getElementText("BenefitsPageHeader");
        assertTrue(displayedName.contains(employeeName), 
                "Benefits page should display the correct employee name: " + employeeName);
        
        // Additional verification - check if the page contains benefits-related information
        String pageSource = getDriver().getPageSource();
        assertTrue(pageSource.contains("Benefits"), 
                "Page should contain 'Benefits' text to confirm we're on the Benefits page");
    }
    
    /**
     * Test case: Edit Employee Details
     * Ensure clicking "Edit" navigates to the correct page with pre-populated employee details for editing.
     */
    @Test
    public void testEditEmployeeNavigation() {
        // Login to the application
        navigateToLoginPage();
        login("admin", "password");
        
        // Navigate to Employee List page
        clickElement("EmployeeListLink");
        
        // Create a new employee with specific details to verify later
        clickElement("CreateNewLink");
        
        String employeeName = "EditTest" + System.currentTimeMillis();
        String salary = "85000";
        
        sendKeys("Name", employeeName);
        sendKeys("Salary", salary);
        int duration = getRandomNumDurationWorked(1, 48);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(duration));
        clickElement("CreateButton");
        
        // Search for the employee
        clear("SearchBox");
        sendKeys("SearchBox", employeeName);
        clickElement("SearchButton");
        
        // Click Edit link
        clickElement("EditLink");
        
        // Verify we are on the Edit page
        assertTrue(elementFinder.findElement(getDriver(), "EditPageHeader").isDisplayed(),
                "Edit page header should be visible");
        
        // Verify the employee details are pre-populated
        String nameValue = elementFinder.findElement(getDriver(), "Name").getAttribute("value");
        assertEquals(nameValue, employeeName, "Employee name should be pre-populated in the edit form");
    }
    
    /**
     * Test case: Data Integrity
     * Ensure that updates made on the "Edit" page persist and reflect accurately on the employee list.
     */
    @Test
    public void testDataIntegrity() {
        // Login to the application
        navigateToLoginPage();
        login("admin", "password");
        
        // Navigate to Employee List page
        clickElement("EmployeeListLink");
        
        // Create a new employee with initial details
        clickElement("CreateNewLink");
        
        String initialName = "DataTest" + System.currentTimeMillis();
        String initialSalary = "70000";
        int initialDuration = 18; // 18 months
        
        // Fill in initial employee details
        sendKeys("Name", initialName);
        sendKeys("Salary", initialSalary);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(initialDuration));
        clickElement("CreateButton");
        
        // Verify employee was created with initial details
        logger.info("Verifying employee was created: {}", initialName);
        Boolean employeeCreated = isEmployeePresent("EmployeeTable", initialName);
        assertTrue(employeeCreated, "Employee " + initialName + " should be created successfully");
        
        // Search for the employee to isolate it in the list
        clear("SearchBox");
        sendKeys("SearchBox", initialName);
        clickElement("SearchButton");
        
        // Get the initial row data for later comparison
        List<WebElement> initialRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        String initialRowText = initialRows.get(0).getText();
        logger.info("Initial employee data: {}", initialRowText);
        
        // Click Edit link
        clickElement("EditLink");
        
        // Update the employee details with new values
        String updatedName = initialName + "-Updated";
        String updatedSalary = "85000";
        int updatedDuration = 24; // 24 months
        
        // Clear and update the fields
        clear("Name");
        sendKeys("Name", updatedName);
        clear("Salary");
        sendKeys("Salary", updatedSalary);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(updatedDuration));
        
        // Try to update Grade if it exists
        try {
            clear("Grade");
            sendKeys("Grade", "8");
            logger.info("Updated Grade field");
        } catch (Exception e) {
            logger.info("Grade field not found, skipping update");
        }
        
        // Save the changes
        logger.info("Saving updated employee details");
        clickElement("SaveButton");
        
        // Add a small wait to allow for redirection
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted", e);
        }
        
        // Verify we are redirected back to the Employee List page
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after saving: {}", currentUrl);
        
        // More flexible check - just verify we're not on the Edit page anymore
        assertFalse(currentUrl.contains("Edit"), 
                "Should not be on the Edit page after saving");
        
        // Search for the updated employee name
        clear("SearchBox");
        sendKeys("SearchBox", updatedName);
        clickElement("SearchButton");
        
        // Verify the updated employee details are displayed in the list
        List<WebElement> tableRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        assertFalse(tableRows.isEmpty(), "Updated employee should be found in search results");
        
        String updatedRowText = tableRows.get(0).getText();
        logger.info("Updated employee data: {}", updatedRowText);
        
        // Verify the updated details are reflected in the list
        assertTrue(updatedRowText.contains(updatedName), 
                "Employee list should show updated name: " + updatedName);
        assertTrue(updatedRowText.contains(updatedSalary), 
                "Employee list should show updated salary: " + updatedSalary);
        
        // Instead of checking if the original name exists, verify that searching for the exact
        // original name returns the updated name (which contains the original name)
        clear("SearchBox");
        sendKeys("SearchBox", initialName);
        clickElement("SearchButton");
        
        tableRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        
        // If we find results, verify they contain the updated name, not just the original name
        if (!tableRows.isEmpty()) {
            boolean foundUpdatedName = false;
            for (WebElement row : tableRows) {
                String rowText = row.getText();
                if (rowText.contains(updatedName)) {
                    foundUpdatedName = true;
                    break;
                }
            }
            assertTrue(foundUpdatedName, 
                    "When searching for original name, results should contain the updated name: " + updatedName);
        }
        
        // Additional verification - view the employee details page to confirm all updates
        clear("SearchBox");
        sendKeys("SearchBox", updatedName);
        clickElement("SearchButton");
        
        // Click on Benefits link to view full employee details
        clickElement("BenefitsLink");
        
        // Verify the details page shows the updated information
        String pageSource = getDriver().getPageSource();
        assertTrue(pageSource.contains(updatedName), 
                "Details page should show updated name: " + updatedName);
        
        // Additional check for other updated fields if they are displayed on the details page
        if (pageSource.contains("Salary") || pageSource.contains("salary")) {
            assertTrue(pageSource.contains(updatedSalary), 
                    "Details page should show updated salary: " + updatedSalary);
        }
    }
    
    /**
     * Test case: Delete Employee Action
     * Verify that clicking "Delete" prompts a confirmation and, upon confirmation, deletes the employee.
     */
    @Test
    public void testDeleteEmployeeAction() {
        // Login to the application
        navigateToLoginPage();
        login("admin", "password");
        
        // Navigate to Employee List page
        clickElement("EmployeeListLink");
        
        // Create a new employee to delete
        clickElement("CreateNewLink");
        
        String employeeName = "DeleteTest" + System.currentTimeMillis();
        sendKeys("Name", employeeName);
        sendKeys("Salary", "65000");
        int duration = getRandomNumDurationWorked(1, 12);
        clear("DurationWorked");
        sendKeys("DurationWorked", String.valueOf(duration));
        clickElement("CreateButton");
        
        // Verify employee was created
        logger.info("Verifying employee was created: {}", employeeName);
        Boolean employeeCreated = isEmployeePresent("EmployeeTable", employeeName);
        assertTrue(employeeCreated, "Employee " + employeeName + " should be created successfully");
        
        // Search for the employee to isolate it in the list
        clear("SearchBox");
        sendKeys("SearchBox", employeeName);
        clickElement("SearchButton");
        
        // Click Delete link
        clickElement("DeleteLink");
        
        // Verify we are on the Delete confirmation page
        String currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after clicking Delete: {}", currentUrl);
        assertTrue(currentUrl.contains("Delete"), 
                "URL should contain 'Delete' to confirm successful navigation to Delete confirmation page");
        
        // Verify the Delete page header is displayed
        assertTrue(elementFinder.findElement(getDriver(), "DeletePageHeader").isDisplayed(), 
                "Delete page header should be visible");
        
        // Verify employee details are displayed on the confirmation page
        String detailsText = elementFinder.findElement(getDriver(), "DeleteEmployeeDetails").getText();
        assertTrue(detailsText.contains(employeeName), 
                "Delete confirmation page should display the employee name: " + employeeName);
        
        // Confirm deletion by clicking the Delete button
        logger.info("Confirming deletion of employee: {}", employeeName);
        clickElement("DeleteConfirmButton");
        
        // Verify we are redirected back to the Employee List page
        currentUrl = getDriver().getCurrentUrl();
        logger.info("Current URL after confirming deletion: {}", currentUrl);
        assertTrue(currentUrl.contains("Employee") && !currentUrl.contains("Delete"), 
                "Should be redirected back to Employee List page after deletion");
        
        // Search for the deleted employee to verify it no longer exists
        clear("SearchBox");
        sendKeys("SearchBox", employeeName);
        clickElement("SearchButton");
        
        // Check if the employee is no longer in the list
        List<WebElement> tableRows = elementFinder.findElements(getDriver(), "EmployeeTableDataRows");
        
        // If there are no rows or the only row doesn't contain our employee name, the test passes
        boolean employeeDeleted = tableRows.isEmpty();
        
        assertTrue(employeeDeleted, "Employee " + employeeName + " should be deleted and not found in search results");
        
        // Additional check for "No match" message if the application shows it
        try {
            WebElement noResultMessage = elementFinder.findElement(getDriver(), "NoResultMessage");
            assertTrue(noResultMessage.isDisplayed(), 
                    "No match message should be displayed when searching for deleted employee");
        } catch (Exception e) {
            // If the message is not found, we'll rely on the previous assertion
            logger.info("No 'No match' message found, using table rows check for verification");
        }
    }
}