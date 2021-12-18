package com.kanboo.www.domain.repository.board;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.Comment;
import com.kanboo.www.domain.repository.board.boardQueryDSL.BoardDSLRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardDSLRepository {
    List<Board> getAllList(String selected, String key, int articleOnvView, String codeDetail);

    long getArticleNum(String keyword, String selected, String codeDetails);

    List<Comment> getComments(long boardIdx, int commentsOnView);

    Board findByBoardIdx(long boardIdx);

    long deleteByBoardIdx(long boardIdx);
}
