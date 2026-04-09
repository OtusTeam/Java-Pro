package ru.otus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GreetingServiceTestMk {
    @Mock
    private UserRepository userRepository;

    @Mock
    private  GreetingService greetingService;

    @InjectMocks
    private  UserService userService;


    @Test
    @DisplayName("check something")
    void shouldBuildGreetingForExisitingUser() {
        User user = new User(1L,"Alex");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        verify(userRepository).findById(1L);
    }

}
