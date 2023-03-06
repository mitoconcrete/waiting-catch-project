package team.waitingcatch.app.restaurant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

public interface SellerManagementRepository extends JpaRepository<SellerManagement, Long> {
	SellerManagement findTopByUsernameAndEmailOrderByCreatedDateDesc(String requestSellerName, String email);

	@Query(value = "select bd from SellerManagement bd where bd.status = :status")
	List<SellerManagement> findAllByStatus(@Param("status") AcceptedStatusEnum status);

	Page<SellerManagement> findByUsernameContaining(String searchVal, Pageable pageable);
}