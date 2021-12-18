package com.kanboo.www.domain.repository.board.boardQueryDSL;

import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardDSLRepository {

    List<Board> getAllList(String selected, String key, int articleOnvView, String codeDetail);

    long getArticleNum(String keyword, String selected, String codeDetails);

    List<Comment> getComments(long boardIdx, int commentsOnView);

}
