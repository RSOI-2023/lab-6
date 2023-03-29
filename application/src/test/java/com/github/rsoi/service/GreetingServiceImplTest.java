package com.github.rsoi.service;

import com.github.rsoi.domain.Greeting;
import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.dto.GreetingResponse;
import com.github.rsoi.repository.GreetingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class GreetingServiceImplTest {

    @InjectMocks
    private GreetingServiceImpl greetingService;

    @Mock
    private GreetingRepository greetingRepository;

    @Test
    void getGreeting() {
        // Given (Arrange)
        // В этом блоке находится буквально всё то, что дано изначально.
        // Создаём объекты, настраиваем объекты, добавляем записи в базу и т.д.
        // В конкретно нашем случае создаём объект, который якобы лежит в репозитории (и базе)
        // и настраиваем сам репозиторий (mock-репозиторий) так, чтобы он отдавал данный объект при запросе по id
        Greeting expectedGreeting = new Greeting();
        expectedGreeting.setId(123L);
        expectedGreeting.setText("Service Test greeting");
        when(greetingRepository.getReferenceById(expectedGreeting.getId()))
                .thenReturn(expectedGreeting);

        // When (Act)
        // Вызов логики, которую тестируем.
        // Spring подложил сервису mock-репозиторий вместо настоящего, что позволяет протестировать
        // только логику сервиса, ведь все зависимости и входные данные мы контролируем сами.
        var actualGreeting = greetingService.getGreeting(expectedGreeting.getId());

        // Then (Assert)
        // Проверяем результаты. Тестируемая логика может вернуть какой-то результат
        // или изменить состояние объекта/базы. Здесь как раз сравниваем, соответствует
        // ли результат ожидаемому.
        // В нашем случае сравниваем то, что getGreeting вернул тот же текст приветствия,
        // какой мы изначально сохранили в mock-репозиторий.
        assertEquals(expectedGreeting.getId(), actualGreeting.getId());
        assertEquals(expectedGreeting.getText(), actualGreeting.getText());
    }

    @Test
    void createGreeting() {
        // Given (Arrange)
        // Аналогично с предыдущим кодом, но здесь делаем mock не только на getReferenceById,
        // но и на save (он будет вызван при вызове тестируемого метода сервиса)
        Greeting greetingToSave = new Greeting();
        greetingToSave.setId(42L);
        greetingToSave.setText("Hi1");
        when(greetingRepository.save(any(Greeting.class))).thenReturn(greetingToSave);
        when(greetingRepository.getReferenceById(greetingToSave.getId())).thenReturn(greetingToSave);

        // When (Act)
        // Вызываем метод, который нужно протестировать. И только его! 
        var request = new CreateGreetingRequest(greetingToSave.getText());
        GreetingResponse actualGreeting = greetingService.createGreeting(request);

        // Then (Assert)
        // Сравниваем результат (actual) с ожидаемым (expected).
        // Здесь мы используем getGreeting, но считаем, что он работает правильно,
        // так как данный метод тестирует только createGreeting и ничего более.
        var expectedGreeting = greetingService.getGreeting(actualGreeting.getId());
        assertEquals(expectedGreeting.getId(), actualGreeting.getId());
        assertEquals(expectedGreeting.getText(), actualGreeting.getText());
    }

    @Test
    void testRepositoryInit(){
        // Given (Arrange)
        var automaticallyGeneratedGreeting = new Greeting();
        automaticallyGeneratedGreeting.setId(null);
        automaticallyGeneratedGreeting.setText("Hello world from Spring Service Bean!");
        when(greetingRepository.findAll())
                .thenReturn(List.of());
        greetingService = new GreetingServiceImpl(greetingRepository);

        // When (Act)
        greetingService.init();

        // Then (Assert)
        verify(greetingRepository, times(1)).findAll();
        verify(greetingRepository, times(1)).save(automaticallyGeneratedGreeting);
    }

}