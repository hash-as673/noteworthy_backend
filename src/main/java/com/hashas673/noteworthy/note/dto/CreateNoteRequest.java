package com.hashas673.noteworthy.note.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateNoteRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;


}
