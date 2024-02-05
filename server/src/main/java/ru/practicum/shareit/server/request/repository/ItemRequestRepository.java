package ru.practicum.shareit.server.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByAuthorIdOrderByCreatedDesc(long userId);

    Page<ItemRequest> findAllByAuthorIdNot(long authorId, PageRequest pageRequest);
}
