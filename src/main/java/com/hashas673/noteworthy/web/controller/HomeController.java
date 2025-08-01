package com.hashas673.noteworthy.web.controller;


import com.hashas673.noteworthy.auth.model.User;
import com.hashas673.noteworthy.auth.service.AuthService;
import com.hashas673.noteworthy.note.dto.CreateNoteRequest;
import com.hashas673.noteworthy.note.dto.NoteResponse;
import com.hashas673.noteworthy.note.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final NoteService noteService;
    private final AuthService authService;

    public HomeController(NoteService noteService, AuthService authService) {
        this.noteService = noteService;
        this.authService = authService;
    }

    @GetMapping({"/", "/home"})
    public String showNotes(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "index";
        }

        User user = authService.getUserByUsername(authentication.getName());
        List<NoteResponse> notes = noteService.getUserNotes(user);

        model.addAttribute("notes", notes);
        model.addAttribute("createNoteRequest", new CreateNoteRequest());

        return "home";
    }
}
