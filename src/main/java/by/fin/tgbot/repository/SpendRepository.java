package by.fin.tgbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import by.fin.tgbot.entity.Spend;

@Repository
public interface SpendRepository extends JpaRepository<Spend, Long> {
}
