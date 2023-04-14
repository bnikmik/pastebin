package com.example.pastebin.service;

import com.example.pastebin.dto.PasteDTO;
import com.example.pastebin.exception.BadParamException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
import com.example.pastebin.repository.projection.PasteProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasteServiceTest {
    @Mock
    private PasteRepository pasteRepository;
    @InjectMocks
    private PasteService pasteService;
    private PasteDTO pasteDTO;
    private Paste paste;

    @BeforeEach
    void setUp() {
        pasteDTO = new PasteDTO();
        pasteDTO.setName("name");
        pasteDTO.setText("text");
        paste = pasteDTO.toModel();
    }

    @Test
    void testAddPaste() {
        pasteRepository.save(paste);
        verify(pasteRepository, only()).save(paste);
    }

    @Test
    void testMappingDtoWhenNameNull() {
        pasteDTO.setName(null);
        assertThrows(BadParamException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenTextNull() {
        pasteDTO.setText(null);
        assertThrows(BadParamException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenNameIsBlank() {
        pasteDTO.setName(" ");
        assertThrows(BadParamException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenTextIsBlank() {
        pasteDTO.setText(" ");
        assertThrows(BadParamException.class, () -> pasteDTO.toModel());
    }

    @Test
    void getLast10Pastes() {
        PasteProjection projection = new PasteProjection() {
            @Override
            public String getName() {
                return "name";
            }

            @Override
            public String getText() {
                return "text";
            }
        };
        List<PasteProjection> list = new ArrayList<>();
        list.add(projection);
        when(pasteRepository.getLast10Pastes()).thenReturn(list);
        int size = pasteService.getLast10Pastes().size();
        assertEquals(1, size);
    }

    @Test
    public void getPasteByNameOrText_throwsBadParamException_whenNameAndTextAreNull() {
        assertThrows(BadParamException.class, () -> pasteService.getPasteByNameOrText(null, null));
        assertThrows(BadParamException.class, () -> pasteService.getPasteByNameOrText("", ""));
    }


    @Test
    public void getPasteByNameOrText() {
        List<Paste> pasteList = new ArrayList<>();
        pasteList.add(paste);
        when(pasteRepository.findAll(any(Specification.class))).thenReturn(pasteList);
        List<PasteDTO> pasteDTOList = pasteService.getPasteByNameOrText("Test", "test");
        assertEquals(1, pasteDTOList.size());
        verify(pasteRepository).findAll(any(Specification.class));
    }

    @Test
    void testGetPasteByLink() {
        String hash = "Mjkgc3RyaW5n";
        when(pasteRepository.findById(anyLong())).thenReturn(Optional.of(paste));
        PasteDTO result = pasteService.getPasteByLink(hash);
        assertNotNull(result);
        assertEquals(paste.getName(), result.getName());
    }

    @Test
    void testGetPasteByLinkThrowException() {
        String hash = "1321";
        assertThrows(BadParamException.class, () -> pasteService.getPasteByLink(hash));
    }
}