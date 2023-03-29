package telran.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class CalculatorApplication {

	public static void main(String[] args) {				
		ArrayList<Item> calculationItems = getCalculationMenuItems();	
		Menu calculationMenu = new Menu("Calculation operations", calculationItems);
		
		ArrayList<Item> dateItems = getDateMenuItems();	
		Menu dateMenu = new Menu("Date operations", dateItems);
				
		Menu mainMenu = new Menu("Main menu", calculationMenu, dateMenu, Item.exit());	
		
		mainMenu.perform(new StandartInputOutput());
		
	}
	
	
	private static ArrayList<Item> getCalculationMenuItems() {
		ArrayList<Item> res = new ArrayList<Item>();
		
		Consumer<InputOutput> addition = io -> {
			double x = io.readNumber("enter the first term", "must be a number");
			double y = io.readNumber("enter the second term", "must be a number");
			io.writeLine(x + y);
		};
		res.add(Item.of("Addition", addition));
		
		Consumer<InputOutput> subtraction = io -> {
			double x = io.readNumber("enter the first term", "must be a number");
			double y = io.readNumber("enter the second term", "must be a number");
			io.writeLine(x - y);
		};
		res.add(Item.of("Subtraction", subtraction));
		
		Consumer<InputOutput> multiplication = io -> {
			double x = io.readNumber("enter the first term", "must be a number");
			double y = io.readNumber("enter the second term", "must be a number");
			io.writeLine(x * y);
		};
		res.add(Item.of("Multiplication", multiplication));
		
		Consumer<InputOutput> division = io -> {
			double x = io.readNumber("enter the first term", "must be a number");
			double y = io.readNumber("enter the second term", "must be a number");
			io.writeLine(x / y);
		};
		res.add(Item.of("Division", division));
		
		res.add(Item.exit());
		
		return res;
	}

	private static ArrayList<Item> getDateMenuItems() {
		ArrayList<Item> res = new ArrayList<Item>();
		
		Consumer<InputOutput> addDays = io -> {
			LocalDate date = io.readDateISO("enter date", "enter the date in the format YYYY-MM-DD");
			int x = io.readInt("enter number of days to add", "wrong enter");
			
			io.writeLine(date.plusDays(x));
		};
		res.add(Item.of("Add days", addDays));
		
		Consumer<InputOutput> subtractDays = io -> {
			LocalDate date = io.readDateISO("enter date", "enter the date in the format YYYY-MM-DD");
			int x = io.readInt("enter number of days", "wrong enter");
			
			io.writeLine(date.minusDays(x));
		};
		res.add(Item.of("Subtract days", subtractDays));
		
		res.add(Item.exit());
		
		return res;
	}

	

	

}
