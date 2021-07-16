package android.termix.ssc.ce.sharif.edu.model;

import java.io.IOException;

public class Account {
    private final String email;
    private final Role role;
    private final boolean verified;

    public Account(String email, Role role, boolean verified) {
        this.email = email;
        this.role = role;
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isVerified() {
        return verified;
    }

    enum Role {
        ADMIN,
        STUDENT
    }

    public static Role getRole(String role) throws IOException {
        if (role.equals("std")) {
            return Role.STUDENT;
        } else if (role.equals("admin")) {
            return Role.ADMIN;
        } else {
            throw new IOException("Invalid pocket");
        }
    }
}