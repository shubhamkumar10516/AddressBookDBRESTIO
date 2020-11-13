package AddressBookProblem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBook {

	private static final String OBJECT_LIST_SAMPLE = "/home/shubham/Desktop/BridgeAssignment/addressBook-list.csv";
	private List<Contact> cList = new ArrayList<>();
	Gson gson = new Gson();

	public void addContact(Contact contact) throws IOException {
		cList.add(contact);
		addedToJson(contact);
	}

	public Contact returnContact(String name) {
		for (Contact cnt : cList) {
			if (cnt.getName().equals(name))
				return cnt;
		}
		return null;
	}

	public void deleteContact(String name) {
		Contact contact = null;
		for (Contact cnt : cList)
			if (cnt.getName() == name)
				contact = cnt;
		if (cList.remove(contact))
			System.out.println("Contact deleted!!");
		else
			System.out.println("Contact not founded!!");
	}

	public void editContact(String name, Contact contact) {
		int i = 0;
		Contact contact1 = null;
		for (i = 0; i < cList.size(); i++)
			if (cList.get(i).getName().equals(name)) {
				contact1 = cList.get(i);
				cList.add(i, contact);
				cList.remove(contact1);
				break;
			}
		System.out.println(cList.get(i).toString(contact));
		System.out.println("Contact Edited:: ");
	}

	public void searchByCity(String city) {
		List<Contact> pList = cList.stream().filter(name -> {
			if (name.getCity().contentEquals(city))
				return true;
			return false;
		}).collect(Collectors.toList());
		pList.stream().forEach(n -> {
			System.out.println(n.getName());
		});
	}

	public void searchByState(String state) {
		System.out.println("...searching person in state...");
		List<Contact> pList = cList.stream().filter(name -> {
			if (name.getState().equals(state))
				return true;
			return false;
		}).collect(Collectors.toList());
		pList.stream().forEach(n -> {
			System.out.println("Name of person : " + n.getName());
		});
	}

	public void sortAlphabeticallyByPersonName() {
		cList.sort(Comparator.comparing(a -> a.getName()));
		cList.forEach(n -> {
			System.out.println(n.getName());
		});
	}

	public void addedToJson(Contact contact) throws IOException {
		FileWriter fileWriter = new FileWriter("contact.json");
		gson.toJson(contact, fileWriter);
		// JsonArray.add(gson);
		fileWriter.close();
		System.out.println("JSON string write to a file successfully");
	}
	
	public int addToCsvFile() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

		try (Writer writer = Files.newBufferedWriter(Paths.get(OBJECT_LIST_SAMPLE));

				CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {
			String[] headerRecord = { "Name", "City", "State", "Phone", "Zip" };
			csvWriter.writeNext(headerRecord);
			int count = 0;
			for (Contact cnt : cList) {
				csvWriter.writeNext(new String[] { cnt.getName(), cnt.getCity(), cnt.getState(),
						Long.toString(cnt.getPhoneNumber()), Integer.toString(cnt.getZip()) });
				count++;
			}
			return count;
		}
	}
}
