package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    // BEGIN

    //create
    @Test
    public void testCreate() throws Exception {
        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));
        taskRepository.save(testTask);
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    //show
    @Test
    public void testShow() throws Exception {
        var task = taskRepository.findById(testTask.getId());
        if (task.isPresent()) {
            mockMvc.perform(get("/tasks/{id}", task.get().getId()))
                    .andExpect(status().isOk());
        }
    }

    //update
    @Test
    public void testUpdate() throws Exception {
        var task = taskRepository.findById(testTask.getId());
        if (task.isPresent()) {
            var data = new HashMap<>();
            data.put("title", "123");

            var request = put("/tasks/{id}", task.get().getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(data));

            mockMvc.perform(request)
                    .andExpect(status().isOk());

            assertThat(task.get().getTitle()).isEqualTo(("123"));
        }

    }

    //delete
    @Test
    public void testDelete() throws Exception {
        System.out.println("123213123123");
        System.out.println(taskRepository.findAll());
        mockMvc.perform(delete("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk());
        taskRepository.deleteById(testTask.getId());

        testTask = new Task();
        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }

    // END
}
