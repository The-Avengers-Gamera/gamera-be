package com.avengers.gamera.contoller;

import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.repository.GenreRepository;
import com.avengers.gamera.service.GenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GenreControlTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    GenreService genreService;

    @Autowired
    GenreRepository genreRepository;

    private GenrePostDto genrePostDto;
    private Long id;
    private GenreUpdateDto genreUpdateDto;

    @BeforeEach
    void cleanup() {
        genreRepository.deleteAll();
        genreRepository.flush();

        genrePostDto = GenrePostDto.builder().name("Ch").build();
        id = genreService.createGenre(GenrePostDto.builder().name("NZ").build()).getId();
        genreUpdateDto = GenreUpdateDto.builder().name("Au").build();
    }

    @Test
    void shouldReturn201WhenCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genrePostDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ch"));

    }

    @Test
    void shouldReturn200AndGenreGetDtoWhenGetGenreDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/genres/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NZ"));
    }

    @Test
    void shouldReturnUpdatedGenreGetDtoWhenUpdateDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/genres/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Au"));

    }

}
