package de.falbers.jwt;

public record TokenRequest(String login, String secret) {
}
