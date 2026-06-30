package sg.edu.nus.car_rental_chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.car_rental_chatbot.model.ChatResponse;
import sg.edu.nus.car_rental_chatbot.service.ChatService;

@RestController
@RequestMapping("/api")
public class ChatRestController {

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    // @RestController returns JSON data
    // Example browser URL: /api/chat?message=hello
    @GetMapping("/chat")
    public ChatResponse chat(@RequestParam(name = "message", required = false) String message) {
        String reply = chatService.getReply(message);
        return new ChatResponse(reply);
    }
}
