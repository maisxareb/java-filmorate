package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	private org.springframework.context.ApplicationContext context;

	@Test
	void contextLoads() {
		// Проверка, что контекст успешно загружен
		assertThat(context).isNotNull();
	}

	@Test
	void controllersAreLoaded() {
		// Проверка, что контроллеры зарегистрированы в контексте
		assertThat(context.containsBean("filmController")).isTrue();
		assertThat(context.containsBean("userController")).isTrue();
	}

}
