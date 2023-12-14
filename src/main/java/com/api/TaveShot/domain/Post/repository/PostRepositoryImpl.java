package com.api.TaveShot.domain.Post.repository;


import static com.api.TaveShot.domain.Post.domain.QPost.post;

import com.api.TaveShot.domain.Post.dto.PostResponse;
import com.api.TaveShot.domain.Post.dto.PostSearchCondition;
import com.api.TaveShot.domain.Post.dto.QPostResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostResponse> searchPagePost(PostSearchCondition condition, Pageable pageable) {

        List<PostResponse> postResponses = getSearchPageContent(condition, pageable);
        JPAQuery<Long> searchPageCount = getSearchPageCount(condition);

        return PageableExecutionUtils.getPage(postResponses, pageable, searchPageCount::fetchOne);
    }

    private List<PostResponse> getSearchPageContent(PostSearchCondition condition, Pageable pageable) {
        return jpaQueryFactory
                .select(
                        new QPostResponse(post.id, post.title, post.content,
                                post.writer, post.viewCount, post.member.id))
                .from(post)
                .where(
                        containTitle(condition.getTitle()),
                        containContent(condition.getContent()),
                        containWriter(condition.getWriter())
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private JPAQuery<Long> getSearchPageCount(PostSearchCondition condition) {
         return jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .where(
                        containTitle(condition.getTitle()),
                        containContent(condition.getContent()),
                        containWriter(condition.getWriter())
                );
    }

    private BooleanExpression containTitle(String title) {
        if (StringUtils.hasText(title)) {
            return post.title.contains(title);
        }
        return null;
    }

    private BooleanExpression containContent(String content) {
        if (StringUtils.hasText(content)) {
            return post.content.contains(content);
        }
        return null;
    }

    private BooleanExpression containWriter(String writer) {
        if (StringUtils.hasText(writer)) {
            return post.writer.contains(writer);
        }
        return null;
    }

}
