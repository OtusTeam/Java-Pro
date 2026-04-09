package ru.otus;

public class UserService {
    private final UserRepository userRepository;
    private final GreetingService greetingService;

    public UserService(UserRepository userRepository, GreetingService greetingService) {
        this.userRepository = userRepository;
        this.greetingService = greetingService;
    }

    public String buildGreetingForUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        return greetingService.greet(user.getName());
    }
}
