package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private static UserService service;

    private UserService() {}

    public static UserService getInstance() {
        if (service == null) {
            service = new UserService();
        }

        return service;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        dataBase.forEach((k, v) -> list.add(v));

        return list;
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        user.setId(maxId.getAndIncrement());

        if (!isExistsThisUser(user)) {
            dataBase.put(user.getId(), user);
            return true;
        }

        return false;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        return dataBase.containsValue(user);
    }

    public List<User> getAllAuth() {
        List<User> list = new ArrayList<>();
        authMap.forEach((k, v) -> list.add(v));

        return list;
    }

    public boolean authUser(User user) {
        if (isExistsThisUser(user) & !authMap.containsValue(user)) {
            getAllUsers().forEach(x -> { if (x.equals(user)) user.setId(x.getId()); });
            authMap.put(user.getId(), user);
            return true;
        }

        if (authMap.containsValue(user)) return false;

        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);

    }
}
