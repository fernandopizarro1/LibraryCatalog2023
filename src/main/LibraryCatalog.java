package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;

public class LibraryCatalog {
	private List<Book> books;
	private List<User> users;

	/** Constructs a LibraryCatalog object to manage a list of books and users for a library system.
	 * LibraryCatalog allows tasks such as adding new books, deleting old books, and updating existing books.
	 * ArrayLists are used due to having to access specific indexes frequently.
	 * @author Fernando L. Pizarro Diaz
	 * 
	 * @throws IOException
	 */	
	public LibraryCatalog() throws IOException {
		this.books = new ArrayList<Book>();
		this.books = getBooksFromFiles();
		this.users = new ArrayList<User>();
		this.users = getUsersFromFiles();
	}

	/** getBooksFromFiles is a method that returns a list of books from the specified file
     * 
     * @return the list of books that appear int the catalog.csv file 
     * @throws IOException
     */
	private List<Book> getBooksFromFiles() throws IOException {
		//Use a buffered reader to read the lines of all the books 
		BufferedReader reader = new BufferedReader(new FileReader("data\\catalog.csv"));
		String thisline;
		try{
			//Skip first line 
			reader.readLine();
			while((thisline = reader.readLine()) != null){
				//split the line by the comma
				String[] book_Line = thisline.split(",");
				//Use each individual part to create a new book
				this.books.add(new Book(Integer.parseInt(book_Line[0].trim()), book_Line[1].trim(), book_Line[2].trim(), book_Line[3].trim(), LocalDate.parse(book_Line[4].trim()), Boolean.parseBoolean(book_Line[5].trim())));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.books;
	}
	
	/** getUsersFromFiles is a method that returns a list of users from the specified file
     * 
     * @return the list of users that appear in the user.csv file 
     * @throws IOException
     */
	private List<User> getUsersFromFiles() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("data\\user.csv"));
		//resultList to create the list of the users books, thisline for the readline() of the BufferedReader 
		List<Book> resultList;
		String thisline;
		try{
			//Skip first line
			reader.readLine();
			while((thisline = reader.readLine()) != null){
				//Split the line using commas to get the appropriate data 
				String[] line = thisline.split(",");
				//Create a new ArrayList for the books
				resultList = new ArrayList<>();
				//make sure the user has a list of books
				if(line.length > 2){
					//Split this list by space to get the respective id's, then add the book with that id to the list
					String[] list = line[2].split(" ");
					for(String number : list){
						//remove curlybrackets from id's
						int real_id = Integer.parseInt(number.replace("{", "").replace("}", ""));
						resultList.add(this.books.get(real_id - 1));
						}
				}
				this.users.add(new User(Integer.parseInt(line[0].trim()), line[1].trim(), resultList));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.users;
	}

	/** this method return the existing list of books in the database.
	 * 
	 * @return the existing list of books
	 */
	public List<Book> getBookCatalog() {
		return this.books;
	}

	/** this method returns the existing list of users in the databse.
	 * 
	 * @return the existing list of users
	 */
	public List<User> getUsers() {
		return this.users;
	}

	/** this method adds a new book to the existing list of books, with Id size() + 1, date of September 15, 2023
	 * and not being checked out 
	 * 
	 * @param title - the title of the new book
	 * @param author - the author of the new book
	 * @param genre - the genre of the new book
	 */
	public void addBook(String title, String author, String genre) {
		this.books.add(new Book(this.books.size() + 1, title, author, genre,LocalDate.of(2023,9,15), false));
		return;
	}

	/** this method removes a book from the existing list at a given index
	 * 
	 * @param id - the id of the book to be removed
	 */
	public void removeBook(int id) {
		if(id <= 0 || id > this.books.size()) throw new IndexOutOfBoundsException();
		this.books.remove(id - 1);
		return;
	}

	/** this method tries to checkout a book in the library.
	 * 
	 * @param id - the id of the book to be checked out.
	 * @return true if the book was correctly checked out or false if it was already checked out.
	 */
	public boolean checkOutBook(int id) {
		if(id <= 0 || id > this.books.size()) throw new IndexOutOfBoundsException();
		if(this.books.get(id - 1).isCheckedOut()) return false;
		this.books.get(id - 1).setCheckedOut(true);
		this.books.get(id - 1).setLastCheckOut(LocalDate.of(2023,9,15));
		return true;
	}

	/** this method tries to return a book to the library to be available again. 
	 * 
	 * @param id - the id of the book to be returned. 
	 * @return true if the book was successfully returned or false if it was already in the library.
	 */
	public boolean returnBook(int id) {
		if(id <= 0 || id > this.books.size()) throw new IndexOutOfBoundsException();
		if(!this.books.get(id - 1).isCheckedOut()) return false;
		this.books.get(id - 1).setCheckedOut(false);
		return true;
	}
	
	/** this method reviews if the book is checked out from the library. 
	 * 
	 * @param id - the id of the book to review if checked out
	 * @return true if the book is checked out from the library
	 */
	public boolean getBookAvailability(int id) {
		if(id <= 0 || id > this.books.size()) throw new IndexOutOfBoundsException();
        return this.books.get(id - 1).isCheckedOut();
	}

	/** this method counts the number of books in the library with the given title
	 * 
	 * @param title - the title of the book to count
	 * @return the number of books in the library with the title
	 */
	public int bookCount(String title) {
        int count = 0; 
        for(int i = 0; i < this.books.size(); i++) {
            if(this.books.get(i).getTitle().equals(title)){
                count++;
            }
        }
        return count;
	}

	/** this method uses many of the methods previously created to make a new report.txt that shows the number of books in
	 * a specific genre, how many books are checked out, and the users that owe money for the books they have checked out and 
	 * the amount they owe.
	 * 
	 * @throws IOException
	 */
	public void generateReport() throws IOException {
		
		String output = "\t\t\t\tREPORT\n\n";
		output += "\t\tSUMMARY OF BOOKS\n";
		output += "GENRE\t\t\t\t\t\tAMOUNT\n";
		/*
		 * In this section you will print the amount of books per category.
		 * 
		 * Place in each parenthesis the specified count. 
		 * 
		 * Note this is NOT a fixed number, you have to calculate it because depending on the 
		 * input data we use the numbers will differ.
		 * 
		 * How you do the count is up to you. You can make a method, use the searchForBooks()
		 * function or just do the count right here.
		 */
		output += "Adventure\t\t\t\t\t" + searchForBook(x -> x.getGenre().equals("Adventure")).size() + "\n";
		output += "Fiction\t\t\t\t\t\t" + searchForBook(x -> x.getGenre().equals("Classics")).size() + "\n";
		output += "Classics\t\t\t\t\t" + searchForBook(x -> x.getGenre().equals("Fiction")).size() + "\n";
		output += "Mystery\t\t\t\t\t\t" + searchForBook(x -> x.getGenre().equals("Mystery")).size() + "\n";
		output += "Science Fiction\t\t\t\t\t" + searchForBook(x -> x.getGenre().equals("Science Fiction")).size() + "\n";
		output += "====================================================\n";
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + (this.books.size()) + "\n\n";
		
		/*
		 * This part prints the books that are currently checked out
		 */
		output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";
		/*
		 * Here you will print each individual book that is checked out.
		 * 
		 * Remember that the book has a toString() method. 
		 * Notice if it was implemented correctly it should print the books in the 
		 * expected format.
		 * 
		 * PLACE CODE HERE
		 */
		List<Book> available_books = searchForBook(x -> this.getBookAvailability(x.getId()) == true);
		for(Book book : available_books){
				output += book.toString() + "\n";
		}

		output += "====================================================\n";
		//(/*Place here the total number of books that are checked out*/)
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + available_books.size() + "\n\n";
		
		
		/*
		 * Here we will print the users the owe money.
		 */
		output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";
		/*
		 * Here you will print all the users that owe money.
		 * The amount will be calculating taking into account 
		 * all the books that have late fees.
		 * 
		 * For example if user Jane Doe has 3 books and 2 of them have late fees.
		 * Say book 1 has $10 in fees and book 2 has $78 in fees.
		 * 
		 * You would print: Jane Doe\t\t\t\t\t$88.00
		 * 
		 * Notice that we place 5 tabs between the name and fee and 
		 * the fee should have 2 decimal places.
		 * 
		 * PLACE CODE HERE!
		 */

		float total = 0;
		//For each user
		for(User user : this.users){
			float amount = 0; 
			for(Book book : user.getCheckedOutList()){
				amount += book.calculateFees();
			}
			// if amount is positive, then print the user and the amount owed
			if(amount  > 0){
				output += user.getName() + "\t\t\t\t\t$" + String.format("%.2f", amount) + "\n";
			}
			total += amount;
		}	
		output += "====================================================\n";
		//(/*Place here the total amount of money owed to the library.*/)
		output += "\t\t\t\tTOTAL DUE\t$" + String.format("%.2f", total) + "\n\n\n";
		output += "\n\n";
		System.out.println(output);// You can use this for testing to see if the report is as expected.
		
		/*
		 * Here we will write to the file.
		 * 
		 * The variable output has all the content we need to write to the report file.
		 * 
		 * PLACE CODE HERE!!
		 */
		BufferedWriter writer = new BufferedWriter(new FileWriter("report\\report.txt"));
		try{
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * BONUS Methods
	 * 
	 * You are not required to implement these, but they can be useful for
	 * other parts of the project.
	 */

	/** this method iterates over the existing library of books
	 * and searches the books that meet the criteria from the function
	 * 
	 * @param func - A lambda function that filters the books that meet the criteria from the function
	 * @return A list of books that meet the criteria from the function
	 */
	public List<Book> searchForBook(FilterFunction<Book> func) {
		List<Book> result = new ArrayList<Book>();
		for(Book book : this.books){
			if(func.filter(book)){
				result.add(book);
			}
		}
		return result;
	}
	
	/**this method iterates over a the existing list of users 
	 * and searches the books thath meet the criteria from the function 
	 * 
	 * @param func - A lambda function that filters the users that meet the criteria from the function 
	 * @return A list of the users that meet the criteria
	 */
	public List<User> searchForUsers(FilterFunction<User> func) {
		List<User> result = new ArrayList<User>();
		for(User user : this.users){
			if(func.filter(user)){
				result.add(user);
			}
		}
        return result;
	}
	
}
