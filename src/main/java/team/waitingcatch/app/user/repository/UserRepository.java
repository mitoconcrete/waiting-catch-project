package team.waitingcatch.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.user.entitiy.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u.username from User u where u.id = :userId")
	String findUsernameById(@Param("userId") Long id);

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	Optional<User> findByUsernameAndIsDeletedFalse(String username);

	Optional<User> findByUsernameAndEmailAndIsDeletedFalse(String username, String email);

	boolean existsByUsername(String username);
}