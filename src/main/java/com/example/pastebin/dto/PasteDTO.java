package com.example.pastebin.dto;

import com.example.pastebin.exception.BadParamException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.projection.PasteProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasteDTO {
    private String name;
    private String text;

    public Paste toModel() {
        Paste paste = new Paste();
        if (name == null || name.isBlank()) throw new BadParamException();
        else paste.setName(name);
        if (text == null || text.isBlank()) throw new BadParamException();
        else paste.setText(text);
        return paste;
    }

    public static PasteDTO fromProjection(PasteProjection projection) {
        PasteDTO pasteDTO = new PasteDTO();
        pasteDTO.setName(projection.getName());
        pasteDTO.setText(projection.getText());
        return pasteDTO;
    }

    public static PasteDTO fromModel(Paste paste) {
        PasteDTO pasteDTO = new PasteDTO();
        pasteDTO.setName(paste.getName());
        pasteDTO.setText(paste.getText());
        return pasteDTO;
    }
}

