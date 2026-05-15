package com.tasktrackergraphql.project.api;

import com.tasktrackergraphql.config.JWTService;
import com.tasktrackergraphql.project.enums.ProjectStatus;
import com.tasktrackergraphql.project.model.ProjectEntity;
import com.tasktrackergraphql.project.repository.ProjectRepository;
import com.tasktrackergraphql.user.model.UserEntity;
import com.tasktrackergraphql.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
public class ProjectGraphQLControllerTest {

    @MockitoBean
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testGetAllProjects_Success () {
        // Create and save test user
        UserEntity user = new UserEntity ();
        user.setTelegramId (123456789L);
        user.setUsername ("test_user");
        user = userRepository.save (user);

        // Create and save test project
        ProjectEntity project = new ProjectEntity ();
        project.setName ("Test Project");
        project.setDescription ("Description");
        project.setReporterId (user.getId ());
        project.setStatus (ProjectStatus.ACTIVE);
        projectRepository.save (project);

        // Generate JWT token
        String token = jwtService.generateToken (user.getId ());

        // Mutate tester to include Authorization header
        GraphQlTester authenticatedTester = graphQlTester.mutate ()
                .header ("Authorization", "Bearer " + token)
                .build ();

        // GraphQL Query
        String query = """
            query {
                getAllProjects(first: 5) {
                    edges {
                        node {
                            id
                            name
                            reporterId
                        }
                    }
                }
            }
        """;

        // Execute query
        authenticatedTester.document (query)
                .execute ()
                .errors ().verify ()
                .path ("getAllProjects.edges")
                .entityList (Object.class)
                .hasSize (1);
    }
}
