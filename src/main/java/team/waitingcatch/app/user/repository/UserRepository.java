package team.waitingcatch.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.user.entitiy.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndIsDeletedFalse(String email);

	Optional<User> findByUsernameAndIsDeletedFalse(String username);

	Optional<User> findByUsernameAndEmailAndIsDeletedFalse(String username, String email);

	boolean existsByUsername(String username);
}
