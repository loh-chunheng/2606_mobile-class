package sg.edu.nus.car_rental_chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.nus.car_rental_chatbot.model.ChatRequest;
import sg.edu.nus.car_rental_chatbot.service.ChatService;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Shows the first page. The browser receives HTML rendered by Thymeleaf.
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("chatRequest", new ChatRequest());
        return "index";
    }

    // Receives the form submission from the browser.
    // The controller reads the user input, asks the service for a reply, and passes the result back to Thymeleaf for rendering.
    @PostMapping("/chat")
    public String chat(@ModelAttribute ChatRequest request, Model model) {
        String reply = chatService.getReply(request.getMessage());

        model.addAttribute("chatRequest", new ChatRequest());
        model.addAttribute("userMessage", request.getMessage());
        model.addAttribute("botReply", reply);

        return "index";
    }

    // Clear simply renders a fresh page.
    @PostMapping("/clear")
    public String clear(Model model) {
        model.addAttribute("chatRequest", new ChatRequest());
        return "index";
    }
}
