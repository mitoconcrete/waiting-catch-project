package team.waitingcatch.app.user.entitiy;

public enum UserRoleEnum {
	USER(Authority.USER),
	SELLER(Authority.SELLER),
	ADMIN(Authority.ADMIN);

	private final String authority;

	UserRoleEnum(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {
		public static final String USER = "ROLE_USER";
		public static final String ADMIN = "ROLE_ADMIN";
		public static final String SELLER = "ROLE_SELLER";
	}
}
