package AddressBookProblem;

import java.time.LocalDate;

import AddressBookProblem.AddressBookMain.IOService;

public class Contact {
	private String name;
	private String city;
	private String state;
	private int zip;
	private long phoneNumber;
	private int id;
	String email;
	String firstName;
	String lastName;
	LocalDate startDate;

	public Contact(String name, String city, String state, int zip, long phoneNumber) {
		this.name = name;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
	}

	public Contact(int id, String firstName, String lastName, int zip, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.zip = zip;
		this.email = email;
	}

	public Contact(String email, String firstName, String lastName, int zip, LocalDate startDate) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.zip = zip;
		this.startDate = startDate;
	}

	public void editContacts(String name, String city, String state, int zip, long phoneNumber) {
		this.name = name;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		AddressBook adb = new AddressBook();
		adb.editContact(name, new Contact(name, city, state, zip, phoneNumber));
	}

	public String toString(Contact contact) {
		return (contact.name + " " + contact.city + " " + contact.state + " " + contact.phoneNumber + " "
				+ contact.zip);
	}

	// setters and getters
	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public int getZip() {
		return zip;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}
}
