package by.fin.tgbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import by.fin.tgbot.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}