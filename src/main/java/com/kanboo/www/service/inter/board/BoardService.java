package com.kanboo.www.service.inter.board;


import com.kanboo.www.domain.entity.board.Board;
import com.kanboo.www.domain.entity.board.Comment;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.board.CommentDTO;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getAllList(String selected, String key, int articleOnvView, String codeDetail);

    long getArticleNum(String keyword, String selected, String codeDetails);

    List<CommentDTO> getComments(long boardIdx, int commentsOnView);

    boolean deleteBoard(long boardIdx);

}
