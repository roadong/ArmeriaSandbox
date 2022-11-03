package com.gl.springsandbox.api.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


// 생각해보니 테이블 속성(on update)으로 지정하면 되는데 데이터 전송 코스트만 들 꺼 같은데
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeAuditor {
    @Column(name = "CREATE_TIME", updatable = false)
    @CreatedDate
    private LocalDateTime createTime;

    @Column(name = "MODIFY_TIME", insertable = false)
    @LastModifiedDate
    private LocalDateTime modifyTime;
}
