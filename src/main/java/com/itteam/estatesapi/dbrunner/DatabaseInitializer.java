package com.itteam.estatesapi.dbrunner;

import com.itteam.estatesapi.model.Estate;
import com.itteam.estatesapi.model.User;
import com.itteam.estatesapi.security.WebSecurityConfig;
import com.itteam.estatesapi.security.oauth2.OAuth2Provider;
import com.itteam.estatesapi.service.EstateService;
import com.itteam.estatesapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final EstateService estateService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });
        ESTATES.forEach(estate ->{
            estate.setId(UUID.randomUUID().toString());
            estateService.saveEstate(estate);
        });
        log.info("Database initialized");
    }
      private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@mycompany.com", WebSecurityConfig.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@mycompany.com", WebSecurityConfig.USER, null, OAuth2Provider.LOCAL, "2")
    );
    private static final List<Estate> ESTATES = Arrays.asList(
            new Estate("Жилой комплекс ABAY 130", "https://krisha-photos.kcdn.online/08/82dcdcbfdc8571338c3929efe09998139f6c48/photo-750x470.jpg", "Новостройка бизнес-класса жилой комплекс Abay 130 находится в хорошем районе Алматы. ЖК отличается оригинальными архитектурными и дизайнерскими решениями. Abay 130 − это 9-этажный жилой комплекс, расположенный в самом центре города, на пересечении пр. Абая и ул. Розыбакиева в Бостандыкском районе Алматы.", "+77055051100", 770000, "Алматы, Бостандыкский р-н, проспект Абая, 130"),
            new Estate( "ЖК Terra в Алматы","https://alakcell-photos-kr.kcdn.kz/complex/00/c488b8be8bb1b5f9d99d310aa66e3506529229/photo.jpg","Концептуальный жилой комплекс «Терра» – масштабный проект, представляющий собой целых пять 16-этажных блоков, размещенных на двух гектарах земли. Квадрат территория обрамлен улицами Гагарина, Березовского, Радостовца и Короленко. Общая площадь комплекса составляет более ста квадратных метров.", "+77771000909", 820000, "Алматы, Бостандыкский р-н, пр. Гагарина, 233"),
            new Estate("Жилой комплекс NEBO","https://sensata.kz/img/projects/709675bc55cfdb7967d115cb0e540604.jpg", "Проект, который решил привнести в городской темп жизни размеренность и свободу, гармонию и баланс. рхитектурная концепция предусматривает лаконичные фасады, которые создают особое восприятие пространства", "+77083335566", 670000, "Алматы, Турксибский район, ул. Майлина 238"),
            new Estate("RIVERSIDE LOFT","https://sensata.kz/img/projects/a9199fd1502392f12fc9c853c4db7a7d.jpg", "Крупный районный город Гаггенау привлекает туристов зеленым разнообразием, его так и называют – цветущий город на Мурге. Благодаря акцентам городского развития с широким спектром медицинских, образовательных, культурных предложений, город является популярным местом для жизни.", "+77714507345", 980000, "г. Гаггенау, юго-запад Германии")
    );
}
