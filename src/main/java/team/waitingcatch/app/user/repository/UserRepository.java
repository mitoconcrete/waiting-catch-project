package team.waitingcatch.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.user.entitiy.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
