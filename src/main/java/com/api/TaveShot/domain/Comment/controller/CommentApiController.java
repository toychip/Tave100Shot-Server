package com.api.TaveShot.domain.Comment.controller;

import com.api.TaveShot.domain.Comment.dto.CommentDto;
import com.api.TaveShot.domain.Comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    /* CREATE */
    @PostMapping("/post/{id}/comments")
    public ResponseEntity<Long> save(@PathVariable Long id, @RequestBody CommentDto.Request dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String gitIdAsString = authentication.getName();

        Long gitId = Long.valueOf(gitIdAsString);
        return ResponseEntity.ok(commentService.save(id, gitId, dto));
    }

    /* READ */
    @GetMapping("/post/{id}/comments")
    public List<CommentDto.Response> read(@PathVariable Long id) {
        return commentService.findAll(id);
    }

    /* UPDATE */
    @PutMapping({"/post/{postId}/comments/{id}"})
    public ResponseEntity<Long> update(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto.Request dto) {
        commentService.update(postId, id, dto);
        return ResponseEntity.ok(id);
    }

    /* DELETE */
    @DeleteMapping("/post/{postId}/comments/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long postId, @PathVariable Long id) {
        commentService.delete(postId, id);
        return ResponseEntity.ok(id);
    }
}