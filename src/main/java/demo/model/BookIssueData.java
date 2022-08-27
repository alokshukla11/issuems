package demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table
@Data
public class BookIssueData
{

	@Id
	@Column
	@GeneratedValue
	private int bookIssueId;
	@Column
	private String isbn;
	@Column
	private int custId;
	@Column
	private int noOfCopies;
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public int getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public int getBookIssueId() {
		return bookIssueId;
	}
	public void setBookIssueId(int bookIssueId) {
		this.bookIssueId = bookIssueId;
	}
	

}
