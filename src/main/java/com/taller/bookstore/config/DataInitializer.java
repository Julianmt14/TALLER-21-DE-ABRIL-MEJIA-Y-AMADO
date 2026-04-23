package com.taller.bookstore.config;

import com.taller.bookstore.entity.Author;
import com.taller.bookstore.entity.Book;
import com.taller.bookstore.entity.Category;
import com.taller.bookstore.entity.Role;
import com.taller.bookstore.entity.User;
import com.taller.bookstore.repository.AuthorRepository;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.CategoryRepository;
import com.taller.bookstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

@Configuration
public class DataInitializer {

    @Bean
    @Profile("dev")
    public CommandLineRunner seedData(UserRepository userRepository,
                                      AuthorRepository authorRepository,
                                      CategoryRepository categoryRepository,
                                      BookRepository bookRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@bookstore.com")) {
                User admin = new User();
                admin.setFullName("Bookstore Admin");
                admin.setEmail("admin@bookstore.com");
                admin.setPassword(passwordEncoder.encode("Admin1234"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            if (!userRepository.existsByEmail("user@bookstore.com")) {
                User user = new User();
                user.setFullName("Default User");
                user.setEmail("user@bookstore.com");
                user.setPassword(passwordEncoder.encode("User12345"));
                user.setRole(Role.USER);
                userRepository.save(user);
            }

            if (authorRepository.count() == 0 && categoryRepository.count() == 0 && bookRepository.count() == 0) {
                Author authorOne = new Author();
                authorOne.setName("Gabriel Garcia Marquez");
                authorOne.setBiography("Novelista colombiano y premio Nobel.");
                authorOne.setContactInfo("contacto@ggm.com");

                Author authorTwo = new Author();
                authorTwo.setName("Isabel Allende");
                authorTwo.setBiography("Escritora chilena reconocida por su narrativa contemporanea.");
                authorTwo.setContactInfo("contacto@allende.com");

                authorRepository.save(authorOne);
                authorRepository.save(authorTwo);

                Category fiction = new Category();
                fiction.setName("Ficcion");
                fiction.setDescription("Novelas y relatos de ficcion.");

                Category latin = new Category();
                latin.setName("Literatura latinoamericana");
                latin.setDescription("Autores y obras de America Latina.");

                categoryRepository.save(fiction);
                categoryRepository.save(latin);

                Book bookOne = new Book();
                bookOne.setTitle("Cien anos de soledad");
                bookOne.setDescription("Clasico del realismo magico.");
                bookOne.setPrice(new BigDecimal("59.90"));
                bookOne.setStock(20);
                bookOne.setIsbn("9780307474728");
                bookOne.setAuthor(authorOne);
                bookOne.setCategories(new LinkedHashSet<>(java.util.List.of(fiction, latin)));

                Book bookTwo = new Book();
                bookTwo.setTitle("La casa de los espiritus");
                bookTwo.setDescription("Saga familiar con elementos historicos y fantasticos.");
                bookTwo.setPrice(new BigDecimal("47.50"));
                bookTwo.setStock(15);
                bookTwo.setIsbn("9781501117015");
                bookTwo.setAuthor(authorTwo);
                bookTwo.setCategories(new LinkedHashSet<>(java.util.List.of(fiction, latin)));

                bookRepository.save(bookOne);
                bookRepository.save(bookTwo);
            }
        };
    }
}
