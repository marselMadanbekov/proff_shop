package com.profi_shop.security;

public class SecurityConstants {
    public static final String ADMIN_URLS = "/admin/**";
    public static final String SUPER_ADMIN_URLS = "/admin/superAdmin/**";
    public static final String SECRET = "SecretKeyGenJWTMarselMadanbekovKubanychbekovichSecretKeyGenJWTMarselMadanbekovKubanychbekovich";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 600_000_000;
}
