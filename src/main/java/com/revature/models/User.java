package com.revature.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
	

	private List<Book> wishlist;
	
	@JsonIgnore
	private List<Book> favorites;
	
	public User() {
		super();
	}
	
	public User(Builder builder) {
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.role = builder.role;
		this.wishlist = builder.wishlist;
		this.favorites = builder.favorites;
	}
	
	public User(User copy) {
		this.id = copy.getId();
		this.username = copy.getUsername(); 
		this.password = copy.getPassword();
		this.firstName = copy.getFirstName();
		this.lastName = copy.getLastName();
		this.role = copy.getRole();
		this.wishlist = copy.getWishlist();
		this.favorites = copy.getFavorites();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Book> getWishlist() {
		return wishlist;
	}

	public void setWishlist(List<Book> wishlist) {
		this.wishlist = wishlist;
	}
	
	public List<Book> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Book> favorites) {
		this.favorites = favorites;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((favorites == null) ? 0 : favorites.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((wishlist == null) ? 0 : wishlist.hashCode());
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
		User other = (User) obj;
		if (favorites == null) {
			if (other.favorites != null)
				return false;
		} else if (!favorites.equals(other.favorites))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (wishlist == null) {
			if (other.wishlist != null)
				return false;
		} else if (!wishlist.equals(other.wishlist))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", role=" + role + ", wishlist=" + wishlist + ", favorites=" + favorites
				+ "]";
	}
	
	public static class Builder {
		private int id;
		private String username;
		private String password;
		private String firstName;
		private String lastName;
		private Role role;
		private List<Book> wishlist;
		private List<Book> favorites;
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder role(Role role) {
			this.role = role;
			return this;
		}
		
		public Builder wishlist(List<Book> wishlist) {
			this.wishlist = wishlist;
			return this;
		}
		
		public Builder favorites(List<Book> favorites) {
			this.favorites = favorites;
			return this;
		}
	}
}
