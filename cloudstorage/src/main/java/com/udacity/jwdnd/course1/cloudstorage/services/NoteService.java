package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes() {
        return noteMapper.getAllNotes();
    }

    public void insertNote(Note note){
        noteMapper.insertNote(note);
    }

    public int deleteNote(int noteId){
        return noteMapper.deleteNote(noteId);
    }

    public int updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public boolean existNote(int noteId){
        for(Note note : getAllNotes()){
            if(note.getNoteId() == noteId){
                return true;
            }
        }
        return false;
    }
}
