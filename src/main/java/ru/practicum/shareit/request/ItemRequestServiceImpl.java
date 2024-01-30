package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequest create(long userId, ItemRequest itemRequest) {
        log.debug("Создание пользователем {} запроса вещи \"{}\"", userId, itemRequest.getDescription());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id %d не найден.", userId));

        itemRequest.setAuthor(user);

        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getAllMyRequests(long userId) {
        log.debug("Получение запросов вещей пользователя с id {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id %d не найден.", userId);
        }

        return itemRequestRepository.findAllByAuthorIdOrderByCreatedDesc(userId);
    }

    @Override
    public List<ItemRequest> getAllNotUserRequests(long userId, long from, int size) {
        log.debug("Получение запросов вещей других пользователей");

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id %d не найден.", userId);
        }

        PageRequest pageRequest = PageRequest.of((int) (from / size), size, Sort.by("created").descending());

        return itemRequestRepository.findAllByAuthorIdNot(userId, pageRequest).getContent();
    }

    @Override
    public ItemRequest getByRequestId(long userId, long requestId) {
        log.debug("Получение запроса вещи по id {}", requestId);

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id %d не найден.", userId);
        }

        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Запрос с id %d не найден.", requestId));
    }
}
