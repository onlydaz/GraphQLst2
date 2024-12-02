package ltweb.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import jakarta.transaction.Transactional;
import ltweb.entity.Category;
import ltweb.entity.User;
import ltweb.repository.CategoryRepository;
import ltweb.repository.UserRepository;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public UserResolver(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // Query: Lấy tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Query: Lấy tất cả category
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Query: Lấy category của một user
    public List<Category> getCategoriesByUser(Long userId) {
        return userRepository.findById(userId)
                .map(User::getCategories)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Mutation: Tạo user
    @Transactional
    public User createUser(UserInput input) {
        User user = new User();
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());
        user.setPhone(input.getPhone());
        return userRepository.save(user);
    }

    // Mutation: Cập nhật user
    @Transactional
    public User updateUser(Long id, UserInput input) {
        return userRepository.findById(id).map(user -> {
            user.setFullname(input.getFullname());
            user.setEmail(input.getEmail());
            user.setPhone(input.getPhone());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Mutation: Xóa user
    @Transactional
    public Boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return true;
    }
}
