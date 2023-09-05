package by.fin.tgbot.service;

import by.fin.tgbot.repository.IncomeRepository;
import by.fin.tgbot.repository.SpendRepository;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinanceServiceTest {

    @InjectMocks
    private FinanceService financeService;

    @Mock
    private SpendRepository spendRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @BeforeAll
    public void beforeAll() {
        System.out.println(System.currentTimeMillis());
    }

    @AfterAll
    public void afterAll() {
        System.out.println(System.currentTimeMillis());
    }

    @DisplayName("ADD_INCOME_test")
    @Test
    void addFinanceOperationAddIncomeTest() {
        String price = "150.0";
        String message = financeService.addFinanceOperation("/addincome", price, 500L);
        Assert.assertEquals("Доход в размере " + price + " был успешно добавлен", message);
    }

    @DisplayName("non_ADD_INCOME_test")
    @Test
    void addFinanceOperationNonAddIncomeTest() {
        String price = "200";
        String message = financeService.addFinanceOperation("/nan", price, 250L);
        Assert.assertEquals("Расход в размере " + price + " был успешно добавлен", message);
    }
}