package main;

import java.time.LocalDate;

public class Book {
	private int id;
	private String title;
	private String author;
	private String genre;
	private LocalDate lastCheckOut;
	private boolean checkedOut;

	public Book(int id, String title, String author, String genre, LocalDate lastCheckOut, boolean checked) {
		this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.lastCheckOut = lastCheckOut;
        this.checkedOut = checked;
	}

	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return this.author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return this.genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public LocalDate getLastCheckOut() {
		return this.lastCheckOut;
	}
	public void setLastCheckOut(LocalDate lastCheckOut) {
		this.lastCheckOut = lastCheckOut;
	}
	public boolean isCheckedOut() {
		return this.checkedOut;
	}
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}
	
	@Override
	public String toString() {
		/*
		 * This is supposed to follow the format
		 * 
		 * {TITLE} By {AUTHOR}
		 * 
		 * Both the title and author are in uppercase.
		 */
		return this.title.toUpperCase() + " BY " + this.author.toUpperCase();
	}
	public float calculateFees() {
		/*
		 * fee (if applicable) = base fee + 1.5 per additional day
		 * Must return $10(base fee) + $1.5 per additional day
		 */
		LocalDate date = LocalDate.of(2023, 9, 15);
		if(date.toEpochDay() - this.lastCheckOut.toEpochDay()  >= 31){
			double result = 10 + (1.5 * ( date.toEpochDay() - this.lastCheckOut.toEpochDay() - 31));
			return (float) result;
		}
		return 0;
	}
}
