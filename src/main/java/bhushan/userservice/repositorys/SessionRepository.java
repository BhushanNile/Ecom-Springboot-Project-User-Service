package bhushan.userservice.repositorys;

import bhushan.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
   Optional<Session> findByToken(String token);
    Session save(Session session);
    Optional<Session> findByTokenAndUserId(String Token, Long Id);
}
