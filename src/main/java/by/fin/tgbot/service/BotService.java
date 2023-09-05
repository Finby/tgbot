package by.fin.tgbot.service;

import by.fin.tgbot.dto.ValuteCursOnDate;
import by.fin.tgbot.entity.ActiveChat;
import by.fin.tgbot.repository.ActiveChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service //Данный класс является сервисом
@Slf4j //Подключаем логирование из Lombok'a
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

//    private static final Logger log = LoggerFactory.getLogger(BotService.class);
    private final CentralRussianBankService centralBankRussianService;
    private final ActiveChatRepository activeChatRepository;
    private final FinanceService financeService;

    private static final String ADD_INCOME = "/addincome";
    private static final String ADD_SPEND = "/addspend";
    private static final String CURRENT_RATES = "/currentrates";

    private Map<Long, List<String>> previousCommands = new ConcurrentHashMap<>();

    @Value("${bot.api.key}")
    private String apiKey; // Bit API_KEY from BotFather

    @Value("${bot.name}")
    private String name; // Bot name, how you registered it in BotFather

    //Это основной метод, который связан с обработкой сообщений
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        try {
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));

            if (CURRENT_RATES.equalsIgnoreCase(message.getText())) {
                for (ValuteCursOnDate valuteCursOnDate : centralBankRussianService.getCurrenciesFromCbr()) {
                    response.setText(StringUtils.defaultIfBlank(response.getText(), "") + valuteCursOnDate.getName() + " - " + valuteCursOnDate.getCourse() + "\n");
                }
            } else if (ADD_INCOME.equalsIgnoreCase(message.getText())) {
                response.setText("Send me the sum of received income");
            } else if (ADD_SPEND.equalsIgnoreCase(message.getText())) {
                response.setText("Send me sum of spend");
            } else {
                response.setText(financeService.addFinanceOperation(getPreviousCommand(message.getChatId()), message.getText(), message.getChatId()));
            }

            putPreviousCommand(message.getChatId(), message.getText());
            execute(response);

            // add chatId to db if it doesn't exist
            if (activeChatRepository.findActiveChatByChatId(chatId).isEmpty()) {
                ActiveChat activeChat = new ActiveChat();
                activeChat.setChatId(chatId);
                activeChatRepository.save(activeChat);
            }

        } catch (TelegramApiException e) {
            log.error("Возникла проблема при получении данных от сервисов ЦБ РФ", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotificationToAllActiveChats(String message, Set<Long> chatIds) {
        for (Long id: chatIds) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(id));
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void putPreviousCommand(Long chatId, String command) {
        if (previousCommands.get(chatId) == null) {
            List<String> commands = new ArrayList<>();
            commands.add(command);
            previousCommands.put(chatId, commands);
        } else {
            previousCommands.get(chatId).add(command);
        }
    }

    private String getPreviousCommand(Long chatId) {
        return previousCommands.get(chatId)
                .get(previousCommands.get(chatId).size() - 1);
    }

    //Данный метод будет вызван сразу после того, как данный бин будет создан - это обеспечено аннотацией Spring PostConstruct
    @PostConstruct
    public void start() {
        log.info("username: {}, token: {}", name, apiKey);
    }

    //Данный метод просто возвращает данные о имени бота и его необходимо переопределять
    @Override
    public String getBotUsername() {
        return name;
    }

    //Данный метод возвращает API ключ для взаимодействия с Telegram
    @Override
    public String getBotToken() {
        return apiKey;
    }
}
