package com.mrs.enpoint.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_mobile_number", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
		"mobile_number" }))
public class SavedMobileNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "mobile_number", nullable = false, length = 15)
	private String mobileNumber;

	// operator_id is fetched from mobile_connection by mobile number at save time
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id", nullable = false)
	private Operator operator;

	@Column(name = "nickname", length = 50)
	private String nickname;

	public SavedMobileNumber() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}