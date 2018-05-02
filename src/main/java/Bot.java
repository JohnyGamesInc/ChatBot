import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private int ifCount = 0;

    public void setIfCount(int ifCount) {
        this.ifCount = ifCount;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiException e){e.printStackTrace();}
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e){e.printStackTrace();}
    }

    public void onUpdateReceived(Update update) {//Для приема сообщений через ЛонгПул
        Model model = new Model();
        Message message = update.getMessage();
        //sendMsg(message, "Хочешь узнать погодку? Пройди мини опрос:)");
        label:
        if (message != null && message.hasText() && ifCount == 0){
            switch (message.getText()){
                case "/ДА":
                    sendMsg(message, "Неверный ты даешь ответ! Пробуй дальше отгадать!" + "\n" + "УВИДИШЬ в ясный день крота,\n" +
                            "Парящим В НЕБЕ, правда?");
                    ifCount++;
                    break;
                case "/НЕТ":
                    sendMsg(message, "Верный ты даешь ответ!" + "\n" + "УВИДИШЬ в ясный день крота,\n" +
                            "Парящим В НЕБЕ, правда?");
                    ifCount++;
                    break;
                    default:
                            //sendMsg(message, Weather.getWeather(message.getText(), model));
                            sendMsg(message, "Откройте мне один секрет:\n" +
                                    "Живут жирафы В ТУНДРЕ?");
           }
        } else if (message != null && message.hasText() && ifCount == 1 ){
            switch (message.getText()){
                case "/ДА":
                    sendMsg(message, "И снова твой ответ не верен!");
                    sendMsg(message, "Главное не унывать!" + "\n" + "Хочешь узнать погоду?)");
                    /*ry {
                        message.getText();
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        e.printStackTrace();}*/
                    ifCount++;
                    break;
                case "/НЕТ":
                    sendMsg(message, "Верный ты даешь ответ!" + "\n" + "Хочешь узнать погоду?)");
                    ifCount++;
                    break;
                /*default:
                    //sendMsg(message, Weather.getWeather(message.getText(), model));
                    sendMsg(message, "Да я знаю, что твоя розочка уже давно вскрыта!:)");*/
            }
        } else if (message != null && message.hasText() && ifCount == 2){
            switch (message.getText()){
                case "/ДА":
                    sendMsg(message, "Введи город, чтоб узнать погоду!");
                    message.getText();
                    //ifCount = 0;
                    break;
                case "/НЕТ":
                    sendMsg(message, "Ну и ладно!");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMsg(message, "We come back SOON");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMsg(message, "Откройте мне один секрет:\n" +
                            "Живут жирафы В ТУНДРЕ?");
                    ifCount = 0;
                    break label;
                default:
                       try {
                            sendMsg(message, Weather.getWeather(message.getText(), model));
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendMsg(message, "Введи город, чтоб узнать погоду!");
                        }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMsg(message, "We come back SOON");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMsg(message, "Откройте мне один секрет:\n" +
                            "Живут жирафы В ТУНДРЕ?");
                    ifCount = 0;
                    break label;
            }

        }
    }

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        //keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/ДА"));
        //keyboardFirstRow.add(new KeyboardButton("/setting"));
        keyboardFirstRow.add(new KeyboardButton("/НЕТ"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return "TestJohnBot";
    }

    public String getBotToken() {
        return "514117148:AAFDaZBafY1AWIvxqbdJYXzqGEB6ohk8MPM";
    }
}
