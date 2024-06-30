package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    // ---------------- File controller------------------------
    @GetMapping("/file/list")
    public ModelAndView showFilePage(){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("files", fileService.getAllFiles());
        modelAndView.addObject("notes", noteService.getAllNotes());
        modelAndView.addObject("credentials", credentialService.getAllCredentials());
        modelAndView.addObject("encryptionService", encryptionService);
        return modelAndView;
    }

    @PostMapping("/file/createFile")
    public ModelAndView createFile(@RequestParam("fileUpload") MultipartFile file) throws IOException, SQLException {
        ModelAndView modelAndView = new ModelAndView("home");
        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            long fileSize = file.getSize();
            byte[] fileData = file.getBytes();
            if (fileData.length  >= 1048576){
                modelAndView.addObject("error", "maximum permitted size of 1048576 bytes");
                modelAndView.addObject("files", fileService.getAllFiles());
                modelAndView.addObject("notes", noteService.getAllNotes());
                modelAndView.addObject("credentials", credentialService.getAllCredentials());
                modelAndView.addObject("encryptionService", encryptionService);
                return modelAndView;
            }
            User user = userService.getCurrentUser();
            int userId = user.getUserId();
            if (fileService.checkFileNameExist(fileName)){
                modelAndView.addObject("error", "File already exists");
            } else {
                fileService.insertFile(new File(fileName, contentType, fileSize, fileData, userId));
            }
        }
        modelAndView.addObject("files", fileService.getAllFiles());
        modelAndView.addObject("notes", noteService.getAllNotes());
        modelAndView.addObject("credentials", credentialService.getAllCredentials());
        modelAndView.addObject("encryptionService", encryptionService);
        return modelAndView;
    }

    @GetMapping("/file/deleteFile/{id}")
    public ModelAndView deleteFile(@PathVariable int id){
        fileService.deleteFile(id);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("files", fileService.getAllFiles());
        modelAndView.addObject("notes", noteService.getAllNotes());
        modelAndView.addObject("credentials", credentialService.getAllCredentials());
        modelAndView.addObject("encryptionService", encryptionService);
        return modelAndView;
    }

    @GetMapping("/file/view/{id}")
    public ModelAndView viewFile(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView("detailFile");
        modelAndView.addObject("file", fileService.getFileById(id));
        return modelAndView;
    }

    @GetMapping("/file/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id){
        File file = fileService.getFileById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    // ---------------- Note controller------------------------
    @GetMapping("/note/list")
    public ModelAndView showNotePage(){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("notes", noteService.getAllNotes());
        modelAndView.addObject("credentials", credentialService.getAllCredentials());
        modelAndView.addObject("files", fileService.getAllFiles());
        modelAndView.addObject("encryptionService", encryptionService);
        modelAndView.addObject("activeTab", "notes");
        return modelAndView;
    }

    @PostMapping("/note/createNote")
    public String createNote(@RequestParam int noteId, @RequestParam String noteTitle, @RequestParam String noteDescription){
        Note note = null;
        if (noteService.existNote(noteId)){
            note = new Note(noteId, noteTitle, noteDescription, userService.getCurrentUser().getUserId());
            noteService.updateNote(note);
        }else {
            note = new Note(noteTitle, noteDescription, userService.getCurrentUser().getUserId());
            noteService.insertNote(note);
        }
        return "redirect:/home/note/list?activeTab=notes";
    }

    @GetMapping("/note/deleteNote/{id}")
    public String deleteNote(@PathVariable int id){
        noteService.deleteNote(id);
        return "redirect:/home/note/list?activeTab=notes";
    }

    // ---------------- Credential controller------------------------
    @GetMapping("/credential/list")
    public ModelAndView showCredentialPage(){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("credentials", credentialService.getAllCredentials());
        modelAndView.addObject("notes", noteService.getAllNotes());
        modelAndView.addObject("files", fileService.getAllFiles());
        modelAndView.addObject("encryptionService", encryptionService);
        modelAndView.addObject("activeTab", "credentials");
        return modelAndView;
    }

    @PostMapping("/credential/createCredential")
    public String createCredential(@RequestParam int credentialId, @RequestParam String url, @RequestParam String username, @RequestParam String password){
        String key = credentialService.createKey();
        String encryptPassword = encryptionService.encryptValue(password, key);
        User user = userService.getCurrentUser();
        int userId = user.getUserId();
        Credential credential = null;
        if (credentialService.existCredential(credentialId)){
            credential = new Credential(credentialId, url, username, key, encryptPassword, userId);
            credentialService.updateCredential(credential);
        } else {
            credential = new Credential(url, username, key, encryptPassword, userId);
            credentialService.insertCredential(credential);
        }


        return "redirect:/home/credential/list?activeTab=credentials";
    }

    @GetMapping("/credential/deleteCredential/{id}")
    public String deleteCredential(@PathVariable int id){
        credentialService.deleteCredential(id);
        return "redirect:/home/credential/list?activeTab=credentials";
    }
}
