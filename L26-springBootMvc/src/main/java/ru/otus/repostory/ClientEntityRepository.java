package ru.otus.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.ClientEntity;
import java.util.List;

public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {

  List<ClientEntity> findByName(String name);
}
