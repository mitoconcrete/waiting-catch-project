package team.waitingcatch.app.event.enums;

public enum CouponRoleEnum {
    PRICE(Authority.PRICE),
    PERCENT(Authority.PERCENT);

    private final String authority;

    CouponRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String PRICE = "PRICE";
        public static final String PERCENT = "PERCENT";
    }
}