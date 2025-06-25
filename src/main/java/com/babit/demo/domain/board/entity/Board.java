package com.babit.demo.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키를 자동으로 증가시킴 
    private Long id; // null 값이어도 오류가 발생하지 않기에 보통 Integer을 사용 

    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false) //columnDefinition : DB의 DDL에 들어갈 SQL 구문을 직접 입력, 기본값이나 특수타입이 필요할 때 사용 
    private String content;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "like_cnt", nullable = false)
    private Long likeCnt = 0;

    @Column(name = "view_cnt", nullable = false)
    private Long viewCnt = 0;

    // 작성자 (user)
    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne : Board -> User 다대일 관계 / LAZY : Board 조회 시 User도 함께 조회되는 게 아니라 board.getUser() 호출 시에 User를 DB에서 로딩함. 
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_boards_users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user; 

}

