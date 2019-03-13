package com.revature.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WishList {
	
	private int userId;
	private List<Book> userWishList;
	
	public WishList() {
		super();
		this.userWishList = new ArrayList<>();
	}
	
	public WishList(int userId) {
		super();
		this.userId = userId;
		this.userWishList = new ArrayList<>();
	}
	
	public WishList(int userId, Book... userWishList) {
		super();
		this.userId = userId;
		this.userWishList = Arrays.asList(userWishList);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Book> getUserWishList() {
		return userWishList;
	}

	public void setUserWishList(List<Book> userWishList) {
		this.userWishList = userWishList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		result = prime * result + ((userWishList == null) ? 0 : userWishList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WishList other = (WishList) obj;
		if (userId != other.userId)
			return false;
		if (userWishList == null) {
			if (other.userWishList != null)
				return false;
		} else if (!userWishList.equals(other.userWishList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WishList [user=" + userId + ", userWishList=" + userWishList + "]";
	}

}
