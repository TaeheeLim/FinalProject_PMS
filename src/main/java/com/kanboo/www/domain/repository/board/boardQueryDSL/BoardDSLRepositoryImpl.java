package com.kanboo.www.domain.repository.board.boardQueryDSL;

import com.kanboo.www.domain.entity.board.*;
import com.kanboo.www.domain.entity.global.QCodeDetail;
import com.kanboo.www.domain.entity.global.QMasterCode;
import com.kanboo.www.domain.entity.member.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class BoardDSLRepositoryImpl implements BoardDSLRepository{

    private final EntityManager em;
    private static final Logger logger
            = LoggerFactory.getLogger(BoardDSLRepositoryImpl.class);
//    private JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    @Override
    public List<Board> getAllList(String selected, String key, int articleOnvView, String codeDetails) {
        QBoard board = QBoard.board;
        QLikes likes = QLikes.likes;
        QBoardReport reports = QBoardReport.boardReport;
        QMember member = QMember.member;
        QCodeDetail codeDetail = QCodeDetail.codeDetail;

        JPAQueryFactory query = new JPAQueryFactory(em);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")) {
            booleanBuilder.and(
                    board.member.memNick.contains(key)
            );
        } else if(selected.equals("boardCN")) {
            booleanBuilder.and(
                    board.boardCn.contains(key)
            );
        } else if(selected.equals("All")){
            booleanBuilder.or(board.member.memNick.contains(key))
                          .or(board.boardCn.contains(key));
        }

        return query
            .select(board)
            .from(board)
            .innerJoin(board.member, member)
            .fetchJoin()
            .innerJoin(board.codeDetail, codeDetail)
            .fetchJoin()
                .where(
                    (JPAExpressions
                                    .select(reports.count())
                                    .from(reports)
                                    .where(reports.board.boardIdx.eq(board.boardIdx))
                    ).lt(5L)
                            .and(board.delAt.eq("N"))
                            .and(board.codeDetail.codeDetailIdx.eq(codeDetails))
                            .and(booleanBuilder)
            )
            .distinct()

            .offset(articleOnvView) .limit(5)
            .fetch();
    }

    @Override
    public long getArticleNum(String keyword, String selected, String codeDetails){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QBoard board = QBoard.board;
        QBoardReport reports = QBoardReport.boardReport;
        QMember member = QMember.member;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(selected.equals("memNick")){
            booleanBuilder.and(board.member.memNick.contains(keyword));
        } else if(selected.equals("boardCN")){
            booleanBuilder.and(board.boardCn.contains(keyword));
        } else if(selected.equals("All")){
            booleanBuilder.and(board.member.memNick.contains(keyword))
                    .or(board.boardCn.contains(keyword));
        }

        return query.select(board.count())
                .from(board, member)
                .where(board.codeDetail.codeDetailIdx.eq(codeDetails)
                        .and(board.member.memIdx.eq(member.memIdx))
                        .and(board.delAt.eq("N"))
                        .and(booleanBuilder)
                        .and((JPAExpressions
                                .select(reports.count())
                                .from(reports)
                                .where(reports.board.boardIdx.eq(board.boardIdx))
                        ).lt(5L))
                )
                .fetchCount();
    }

    @Override
    public List<Comment> getComments(long boardIdx, int commentsOnView) {
        QComment comment = QComment.comment;
        QBoard qBoard = QBoard.board;
        QMember qMember = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(comment)
                        .from(comment)
                        .innerJoin(comment.board, qBoard)
                        .fetchJoin()
                        .innerJoin(comment.member, qMember)
                        .fetchJoin()
                        .where(comment.board.boardIdx.eq(boardIdx))
                        .offset(commentsOnView)
                        .limit(5)
                        .fetch();
    }



}
