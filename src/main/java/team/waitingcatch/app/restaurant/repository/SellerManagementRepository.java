package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.waitingcatch.app.restaurant.entity.SellerManagement;

public interface SellerManagementRepository extends JpaRepository<SellerManagement, Long> {

}