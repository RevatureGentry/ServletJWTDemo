package com.revature.model;

import java.util.List;
import java.util.Objects;

public class AuthenticatedUser implements UserDetails {

	private final String username;
	private final String email;
	private final List<String> roles;

	public AuthenticatedUser(final String username, final String email, final List<String> roles) {
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, roles, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AuthenticatedUser)) {
			return false;
		}
		AuthenticatedUser other = (AuthenticatedUser) obj;
		return Objects.equals(email, other.email) && Objects.equals(roles, other.roles)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthenticatedUser [username=");
		builder.append(username);
		builder.append(", email=");
		builder.append(email);
		builder.append(", roles=");
		builder.append(roles);
		builder.append("]");
		return builder.toString();
	}

}
