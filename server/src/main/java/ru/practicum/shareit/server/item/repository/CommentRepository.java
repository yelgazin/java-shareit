package ru.practicum.shareit.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.item.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
