package com.orgacare.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Orga Care.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String dirPieceJointe;

    public String getDirPieceJointe() {
        return dirPieceJointe;
    }

    public void setDirPieceJointe(String dirPieceJointe) {
        this.dirPieceJointe = dirPieceJointe;
    }
}
