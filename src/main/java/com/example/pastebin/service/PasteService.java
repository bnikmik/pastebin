package com.example.pastebin.service;

import com.example.pastebin.dto.PasteDTO;
import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.exception.BadParamException;
import com.example.pastebin.exception.NotFoundException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
import com.example.pastebin.repository.specification.PasteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasteService {
    private final PasteRepository pasteRepository;

    @Autowired
    public PasteService(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    public String addPaste(PasteDTO pasteDTO, TimeRange timeRange, Access access) {
        Paste paste = pasteDTO.toModel();
        paste.setAccess(access);
        if (!timeRange.equals(TimeRange.NO_TIME_LIMIT))
            paste.setExpiredDate(Instant.now().plus(timeRange.getTime(), ChronoUnit.MINUTES));
        else paste.setExpiredDate(null);
        pasteRepository.save(paste);
        return encode(paste.getId().toString() + " " + paste.getName().trim());
    }

    public List<PasteDTO> getLast10Pastes() {
        return pasteRepository.getLast10Pastes().stream().map(PasteDTO::fromProjection).collect(Collectors.toList());
    }

    private String encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    private String decode(String string) {
        String tmp = new String(java.util.Base64.getDecoder().decode(string));
        String [] arr = tmp.split(" ", 2);
        return arr[0];
    }

    public List<PasteDTO> getPasteByNameOrText(String name, String text) {
        if ((name == null || name.isBlank()) && (text == null || text.isBlank())) throw new BadParamException();
        return pasteRepository.findAll(PasteSpecification.byName(name)
                .and(PasteSpecification.byText(text))).stream().map(PasteDTO::fromModel).collect(Collectors.toList());
    }

    public PasteDTO getPasteByLink(String hash) {
        long tmp;
        try {
            tmp = Long.parseLong(decode(hash));
        } catch (IllegalArgumentException e) {
            throw new BadParamException();
        }
        return PasteDTO.fromModel(pasteRepository.findById(tmp).orElseThrow(NotFoundException::new));
    }
}
