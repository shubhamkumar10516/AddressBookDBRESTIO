package AddressBookProblem;

import java.util.*;


public class Dictionary {

	HashMap<String, String> cityPersonDictionary = new HashMap<>();
	HashMap<String, String> statePersonDictionary = new HashMap<>();

	public void addToDictionary(String name, String state, String city) {
		cityPersonDictionary.put(city, name);
		statePersonDictionary.put(state, name);
	}

	public void viewPersonByCityAndState() {
		cityPersonDictionary.forEach((k, v) -> System.out.println("City : " + k + ", Person : " + v));
		statePersonDictionary.forEach((k, v) -> System.out.println("State : " + k + ", Person : " + v));
	}
	
	public void countByState(String state) {
		int size = (int) statePersonDictionary.entrySet().stream().filter(e -> e.getKey().equals(state))
				                       .map(Map.Entry :: getValue).count();
		 System.out.println(size);
	 }
	
	public void countByCity(String city) {
		 int count = (int)statePersonDictionary.entrySet().stream().filter(e -> e.getKey().equals(city)).
				 map(Map.Entry::getValue).count();
		System.out.println(count);
	 }
	
}
