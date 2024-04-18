package com.sample.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "병신마스터")
public class QudtlsMaster {

	public QudtlsMaster() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "AGE")
	private int age;

	@Column(name = "JOB")
	private String job;

	@Column(name = "RANK")
	private String rank;

	@Column(name = "REASON")
	private String reason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public QudtlsMaster(Long id, String name, int age, String job, String rank, String reason) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.job = job;
		this.rank = rank;
		this.reason = reason;
	}

}
