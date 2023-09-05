package by.fin.tgbot.controllers;

import by.fin.tgbot.dto.ValuteCursOnDate;
import by.fin.tgbot.service.StatsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import by.fin.tgbot.service.CentralRussianBankService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CentralRussianBankService centralRussianBankService;
    private final StatsService statsService;

//    @PostMapping("/getCurrencies")
    @GetMapping("/getCurrencies")
    @ApiOperation(value = "Get all change currencies on current day")
//    @RequestMapping("/getCurrencies")
    public List<ValuteCursOnDate> getValuteCursOnDate() throws Exception {
        return centralRussianBankService.getCurrenciesFromCbr();
    }

    @GetMapping("/getStats")
//    @RequestMapping("/getCurrencies")
    @ApiOperation(value = "Count number of operations greater than some amount")
    public int GetStatsAboutIncomesGreaterThan(@RequestParam(value = "amount")BigDecimal amount) {
        return statsService.getCountOfIncomesThatGreater(amount);
    }
}
