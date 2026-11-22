package com.controller;

import com.entity.Conference;
import com.entity.User;
import com.service.ConferenceService;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ConferenceController {
    private final ConferenceService service;
    private final User currentUser;

    public ConferenceController(ConferenceService service, User currentUser) {
        this.service = Objects.requireNonNull(service);
        this.currentUser = Objects.requireNonNull(currentUser);
    }

    public List<Conference> loadConferences() throws SQLException {
        return service.getAllConferences();
    }

    public void create(Conference conference) throws SQLException {
        service.createConference(currentUser, conference);
    }

    public void update(Conference conference) throws SQLException {
        service.updateConference(currentUser, conference);
    }

    public void delete(int conferenceId) throws SQLException {
        service.deleteConference(currentUser, conferenceId);
    }
}