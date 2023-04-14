package com.example.pastebin.controller;

import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.repository.PasteRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class PasteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasteRepository pasteRepository;
    private JSONObject jsonObject;

    private JSONObject getJson() throws JSONException {
        jsonObject = new JSONObject();
        jsonObject.put("name", "имя");
        jsonObject.put("text", "текст");
        return jsonObject;
    }

    @Test
    void addPaste() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("timeRange", TimeRange.ONE_DAY.toString())
                        .param("access", Access.PUBLIC.toString())
                        .content(getJson().toString())).andExpect(status().isOk())
                .andExpect(jsonPath("$").isString()).andReturn();

        mockMvc.perform(get("/get-last10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(getJson().get("name")))
                .andExpect(jsonPath("$[0].text").value(getJson().get("text")));
    }

    @Test
    void getLast10Pastes() throws Exception {
        mockMvc.perform(get("/get-last10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPasteByNameOrText() {
        mockMvc.perform(get("/api/faculty/students?facultyId=" + faculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPasteByLink() {
    }
}