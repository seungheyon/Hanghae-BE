package com.hanghae7.alcoholcommunity.domain.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="Notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false)
    private String notice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Notice(String notice, Member member){
        this.notice = notice;
        this.member = member;
    }
}
