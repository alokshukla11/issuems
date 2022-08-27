package demo.repository;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import demo.model.Book;
import demo.response.APIResponse;

@FeignClient(name="BOOK-SERVICE")
@Service
public interface BookFiengClient {

	@GetMapping("/book")
	public APIResponse getAllBooks();

    @GetMapping("/book/{bookid}")
    public APIResponse getBook(@PathVariable("bookid") int bookid);
    
    @GetMapping("/book/isbn/{isbn}")
    public APIResponse findByIsbn(@PathVariable("isbn") String isbn);

    @PostMapping("/book")
    public APIResponse saveBook(@RequestBody Book book);

}