package team.waitingcatch.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.user.entitiy.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndDeletedFalse(String email);

	Optional<User> findByUsernameAndDeletedFalse(String username);

	Optional<User> findByUsernameAndEmailAndDeletedFalse(String username, String email);

	boolean existsByUsername(String username);
}
