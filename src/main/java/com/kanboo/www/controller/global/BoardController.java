package com.kanboo.www.controller.global;

import com.kanboo.www.domain.repository.board.boardQueryDSL.BoardDSLRepositoryImpl;
import com.kanboo.www.dto.board.BoardDTO;
import com.kanboo.www.dto.board.BoardFileDTO;
import com.kanboo.www.dto.board.CommentDTO;
import com.kanboo.www.service.inter.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private static final Logger logger
            = LoggerFactory.getLogger(BoardDSLRepositoryImpl.class);

    private final BoardService boardService;

    @GetMapping("/boardTest")
    public List<BoardDTO> testBoard(String selected, String key, int articleOnvView, String codeDetail) {
        return boardService.getAllList(selected, key, articleOnvView, codeDetail);
    }

    @GetMapping("/getArticleNum")
    public long getArticleNum(String key, String selected, String codeDetails) {
        return boardService.getArticleNum(key, selected, codeDetails);
    }

    @GetMapping("/BoardComment")
    public List<CommentDTO> getComments(long boardIdx, int commentsOnView){
        return boardService.getComments(boardIdx, commentsOnView);
    }

    @GetMapping("/DeleteBoard")
    public boolean deleteBoard(long boardIdx){
        return boardService.deleteBoard(boardIdx);
    }

    @PostMapping("/insertBoard")
    public BoardDTO insertBoard(BoardDTO boardDTO, BoardFileDTO boardFileDTO, String token){
        logger.info(token);
        logger.info(boardDTO.toString());
        logger.info(boardFileDTO.toString());
        return null;
    }

    @PostMapping("/insertFile")
    @ResponseBody
    public String insertFile(@ModelAttribute MultipartFile file){
        logger.info(file.getOriginalFilename());

        return "파일 업로드 성공";
    }




}

