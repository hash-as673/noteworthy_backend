package com.hashas673.noteworthy.note.repository;

import com.hashas673.noteworthy.auth.model.User;
import com.hashas673.noteworthy.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOwner(User owner);

    Optional<Note> findByIdAndOwner(Long id, User owner);
}
