package by.fin.tgbot.repository;

import by.fin.tgbot.entity.Spend;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
public class SpendRepositoryTest {

    @Autowired
    private SpendRepository spendRepository;

    @Test
    public void testSpendInit() {
        Optional<Spend> spend = spendRepository.findById(11L);
        assertTrue(spend.isPresent());
        assertEquals(new BigDecimal("3333"), spend.get().getSpend());

    }

}
