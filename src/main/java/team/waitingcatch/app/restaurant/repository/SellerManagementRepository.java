package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.SellerManagement;

public interface SellerManagementRepository extends JpaRepository<SellerManagement, Long> {

	SellerManagement findTopByUsernameAndEmailOrderByCreatedDateDesc(String requestSellerName, String email);

	Page<SellerManagement> findByUsernameContaining(String searchVal, Pageable pageable);
}