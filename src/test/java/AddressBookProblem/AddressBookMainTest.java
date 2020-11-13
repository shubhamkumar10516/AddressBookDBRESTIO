package AddressBookProblem;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import AddressBookProblem.AddressBookMain.IOService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.junit.Assert.*;
import java.io.IOException;
import java.time.*;
import java.util.Arrays;

public class AddressBookMainTest {

	@Test
	public void AddingToCsvFileTest() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		AddressBook addressBook = new AddressBook();
		addressBook.addContact(new Contact("Shubham", "Gaya", "Bihar", 82300, 12334));
		addressBook.addContact(new Contact("Shu", "Pune", "Mah", 88923001, 873512));
		// assertTrue(2== addressBook.addToCsvFile());
	}

	@Test
	public void AddingToJsonFileTest() throws IOException {
		AddressBook addressBook = new AddressBook();
		addressBook.addedToJson(new Contact("Shubham", "Gaya", "Bihar", 82300, 12334));
		addressBook.addedToJson(new Contact("Shu", "Pune", "Mah", 88923001, 873512));
	}

	@Test
	public void checkRetrievalFromDatabase() {
		AddressBookMain addressBookMain = new AddressBookMain();
		int count = addressBookMain.readDataFromDb(IOService.DB_IO);
		System.out.println(count);
		assertEquals(7, count);
	}

	@Test
	public void checkUpdateAndSynchingWithDatabase() {
		AddressBookMain addressBookMain = new AddressBookMain();
		int count = addressBookMain.updateDataFromDb(IOService.DB_IO, "kumar", "sharma");
		assertEquals(3, count);
	}

	@Test
	public void retrieveDataFromGivenPeriod() {
		AddressBookMain addressBookMain = new AddressBookMain();
		int count = addressBookMain.readDataFromDb(IOService.DB_IO, LocalDate.of(2018, 1, 1), LocalDate.now());
		assertEquals(2, count);
	}

	@Test
	public void countContactByStateOrCity() {
		AddressBookMain addressBookMain = new AddressBookMain();
		int count = addressBookMain.readContactByCityOrState("Delhi", "WB");
		assertEquals(2, count);
	}

	@Test
	public void addNewContactAndCheckInSync() throws AddressBookException {
		AddressBookMain addressBookService = new AddressBookMain();
		int result = addressBookService.addNewContactToDB("Ranju@gmail.com", "ranju", "verma", 231001,
				LocalDate.of(2019, 6, 6), "add2", 889945120, "family");
		result += addressBookService.addNewContactToDB("Rishu@gmail.com", "rishu", "sharma", 131001,
				LocalDate.of(2018, 6, 6), "add1", 889935120, "friend");
		System.out.println(result);
		assertTrue(result == 8);
		// boolean res = addressBookService.checkAddressBookInSyncWithDB("rohan");
		// assertTrue(res);
	}

	@Test
	public void given6ContactsAddedToDbShouldMatch() {

		Contact[] arrOfContact = { new Contact("rajeshwar@gmail.com", "rajeshwar", "sharma", 121003, LocalDate.now()),
				new Contact("shami@gmail.com", "shami", "aksh", 123001, LocalDate.now()),
				new Contact("somu@gmail.com", "somu", "kumar", 123456, LocalDate.now()),
				new Contact("ashu231@gmail.com", "ashu", "sharma", 123498, LocalDate.now()),
				new Contact("naresh123@gmail.com", "naresh", "varma", 234765, LocalDate.now()),
				new Contact("rawar@gmail.com", "rawar", "rohit", 123098, LocalDate.now()) };

		AddressBookMain employeePayrollService = new AddressBookMain();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addContactToAdd(Arrays.asList(arrOfContact));
		Instant end = Instant.now();
		System.out.println("Duration taken without threads: " + Duration.between(start, end));
		Instant start1 = Instant.now();
		employeePayrollService.addEmployeeToPayrollUsingThreads(Arrays.asList(arrOfContact));
		Instant end1 = Instant.now();
		System.out.println("Duration taken with threads: " + Duration.between(start, end));
		assertEquals(15, employeePayrollService.countEntries(IOService.DB_IO));
	}

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	// retrieving total number of entries
	@Test
	public void onRetrieveShouldReturnCount() {
		Contact[] arrOfCnt = getListOfContact();
		AddressBookMain addressBookMain;
		addressBookMain = new AddressBookMain(Arrays.asList(arrOfCnt));
		long entries = addressBookMain.countEntries(IOService.REST_IO);
		System.out.println("------" + entries);
		assertEquals(7, entries);
	}

	@Test
	public void newEmployeeAddedToJsonReturnUpdatedCount() {
		AddressBookMain addressBookMain;
		Contact[] arrOfCnt = getListOfContact();
		addressBookMain = new AddressBookMain(Arrays.asList(arrOfCnt));
		Contact contact = new Contact(0, "Mark", "Jucker", 12345, "jucker@fb.com");
		Response response = addEmployeeToJsonServer(contact);
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);
		contact = new Gson().fromJson(response.asString(), Contact.class);
		addressBookMain.addEmployeeToPayroll(contact, IOService.REST_IO);
		long entries = addressBookMain.countEntries(IOService.REST_IO);
		assertEquals(7, entries);
	}
	
	@Test
    public void addingListOfEmployeeAndChecking201ResponseAndCount() {
    	
    	AddressBookMain addressBookMain;
    	Contact[] arrOfCnt = getListOfContact();
    	addressBookMain = new AddressBookMain(Arrays.asList(arrOfCnt));
    	
    	Contact[] arrOfContacts = {
    			new Contact(0, "Bill", "Jucker", 12345, "jucker@fb.com"),
    			new Contact(0, "Sunder", "Jucker", 12345, "jucker@fb.com"),
    			new Contact(0, "Jeff", "Jucker", 12345, "jucker@fb.com")
    	}; 
    	
    	for(Contact contact : arrOfContacts) {
    		Response response = addEmployeeToJsonServer(contact);
    		int statusCode  = response.getStatusCode();
    		assertEquals(201, statusCode);
    		contact = new Gson().fromJson(response.asString(), Contact.class);
    		addressBookMain.addEmployeeToPayroll(contact, IOService.REST_IO);
    	}
    	long entries = addressBookMain.countEntries(IOService.REST_IO);
    	//assertEquals(10, entries);
    }

    
	//methods
	private Response addEmployeeToJsonServer(Contact contact) {
		String empJson = new Gson().toJson(contact);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(empJson);
		return requestSpecification.post("/contact");
	}

	private Contact[] getListOfContact() {
		Response response = RestAssured.get("/contact");
		System.out.println("Employee payroll data: " + response.asString());
		Gson gson = new Gson();
		Contact[] arrOfEmp = gson.fromJson(response.asString(), Contact[].class);
		return arrOfEmp;
	}
}
