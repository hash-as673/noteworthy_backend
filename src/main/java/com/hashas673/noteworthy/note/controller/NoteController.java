package com.hashas673.noteworthy.note.controller;

import com.hashas673.noteworthy.auth.model.User;
import com.hashas673.noteworthy.auth.service.AuthService;
import com.hashas673.noteworthy.note.dto.CreateNoteRequest;
import com.hashas673.noteworthy.note.dto.NoteResponse;
import com.hashas673.noteworthy.note.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final AuthService authService;

    public NoteController(NoteService noteService, AuthService authService) {
        this.noteService = noteService;
        this.authService = authService;
    }

    // Create new note
    @PostMapping
    public ResponseEntity<String> createNote(@Valid @RequestBody CreateNoteRequest request,
                                             Authentication authentication) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        noteService.createNote(user, request);
        return ResponseEntity.ok("Note created successfully");
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getUserNotes(Authentication authentication) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        List<NoteResponse> notes = noteService.getUserNotes(user);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id,
                                             Authentication authentication) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        noteService.deleteNote(id, user);
        return ResponseEntity.ok("Note deleted");
    }


}
