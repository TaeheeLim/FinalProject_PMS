package com.kanboo.www.domain.entity.board;

import com.kanboo.www.domain.entity.global.CodeDetail;
import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.dto.board.BoardDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_idx")
    private Member member;
    private String boardCn;
    private LocalDateTime boardDate;
    private String delAt;
    private int totalComments;
    private int totalLikes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_detail_idx")
    private CodeDetail codeDetail;
    private String fileAt;

    public void changeDelAt(String delAt) {
        this.delAt = delAt;
    }

    public BoardDTO entityToDto() {
        BoardDTO build = BoardDTO.builder()
                .boardIdx(boardIdx)
                .member(member.entityToDto())
                .boardCn(boardCn)
                .boardDate(boardDate)
                .delAt(delAt)
                .codeDetail(codeDetail.entityToDto())
                .fileAt(fileAt)
                .totalComments(totalComments)
                .totalLikes(totalLikes)
                .build();
        return build;
    }
}
