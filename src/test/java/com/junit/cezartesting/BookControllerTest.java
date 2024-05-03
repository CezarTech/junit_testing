package com.junit.cezartesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
  private MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();
  ObjectWriter objectWriter = new ObjectMapper().writer();

  @Mock
    private BookRepository bookRepository;
  @InjectMocks
    private BookController bookController;

    Book record_1=new Book(1L,"Cezar book 1","Hello world",1);
    Book record_2=new Book(2L,"Cezar book 2","Hello world 2",2);
    Book record_3=new Book(3L,"Cezar book 3","Hello world 3",3);

    @BeforeEach
   public void setUp() {
        this.mockMvc= MockMvcBuilders.standaloneSetup(bookController).build();

    }

    @Test
    public void getAllBooks() throws Exception {
        List<Book> books=new ArrayList<>(Arrays.asList(record_1,record_2,record_3));
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].name",is("Cezar book 3"))
        );
    }

    @Test
    public void getBookById() throws Exception {
        Mockito.when(bookRepository.findById(record_1.getBookId())).thenReturn(java.util.Optional.of(record_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is("Cezar book 1"))
                );

    }

    @Test
    public void createBook() throws Exception {
        Book record= Book.builder()
                .bookId(4L)
                .name("Cezar Creating in Test")
                .summary("Hello world 4")
                .rating(10)
                .build();
        Mockito.when(bookRepository.save(record)).thenReturn(record);
        String content =objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is("Cezar Creating in Test")));

    }

    @Test
    public void updateBook() throws Exception {
        Book updateBook=Book.builder()
                .bookId(1L)
                .name("Cezar Update Book Name")
                .summary("Hello new World")
                .rating(11).build();
        Mockito.when(bookRepository.findById(updateBook.getBookId())).thenReturn(java.util.Optional.of(updateBook));
        Mockito.when(bookRepository.save(updateBook)).thenReturn(updateBook);
        String updatedContent =objectWriter.writeValueAsString(updateBook);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is("Cezar Update Book Name")));


    }

    @Test
    public void deleteBook() throws Exception {
        Mockito.when(bookRepository.findById(record_2.getBookId())).thenReturn(java.util.Optional.of(record_2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/2")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}