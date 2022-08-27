package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.model.Book;
import demo.model.BookIssueData;
import demo.repository.BookFiengClient;
import demo.response.APIResponse;
import demo.response.util.APIResponseUtil;
import demo.service.BookIssueService;

@RestController
public class IssueController
{
    @Autowired
    BookFiengClient bookFiengClient;
    @Autowired
    BookIssueService bookIssueService;

    @GetMapping("/book/{bookid}")
    private APIResponse getAvailableBookByID(@PathVariable("bookid") int bookid)
    {
    	//call the book service to get the available book details
        return bookIssueService.getAvailableBookByID(bookid);
    }
    
    @GetMapping("/book")
    private APIResponse getAllAvailableBooks()
    {
    	//call the book service to get the available book details
        return bookIssueService.getAllAvailableBooks();
    }

    @PostMapping("/book")
    private APIResponse issueBookToCustomer(@RequestBody BookIssueData bookIssueData)
    {
    	return bookIssueService.issueBookToCustomer(bookIssueData);
    }

    @PostMapping("/book/return")
    private APIResponse returnBook(@RequestBody BookIssueData bookIssueData)
    {        
    	return bookIssueService.returnBook(bookIssueData);
    }


}
