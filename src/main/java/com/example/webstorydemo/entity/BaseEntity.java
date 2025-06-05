package com.example.webstorydemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T extends Serializable> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;

	@CreatedBy
	@Column(length = 100)
	private String createdBy;
	
	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;
	
	@LastModifiedBy
	@Column(length = 100)
	private String updatedBy;
	
	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt;
}
