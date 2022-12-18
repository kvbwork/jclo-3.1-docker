package ru.netology.rest.repository;

import ru.netology.rest.enums.Authorities;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class UserRepository {
    private final Map<Integer, List<Authorities>> storage = new ConcurrentHashMap<>();

    public void save(String user, String password, List<Authorities> authorities) {
        storage.put(getKey(user, password), authorities);
    }

    private int getKey(String user, String password) {
        return Objects.hash(user, password);
    }

    public List<Authorities> getUserAuthorities(String user, String password) {
        return ofNullable(storage.get(getKey(user, password)))
                .orElseGet(Collections::emptyList);
    }

}
