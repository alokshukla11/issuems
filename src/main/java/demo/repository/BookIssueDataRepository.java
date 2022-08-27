package demo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import demo.model.BookIssueData;


public interface BookIssueDataRepository extends CrudRepository<BookIssueData, Integer>
{
	@Modifying
	@Query("delete from BookIssueData f where f.custId=:custId or f.isbn=:isbn")
	void deleteBookIssueDataForCustomer(@Param("custId") int custId, @Param("isbn") String isbn);
}
