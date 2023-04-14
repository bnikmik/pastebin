package com.example.pastebin.controller;

import com.example.pastebin.dto.PasteDTO;
import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.service.PasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasteController {
    private PasteService pasteService;

    @Autowired
    public PasteController(PasteService pasteService) {
        this.pasteService = pasteService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPaste(@RequestBody PasteDTO pasteDTO, @RequestParam TimeRange timeRange, Access access) {
        return ResponseEntity.ok(WebMvcLinkBuilder.linkTo(PasteController.class).slash(pasteService.addPaste(pasteDTO, timeRange, access)).withSelfRel().getHref());
    }

    @GetMapping("/get-last10")
    public ResponseEntity<?> getLast10Pastes() {
        return ResponseEntity.ok(pasteService.getLast10Pastes());
    }

    @GetMapping("/get-paste")
    public ResponseEntity<?> getPasteByNameOrText(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String text) {
        return ResponseEntity.ok(pasteService.getPasteByNameOrText(name, text));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<?> getPasteByLink(@PathVariable String hash) {
        return ResponseEntity.ok(pasteService.getPasteByLink(hash));
    }
}
