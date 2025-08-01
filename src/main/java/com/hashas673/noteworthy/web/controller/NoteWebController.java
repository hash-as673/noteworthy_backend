package com.hashas673.noteworthy.web.controller;

import com.hashas673.noteworthy.auth.model.User;
import com.hashas673.noteworthy.auth.service.AuthService;
import com.hashas673.noteworthy.note.dto.CreateNoteRequest;
import com.hashas673.noteworthy.note.dto.NoteResponse;
import com.hashas673.noteworthy.note.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteWebController {

    private final NoteService noteService;
    private final AuthService authService;

    public NoteWebController(NoteService noteService, AuthService authService) {
        this.noteService = noteService;
        this.authService = authService;
    }

    // Main view — loads notes for authenticated user
    @GetMapping
    public String showNotes(Model model, Authentication authentication) {
        User user = authService.getUserByUsername(authentication.getName());
        List<NoteResponse> notes = noteService.getUserNotes(user);

        model.addAttribute("notes", notes);
        model.addAttribute("createNoteRequest", new CreateNoteRequest());

        return "home";
    }

    // Add note
    @PostMapping
    public String addNote(@ModelAttribute("createNoteRequest") @Valid CreateNoteRequest request,
                          BindingResult result,
                          Authentication authentication,
                          Model model) {
        User user = authService.getUserByUsername(authentication.getName());

        if (result.hasErrors()) {
            model.addAttribute("notes", noteService.getUserNotes(user));
            return "home";
        }

        noteService.createNote(user, request);
        return "redirect:/notes";
    }

    // Delete note
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByUsername(authentication.getName());
        noteService.deleteNote(id, user);
        return "redirect:/notes";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable Long id,
                               Authentication authentication,
                               Model model) {
        User user = authService.getUserByUsername(authentication.getName());
        NoteResponse note = noteService.getNoteByIdAndUser(id, user);

        CreateNoteRequest editRequest = new CreateNoteRequest();
        editRequest.setTitle(note.getTitle());
        editRequest.setContent(note.getContent());

        model.addAttribute("noteId", id);
        model.addAttribute("editNoteRequest", editRequest);
        return "edit-note";
    }

    // Submit edit form
    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable Long id,
                             @ModelAttribute("editNoteRequest") @Valid CreateNoteRequest request,
                             BindingResult result,
                             Authentication authentication) {
        if (result.hasErrors()) {
            return "edit-note";
        }

        User user = authService.getUserByUsername(authentication.getName());
        noteService.updateNote(id, user, request);
        return "redirect:/notes";
    }
}
