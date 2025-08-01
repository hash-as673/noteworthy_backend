package com.hashas673.noteworthy.note.dto;

import lombok.Data;

@Data
public class NoteResponse {
    private Long id;
    private String title;
    private String content; // Decrypted content

    public NoteResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

 }