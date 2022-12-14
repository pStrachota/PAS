package pl.lodz.p.pas.manager;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import pl.lodz.p.pas.dto.UserDto;
import pl.lodz.p.pas.dto.mapper.UserDtoMapper;
import pl.lodz.p.pas.exception.DuplicatedLoginException;
import pl.lodz.p.pas.exception.ItemNotFoundException;
import pl.lodz.p.pas.model.user.User;
import pl.lodz.p.pas.repository.UserRepo;

@Stateless
public class UserManager {

    @EJB
    UserRepo userDbRepo;

    public void addUser(UserDto userDto) {
        User user = UserDtoMapper.mapToUser(userDto);
        try {
            userDbRepo.add(user);
        } catch (PersistenceException | EJBException e) {
            if (e.getCause().getMessage().contains("ConstraintViolationException")) {
                throw new DuplicatedLoginException(
                        "User with " + user.getLogin() + " login already exists");
            }
        }
    }

    public void removeUser(Long id) {
        User user = userDbRepo.findByID(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));
        userDbRepo.remove(user);
    }

    public List<User> findByLoginContains(String login) {
        return userDbRepo.findByLoginContains(login);
    }

    public List<User> findByNameContains(String name) {
        return userDbRepo.findByNameContains(name);
    }

    public User getUser(long id) {
        return userDbRepo.findByID(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));
    }

    public List<User> getUsers() {
        return userDbRepo.getItems();
    }

    public User activateUser(long id) {
        User user = userDbRepo.findByID(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));
        user.setActive(true);
        userDbRepo.update(id, user);
        return user;
    }

    public User deactivateUser(long id) {
        User user = userDbRepo.findByID(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));
        user.setActive(false);
        userDbRepo.update(id, user);
        return user;
    }

    public void updateUser(Long id, UserDto userDto) {

        User user = userDbRepo.findByID(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found"));

        User updatedUser = UserDtoMapper.mapToUser(userDto);

        try {
            userDbRepo.update(id, updatedUser);
        } catch (EJBException e) {
            if (e.getCause().getMessage().contains("ConstraintViolationException")) {
                throw new ItemNotFoundException(
                        "User with " + user.getLogin() + " login already exists");
            }
        }
    }
}
