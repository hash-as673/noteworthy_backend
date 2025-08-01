package com.hashas673.noteworthy.note.service;

import com.hashas673.noteworthy.auth.model.User;
import com.hashas673.noteworthy.common.encryption.EncryptionService;
import com.hashas673.noteworthy.note.dto.CreateNoteRequest;
import com.hashas673.noteworthy.note.dto.NoteResponse;
import com.hashas673.noteworthy.note.model.Note;
import com.hashas673.noteworthy.note.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final EncryptionService encryptionService;

    public NoteService(NoteRepository noteRepository, EncryptionService encryptionService) {
        this.noteRepository = noteRepository;
        this.encryptionService = encryptionService;
    }


    public void createNote(User user, CreateNoteRequest request) {
        String encrypted = encryptionService.encrypt(request.getContent());

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setEncryptedContent(encrypted);
        note.setOwner(user);

        noteRepository.save(note);
    }

    public List<NoteResponse> getUserNotes(User user) {
        return noteRepository.findAllByOwner(user).stream()
                .map(note -> new NoteResponse(
                        note.getId(),
                        note.getTitle(),
                        encryptionService.decrypt(note.getEncryptedContent())))
                .collect(Collectors.toList());
    }

    public void deleteNote(Long noteId, User user) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getOwner().getId().equals(user.getId()))
            throw new RuntimeException("Unauthorized access");

        noteRepository.delete(note);
    }

    public void updateNote(Long noteId, User user, CreateNoteRequest request) {
        Note note = noteRepository.findByIdAndOwner(noteId, user)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setTitle(request.getTitle());
        note.setEncryptedContent(encryptionService.encrypt(request.getContent()));
        noteRepository.save(note);
    }

    public NoteResponse getNoteByIdAndUser(Long id, User user) {
        Note note = noteRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new RuntimeException("Note not found or access denied"));

        String decryptedContent = encryptionService.decrypt(note.getEncryptedContent());

        return new NoteResponse(note.getId(), note.getTitle(), decryptedContent);
    }

}
