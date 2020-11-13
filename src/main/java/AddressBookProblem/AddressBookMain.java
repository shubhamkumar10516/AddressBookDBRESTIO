package AddressBookProblem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookMain {

	public enum IOService {
		DB_IO, REST_IO
	}

	List<Contact> addressBookList;

	public AddressBookMain(List<Contact> addressBookList) {
		this();
		this.addressBookList = new ArrayList<>(addressBookList);
	}

	public AddressBookMain() {

	}

	public int readDataFromDb(IOService ioService) {
		int count = 0;
		if (ioService.equals(IOService.DB_IO))
			count = new AddressbookDBService().retriveDataFromDB();
		return count;
	}

	public int updateDataFromDb(IOService ioService, String lastName, String changedName) {
		int result = 0;
		if (ioService.equals(IOService.DB_IO))
			result = new AddressbookDBService().UpdateDataToDB(lastName, changedName);
		return result;
	}

	public int readDataFromDb(IOService ioService, LocalDate start, LocalDate end) {

		int result = 0;
		if (ioService.equals(IOService.DB_IO))
			result = new AddressbookDBService().retrieveasDateFromDB(start, end);
		return result;
	}

	public int readContactByCityOrState(String city, String state) {

		return new AddressbookDBService().retrieveDataByStateOrCity(city, state);
	}

	public int addNewContactToDB(String email, String first_name, String last_name, int zip, LocalDate startDate,
			String name, int phone, String type) throws AddressBookException {

		int result = new AddressbookDBService().addNewContact(first_name, last_name, zip, email, startDate, name, phone,
				type);
		return result;
	}

	public boolean checkAddressBookInSyncWithDB(String name) {

		return false;
	}

	public void addEmployeeToPayrollUsingThreads(List<Contact> contactList) {
		Map<Integer, Boolean> employeeAdditonStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				employeeAdditonStatus.put(contact.hashCode(), false);
				System.out.println("Employee being added..." + Thread.currentThread().getName());
				employeeAdditonStatus.put(contact.hashCode(), true);
				System.out.println("Employee added.." + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contact.getName());
			thread.start();
		});
		while (employeeAdditonStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(contactList);
	}

	public long countEntries(IOService ioService) {
		if (ioService.equals(IOService.REST_IO))
			return addressBookList.size();
		return addressBookList.size();
	}

	public void addEmployeeToPayroll(Contact contact, IOService restIo) {
		if (restIo.equals(IOService.REST_IO))
			addressBookList.add(contact);
	}

	public void addContactToAdd(List<Contact> asList) {

		addressBookList.forEach(contact -> {
			System.out.println("Employee being added.... " + contact.firstName);
			addContactToAdd(contact.email, contact.firstName, contact.lastName, contact.startDate, contact.getZip());
			System.out.println("Employee added.... " + contact.firstName);
		});
		System.out.println(addressBookList);
	};

	public void addContactToAdd(String email, String firstName, String lastName, LocalDate date, int zip) {
		addressBookList.add(new AddressbookDBService().addNewContact(firstName, lastName, zip, email, date));
	}

	public int readEmployeePayrollData(IOService dbIo) {
		return this.addressBookList.size();
	}

	public void updateEmpSalary(String string, double d, IOService restIo) {
		
	}

	public Contact getEmployeePayrollData(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
