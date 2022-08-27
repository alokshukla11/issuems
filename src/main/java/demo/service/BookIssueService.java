package demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import demo.model.Book;
import demo.model.BookDetails;
import demo.model.BookIssueData;
import demo.repository.BookFiengClient;
import demo.repository.BookIssueDataRepository;
import demo.response.APIResponse;
import demo.response.util.APIResponseUtil;

@Service
public class BookIssueService
{
	@Autowired
    BookFiengClient bookFiengClient;
	
	@Autowired
	BookIssueDataRepository bookIssueDataRepository;
	
	public APIResponse issueBookToCustomer(BookIssueData bookIssueData)
    {
		try {
			if(bookIssueData==null || bookIssueData.getIsbn()==null || bookIssueData.getIsbn().isBlank())
				return APIResponseUtil.badResponse(bookIssueData);
			
			APIResponse getBookApiResponse = getAvailableBookByISBN(bookIssueData.getIsbn());
			if(getBookApiResponse!=null && getBookApiResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)) {
				Book availableBooks = (Book)getBookApiResponse.getData();
				if(availableBooks!=null && availableBooks.getTotalCopies()>0) {
					if(availableBooks.getTotalCopies()==availableBooks.getIssuedCopies()
							||availableBooks.getTotalCopies()<=0) {
						return APIResponseUtil.notFoudResponse("ALL BOOKS ARE ISSUEED, TRY WITH ANOTHER BOOK");
						
					}else if(availableBooks.getTotalCopies()<(availableBooks.getIssuedCopies() + bookIssueData.getNoOfCopies())){
						return APIResponseUtil.notFoudResponse(bookIssueData.getNoOfCopies()+ " COPIES OF BOOK, ISBN:"+bookIssueData.getIsbn()+" ARE NOT AVAILABLE");
					}else {
						availableBooks.setIssuedCopies(availableBooks.getIssuedCopies()+bookIssueData.getNoOfCopies());
					}
					APIResponse updateNumberOfCopiesResponse = bookFiengClient.saveBook(availableBooks);
					if(updateNumberOfCopiesResponse!=null && updateNumberOfCopiesResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)) {
						
						availableBooks.setCustId(bookIssueData.getCustId());
						//save data and return response
						bookIssueDataRepository.save(bookIssueData);
						return APIResponseUtil.successResponse(availableBooks);
					}else
						return updateNumberOfCopiesResponse;
				}else
					APIResponseUtil.notFoudResponse("ALL BOOKS ARE ISSUEED, TRY WITH ANOTHER BOOK");
				
			}else
				return getBookApiResponse;
			
			
		}catch(Exception e) {
			return APIResponseUtil.failResponse(e);
		}
		return APIResponseUtil.failResponse(null);
    }

	public APIResponse returnBook(BookIssueData bookIssueData)
    {
		try {
			if(bookIssueData==null || bookIssueData.getIsbn()==null || bookIssueData.getIsbn().isBlank()
					||bookIssueData.getCustId()<=0 )
				return APIResponseUtil.badResponse(bookIssueData);
			
			APIResponse getBookApiResponse = getAvailableBookByISBN(bookIssueData.getIsbn());
			if(getBookApiResponse!=null && getBookApiResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)) {
				Book availableBooks = (Book)getBookApiResponse.getData();
				if(availableBooks!=null) {
					
					availableBooks.setIssuedCopies(availableBooks.getIssuedCopies()-bookIssueData.getNoOfCopies());
					APIResponse updateNumberOfCopiesResponse = bookFiengClient.saveBook(availableBooks);
					if(updateNumberOfCopiesResponse!=null && updateNumberOfCopiesResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)) {
						
						availableBooks.setCustId(bookIssueData.getCustId());
						//save data and return response
						bookIssueDataRepository.save(bookIssueData);
						return APIResponseUtil.successResponse(availableBooks);
					}else
						return updateNumberOfCopiesResponse;
				}else
					APIResponseUtil.notFoudResponse("BOOK NOT FOUND WITH ISBN:"+bookIssueData.getIsbn());
				
			}else
				return getBookApiResponse;
			
			
		}catch(Exception e) {
			return APIResponseUtil.failResponse(e.getMessage());
		}
		return APIResponseUtil.failResponse(null);
    }
	
	
	public APIResponse getAvailableBookByID(int bookid)
    {
		if(bookid<=0)
			return APIResponseUtil.badResponse(bookid);
        System.out.println("Accessing from Issue-SERVICE ==> " );

        try {
	        APIResponse getBookApiResponse = bookFiengClient.getBook(bookid);
	
	        if(getBookApiResponse!=null 
	        		&&getBookApiResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)
	        		&&getBookApiResponse.getData()!=null
	          ){
	            
	            GsonBuilder gsonBuilder = new GsonBuilder();
	            gsonBuilder.setDateFormat("dd-MM-yyyy");
	            Gson gson = gsonBuilder.create();
	            
	        	String jsonString = gson.toJson(getBookApiResponse.getData(), LinkedHashMap.class);
	        	Book book =  gson.fromJson(jsonString, Book.class);

	        	BookDetails bookDetails = new BookDetails();
	            bookDetails.setBookid(book.getBookid());
	            bookDetails.setAvailableCopies(book.getTotalCopies()-book.getIssuedCopies());	
	            bookDetails.setIsbn(book.getIsbn());
	            return APIResponseUtil.successResponse(bookDetails);
	
	        		
	        }else {
	        	return getBookApiResponse;
	        }
        }catch(Exception e) {
        	return APIResponseUtil.failResponse(e.getMessage());
        }
        
    }
	
	public APIResponse getAvailableBookByISBN(String isbn)
    {
		if(isbn==null || isbn.isBlank())
			return APIResponseUtil.badResponse(isbn);
        System.out.println("Accessing from Issue-SERVICE ==> " );

        try {
	        APIResponse getBookApiResponse = bookFiengClient.findByIsbn(isbn);
	
	        if(getBookApiResponse!=null 
	        		&&getBookApiResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)
	        		&&getBookApiResponse.getData()!=null
	          ){
	            
	            GsonBuilder gsonBuilder = new GsonBuilder();
	            gsonBuilder.setDateFormat("dd-MM-yyyy");
	            Gson gson = gsonBuilder.create();
	            
	        	String jsonString = gson.toJson(getBookApiResponse.getData(), LinkedHashMap.class);
	        	Book book =  gson.fromJson(jsonString, Book.class);
	        	return APIResponseUtil.successResponse(book);
	
	        		
	        }else {
	        	return getBookApiResponse;
	        }
        }catch(Exception e) {
        	return APIResponseUtil.failResponse(e.getMessage());
        }
        
    }
	
	public APIResponse getAllAvailableBooks()
    {
		System.out.println("Accessing from Issue-SERVICE ==> " );

        try {
	        APIResponse getBookApiResponse = bookFiengClient.getAllBooks();
	
	        if(getBookApiResponse!=null 
	        		&&getBookApiResponse.getStatus().equalsIgnoreCase(APIResponseUtil.SUCCESS_STATUS)
	        		&&getBookApiResponse.getData()!=null
	          ){
	            
	            GsonBuilder gsonBuilder = new GsonBuilder();
	            gsonBuilder.setDateFormat("dd-MM-yyyy");
	            Gson gson = gsonBuilder.create();

	            ArrayList<BookDetails> availableBooks = new ArrayList();
	            ArrayList<LinkedHashMap> books = (ArrayList)getBookApiResponse.getData();
	            for(LinkedHashMap book:books) {
	            	String jsonString = gson.toJson(book, LinkedHashMap.class);
		        	Book book_ =  gson.fromJson(jsonString, Book.class);

		        	BookDetails bookDetails = new BookDetails();
		            bookDetails.setBookid(book_.getBookid());
		            bookDetails.setAvailableCopies(book_.getTotalCopies()-book_.getIssuedCopies());
		            bookDetails.setIsbn(book_.getIsbn());
		            availableBooks.add(bookDetails);
	            }
	        	
	            return APIResponseUtil.successResponse(availableBooks);
	
	        		
	        }else {
	        	return getBookApiResponse;
	        }
        }catch(Exception e) {
        	return APIResponseUtil.failResponse(e.getMessage());
        }
        
    }

	
	
}
